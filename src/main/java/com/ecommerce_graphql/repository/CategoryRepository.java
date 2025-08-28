package com.ecommerce_graphql.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce_graphql.model.Category;


public interface CategoryRepository extends JpaRepository<Category,Long>{

}
