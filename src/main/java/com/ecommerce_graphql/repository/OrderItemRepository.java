package com.ecommerce_graphql.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce_graphql.model.OrderItem;


public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
