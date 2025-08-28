package com.ecommerce_graphql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecommerce_graphql.model.Review;
import com.ecommerce_graphql.model.User;


public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductId(Long productId);
    Review findByProductIdAndUser(Long productId, User user);
    
    @Query("select avg(r.rating) from Review r where r.product.id = :productId")
    Double averageRatingByProductId(@Param("productId") Long productId);

    Long countByProductId(Long productId);
}