package com.ecommerce_graphql.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce_graphql.model.Product;


public interface ProductRepository extends JpaRepository<Product, Long> {

}
