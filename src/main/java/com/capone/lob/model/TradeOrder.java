package com.capone.lob.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class TradeOrder extends BaseEntity {

	
	private String symbol;
	
	private Integer quantity;
	
	private Integer executedQuantity;
	
	
	private Double price;
	
	@Enumerated(EnumType.STRING)
	private OrderStateEnum orderStatus;
	
	@Enumerated(EnumType.STRING)
	private OrderTypeEnum orderType;
	
	@Enumerated(EnumType.STRING)
	private OrderCategoryEnum orderCategory;
	
	
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	public OrderStateEnum getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStateEnum orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	public OrderTypeEnum getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderTypeEnum orderType) {
		this.orderType = orderType;
	}
	
	public Integer getExecutedQuantity() {
		return executedQuantity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((orderType == null) ? 0 : orderType.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + ((quantity == null) ? 0 : quantity.hashCode());
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		result = prime * result + ((getCreatedTime() == null) ? 0 : getCreatedTime().hashCode());

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TradeOrder other = (TradeOrder) obj;
		if (orderType != other.orderType)
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (quantity == null) {
			if (other.quantity != null)
				return false;
		} else if (!quantity.equals(other.quantity))
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		if (getCreatedTime() == null) {
			if (other.getCreatedTime() != null)
				return false;
		} else if (!getCreatedTime().equals(other.getCreatedTime()))
			return false;
		return true;
	}

	public void setExecutedQuantity(Integer executedQuantity) {
		this.executedQuantity = executedQuantity;
	}

	public OrderCategoryEnum getOrderCategory() {
		return orderCategory;
	}

	public void setOrderCategory(OrderCategoryEnum orderCategory) {
		this.orderCategory = orderCategory;
	}

	


}
