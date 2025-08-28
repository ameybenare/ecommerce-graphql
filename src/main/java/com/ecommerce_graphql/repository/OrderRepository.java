package com.ecommerce_graphql.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce_graphql.model.Order;


public interface OrderRepository extends JpaRepository<Order, Long>{
	 List<Order> findByUserId(Long userId);
}
