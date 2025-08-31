package com.ecommerce_graphql.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

public class OrderDTO {
    private Long id;
    //private LocalDateTime orderDate;
    private OffsetDateTime orderDate;

    private BigDecimal total;
    private Long userId;
    private List<OrderItemDTO> items;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	/*
	 * public LocalDateTime getOrderDate() { return orderDate; } public void
	 * setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
	 */
	
	
	public BigDecimal getTotal() {
		return total;
	}
	public OffsetDateTime getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(OffsetDateTime orderDate) {
		this.orderDate = orderDate;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public List<OrderItemDTO> getItems() {
		return items;
	}
	public void setItems(List<OrderItemDTO> items) {
		this.items = items;
	}
    
    
}
