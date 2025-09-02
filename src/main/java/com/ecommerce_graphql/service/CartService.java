package com.ecommerce_graphql.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce_graphql.DTO.CartDTO;
import com.ecommerce_graphql.DTO.CartItemDTO;
import com.ecommerce_graphql.model.Cart;
import com.ecommerce_graphql.model.CartItem;
import com.ecommerce_graphql.model.Product;
import com.ecommerce_graphql.model.User;
import com.ecommerce_graphql.repository.CartItemRepository;
import com.ecommerce_graphql.repository.CartRepository;
import com.ecommerce_graphql.repository.ProductRepository;
import com.ecommerce_graphql.repository.UserRepository;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       ProductRepository productRepository,
                       UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // ✅ Fetch Cart by User ID
 /*   public CartDTO getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        return mapCart(cart);
    }*/
    
    public CartDTO getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .map(this::mapCart)
                .orElse(null); // ✅ Return null instead of throwing an exception
    }

    // ✅ Add item to cart
    //public CartItemDTO addItemToCart(Long userId, CartItemDTO cartItemDTO) {
    public CartDTO addItemToCart(Long userId, int quantity, Long productId, Boolean addToCart) {
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            newCart.setUser(user);
            newCart.setCreatedAt(LocalDateTime.now());
            return cartRepository.save(newCart);
        });

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product);

        CartItem cartItem;
        if (existingItem.isPresent()) {
            cartItem = existingItem.get();
            System.out.println("quantity in existing item = "+ quantity);
            System.out.println("cartItem.getQuantity() in existing item = "+ cartItem.getQuantity());
            if(addToCart) quantity = cartItem.getQuantity()+ quantity;   
            cartItem.setQuantity(quantity);
        } else {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setPrice(product.getPrice());
        }

        cartItem = cartItemRepository.save(cartItem);
        //return mapCartItem(cartItem);
        return mapCart(cart);
    }

    // ✅ Update quantity
    public CartDTO  updateCartItemQuantity(Long userId, int quantity, Long productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No Cart Found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new RuntimeException("No Item in Cart"));

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
        return mapCart(cart);
    }

    // ✅ Remove one item
    @Transactional
    public CartDTO removeCartItem(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No Cart Found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new RuntimeException("No Item in Cart"));

        cartItemRepository.delete(cartItem);
        cartItemRepository.flush();
        return getCartByUserId(userId);
    }

    // ✅ Clear all items
    @Transactional
    public void clearAllCartItems(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No Cart Found"));

        cartItemRepository.deleteAllByCartId(cart.getId());
    }

    // -------------------------------
    // 📌 Manual Mapping Methods
    // -------------------------------

    private CartDTO mapCart(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUser().getId());
        dto.setCreatedAt(cart.getCreatedAt());
        dto.setItems(cart.getItems().stream()
                .map(this::mapCartItem)
                .collect(Collectors.toList()));
        return dto;
    }

    private CartItemDTO mapCartItem(CartItem item) {
        CartItemDTO dto = new CartItemDTO();
        dto.setId(item.getId());
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        return dto;
    }
}
