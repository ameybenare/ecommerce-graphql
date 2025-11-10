package com.ecommerce_graphql.service;

import com.ecommerce_graphql.DTO.OrderDTO;
import com.ecommerce_graphql.DTO.OrderItemDTO;
import com.ecommerce_graphql.model.Cart;
import com.ecommerce_graphql.model.CartItem;
import com.ecommerce_graphql.model.Order;
import com.ecommerce_graphql.model.OrderItem;
import com.ecommerce_graphql.model.Product;
import com.ecommerce_graphql.model.User;
import com.ecommerce_graphql.repository.CartRepository;
import com.ecommerce_graphql.repository.OrderItemRepository;
import com.ecommerce_graphql.repository.OrderRepository;
import com.ecommerce_graphql.repository.ProductRepository;
import com.ecommerce_graphql.repository.UserRepository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        ProductRepository productRepository,
                        UserRepository userRepository,
                        CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    @Cacheable(value = "order", key = "#userId")
    public List<OrderDTO> getOrderByUserId(Long userId ){
    	 List<Order> orders = orderRepository.findByUserId(userId); 
    	 System.out.println("Fetching orders for user " + userId + " from DB");
        if (orders.isEmpty()) {
            throw new RuntimeException("No orders found for user " + userId);
        }
        return orders.stream().map(this::mapToDTO).collect(Collectors.toList());

        
    }
    


    @Transactional
    @CacheEvict(value = {"order"}, allEntries = true)
    public OrderDTO createOrder(OrderDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        for (OrderItemDTO itemDTO : dto.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setPrice(itemDTO.getPrice());

            orderItemRepository.save(orderItem);
        }

        return mapToDTO(savedOrder);
    }
    
    @Transactional
    @CacheEvict(value = {"order"}, allEntries = true)
    public OrderDTO createOrderFromCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setTotal(BigDecimal.ZERO);
        Order savedOrder = orderRepository.save(order);

        BigDecimal totalOrderPrice = BigDecimal.ZERO;
        List<OrderItem> orderItemList = new ArrayList<OrderItem>();
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            totalOrderPrice = totalOrderPrice
            		.add(BigDecimal.valueOf(cartItem.getQuantity()).multiply(cartItem.getPrice()));
            orderItemList.add(orderItem);
           
        }
        orderItemRepository.saveAll(orderItemList);
        order.setTotal(totalOrderPrice);
		order = orderRepository.save(order);
		order.setItems(orderItemList);
        return mapToDTO(savedOrder);
    }

    private OrderDTO mapToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        LocalDateTime localDateTime = order.getOrderDate();
        if(localDateTime!=null) {
        	  dto.setOrderDate(localDateTime.atOffset(ZoneOffset.UTC));	
        }
        
        
        List<OrderItemDTO> items = order.getItems()
                .stream()
                .map(item -> {
                    OrderItemDTO itemDTO = new OrderItemDTO();
                    itemDTO.setId(item.getId());
                    itemDTO.setProductId(item.getProduct().getId());
                    itemDTO.setQuantity(item.getQuantity());
                    itemDTO.setPrice(item.getPrice());
                    itemDTO.setProductName(item.getProduct().getName());
                    return itemDTO;
                })
                .collect(Collectors.toList());

        dto.setItems(items);
        return dto;
    }
}
