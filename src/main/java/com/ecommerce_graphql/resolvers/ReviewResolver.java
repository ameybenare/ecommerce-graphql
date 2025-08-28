package com.ecommerce_graphql.resolvers;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.ecommerce_graphql.DTO.ProductDTO;
import com.ecommerce_graphql.DTO.ReviewDTO;
import com.ecommerce_graphql.DTO.UserDTO;
import com.ecommerce_graphql.service.ReviewService;
import com.ecommerce_graphql.service.UserService;

@Controller
public class ReviewResolver {
  private final ReviewService reviewService;
  private final UserService userService;

  public ReviewResolver(ReviewService reviewService, UserService userService) {
    this.reviewService = reviewService; this.userService = userService;
  }

  // Product.reviews
  @SchemaMapping(typeName = "Product", field = "reviews")
  public List<ReviewDTO> reviews(ProductDTO product) { return reviewService.getReviewsByProduct(product.getId()); }

  // Review.user
  @SchemaMapping(typeName = "Review", field = "user")
  public UserDTO user(ReviewDTO review) { return userService.getUserById(review.getUserId()); }

  @MutationMapping
  public ReviewDTO addReview(@Argument Long userId, @Argument Long productId,
                             @Argument Integer rating, @Argument String comment) {
    ReviewDTO dto = new ReviewDTO();
    dto.setUserId(userId); dto.setProductId(productId);
    dto.setRating(rating); dto.setComment(comment);
    return reviewService.addReview(dto);
  }
}