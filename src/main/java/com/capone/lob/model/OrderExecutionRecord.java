package com.capone.lob.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;


@Entity
public class OrderExecutionRecord extends BaseEntity {
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bid_id", nullable = false)
	TradeOrder bidOrder;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ask_ID", nullable = false)
	TradeOrder askOrder;
	
	Double executionPrice;

	Integer quantity;
	
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public TradeOrder getBidOrder() {
		return bidOrder;
	}

	public void setBidOrder(TradeOrder bidOrder) {
		this.bidOrder = bidOrder;
	}

	public TradeOrder getAskOrder() {
		return askOrder;
	}

	public void setAskOrder(TradeOrder askOrder) {
		this.askOrder = askOrder;
	}

	public Double getExecutionPrice() {
		return executionPrice;
	}

	public void setExecutionPrice(Double executionPrice) {
		this.executionPrice = executionPrice;
	}

	

	
	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "execution_id")
	OrderExecutionBatch executionBatch;
	
	public void setExecutionBatch(OrderExecutionBatch executionBatch) {
		this.executionBatch = executionBatch;
	}

	public OrderExecutionBatch getExecutionBatch() {
		return executionBatch;
	} 

	
}
