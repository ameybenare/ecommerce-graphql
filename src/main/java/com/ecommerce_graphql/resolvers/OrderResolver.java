package com.ecommerce_graphql.resolvers;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce_graphql.DTO.OrderDTO;
import com.ecommerce_graphql.DTO.OrderItemDTO;
import com.ecommerce_graphql.DTO.ProductDTO;
import com.ecommerce_graphql.service.CartService;
import com.ecommerce_graphql.service.OrderService;
import com.ecommerce_graphql.service.ProductService;

@Controller
public class OrderResolver {

    private final OrderService orderService;
    private final CartService cartService;
    private final ProductService productService;

    public OrderResolver(OrderService orderService,
                         CartService cartService,
                         ProductService productService) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.productService = productService;
    }

    // ---- Queries ----
    @QueryMapping
    public List<OrderDTO> ordersByUser(@Argument Long userId) {
        return orderService.getOrderByUserId(userId);
    }
 

    @MutationMapping
    @Transactional
    public OrderDTO createOrder(@Argument Long userId) {
        OrderDTO order = orderService.createOrderFromCart(userId);
        cartService.clearAllCartItems(userId); // clear after checkout
        return order;
    }

    
    @SchemaMapping(typeName = "OrderItem", field = "subtotal")
    public Double subtotal(OrderItemDTO item) {
        return item.getPrice().doubleValue() * item.getQuantity();
    }

    @SchemaMapping(typeName = "OrderItem", field = "product")
    public ProductDTO product(OrderItemDTO item) {
        return productService.getProductById(item.getProductId());
    }
}
