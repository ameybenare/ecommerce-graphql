package com.ecommerce_graphql.resolvers;

import java.util.List;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.ecommerce_graphql.DTO.CategoryDTO;
import com.ecommerce_graphql.service.CategoryService;

public class CategoryResolver {

@Controller
public class CategoryGraphQLController {
  private final CategoryService service;
  public CategoryGraphQLController(CategoryService service) { this.service = service; }

  @QueryMapping public List<CategoryDTO> categories() { return service.getAllCategories(); }
}
}
