package com.ecommerce_graphql.resolvers;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.ecommerce_graphql.DTO.ProductDTO;
import com.ecommerce_graphql.repository.ReviewRepository;
import com.ecommerce_graphql.service.ProductService;

@Controller
public class ProductResolver {
	private final ProductService productService;
	private final ReviewRepository reviewRepo;

	public ProductResolver(ProductService productService, ReviewRepository reviewRepo) {
		this.productService = productService; this.reviewRepo = reviewRepo;
	}

	@QueryMapping public List<ProductDTO> products() { return productService.getAllProducts(); }

	@QueryMapping 
	public ProductDTO productById(@Argument Long id) { 
		return productService.getProductById(id); 
	}

	@SchemaMapping(typeName = "Product", field = "avgRating")
	public Double avgRating(ProductDTO product) { return reviewRepo.averageRatingByProductId(product.getId()); }

	@SchemaMapping(typeName = "Product", field = "reviewCount")
	public Long reviewCount(ProductDTO product) { return reviewRepo.countByProductId(product.getId()); }
}

