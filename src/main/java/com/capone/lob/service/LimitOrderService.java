package com.capone.lob.service;

import java.util.List;
import java.util.Map;

import com.capone.lob.model.ExecutionResult;
import com.capone.lob.model.TradeOrder;

public interface LimitOrderService {
	void createOrder(TradeOrder o) throws InvalidOrderException;
	void cancelOrder(TradeOrder o);
	List<ExecutionResult> executeOrders();
	ExecutionResult executeOrders(String symbol);
	TradeOrder findOrder(Long id) throws InvalidOrderException;
	Iterable<TradeOrder> findOrders();
}
