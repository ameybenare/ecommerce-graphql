package com.ecommerce_graphql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class EcommerceGraphqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceGraphqlApplication.class, args);
	}

}
