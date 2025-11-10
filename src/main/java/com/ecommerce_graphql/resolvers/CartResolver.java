package com.ecommerce_graphql.resolvers;
import com.ecommerce_graphql.DTO.CartDTO;
import com.ecommerce_graphql.DTO.CartItemDTO;
import com.ecommerce_graphql.DTO.ProductDTO;
import com.ecommerce_graphql.repository.ReviewRepository;
import com.ecommerce_graphql.service.CartService;
import com.ecommerce_graphql.service.ProductService;

import org.springframework.stereotype.Controller;
import org.springframework.graphql.data.method.annotation.*;

import java.math.BigDecimal;
import java.util.List;
@Controller
public class CartResolver {
  private final CartService cartService;
  private final ProductService productService;

  public CartResolver(CartService cartService, ProductService productService) {
    this.cartService = cartService; this.productService = productService;
  }

  @QueryMapping
  public CartDTO cartByUser(@Argument Long userId) { return cartService.getCartByUserId(userId); }

  @MutationMapping
  public CartDTO modifyCart(@Argument Long userId, @Argument Long productId, @Argument Integer quantity, @Argument Boolean addToCart) {
    if (quantity == null || quantity == 0) {
    	return cartService.removeCartItem(userId, productId);
    } 
    return cartService.addItemToCart(userId, quantity, productId ,addToCart  );
  }

  // Derived fields
  @SchemaMapping(typeName = "CartItem", field = "subtotal")
  public BigDecimal subtotal(CartItemDTO i) { return i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity()))  ; }

  @SchemaMapping(typeName = "Cart", field = "total")
  public BigDecimal total(CartDTO c) {
	    return c.getItems().stream()
	            .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
	            .reduce(BigDecimal.ZERO, BigDecimal::add);
	}
  @SchemaMapping(typeName = "CartItem", field = "product")
  public ProductDTO product(CartItemDTO i) { return productService.getProductById(i.getProductId()); }
}