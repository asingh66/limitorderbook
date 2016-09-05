package com.capone.lob.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.capone.lob.model.TradeOrder;
import com.capone.lob.model.OrderExecutionBatch;
import com.capone.lob.model.OrderExecutionRecord;
import com.capone.lob.model.OrderStateEnum;
import com.capone.lob.model.OrderTypeEnum;

public class OrderBook {

	//Sorted maps to keep bid and ask orders by map
	//bids are kept in descending order of price
	//asks are kept in acending order of price
	Map<Double, List<TradeOrder>> bidOrderMap;
	Map<Double, List<TradeOrder>> askOrderMap;
	
	public Map<Double, List<TradeOrder>> getBidOrderMap() {
		return bidOrderMap;
	}

	//make the TreeMaps thread safe for concurrency
	public OrderBook() {
		bidOrderMap = Collections.synchronizedSortedMap(new TreeMap(Collections.reverseOrder()));
		askOrderMap = Collections.synchronizedSortedMap(new TreeMap());

	}

	//add order to the book
	public void addOrder(TradeOrder order) {
		if (order.getOrderType() == OrderTypeEnum.BID) {
			addOrder(bidOrderMap, order);
			
		} else {
			addOrder(askOrderMap, order);
		}

	}

	//cancel order request, remove the order from orderbook
	public void cancelOrder(TradeOrder order) {
		if (order.getOrderType() == OrderTypeEnum.BID) {
			removeOrder(bidOrderMap, order);
		} else {
			removeOrder(askOrderMap, order);
		}
	}

	//execute the order in the order book
	public List<OrderExecutionRecord> executeOrder(TradeOrder order) {
		if (order.getOrderType() == OrderTypeEnum.ASK) {
			return matchOrders(bidOrderMap, order);
		} else {
			return matchOrders(askOrderMap, order);

		}
	}

	//method for matching bids and asks
	private List<OrderExecutionRecord> matchOrders(Map<Double, List<TradeOrder>> m, TradeOrder order) {
		int quantity = order.getQuantity();
		boolean foundMatch = false;
		List<OrderExecutionRecord> matchedOrders = new ArrayList<OrderExecutionRecord>();
		OrderExecutionBatch executionBatch = new OrderExecutionBatch();
		executionBatch.setCreatedBy(Constants.CREATED_BY);
		executionBatch.setCreatedTime(Utility.converDateToLong(new Date()));
		for (Map.Entry<Double, List<TradeOrder>> entry : m.entrySet()) {
			Double price = entry.getKey();
			List<TradeOrder> orderList = entry.getValue();
			if (matchPrice(price, order) && quantity > 0) {
				List<TradeOrder> removelist = new ArrayList();
				for (TradeOrder bid : orderList) {
					quantity = quantity - (bid.getQuantity() - bid.getExecutedQuantity());
					foundMatch = true;
					if (quantity == 0) { // perfect match
						OrderExecutionRecord executionRecord = createExecutionRecord(order, bid, order.getPrice(), executionBatch);
						bid.setExecutedQuantity(bid.getQuantity());
						bid.setOrderStatus(OrderStateEnum.EXECUTED);
						order.setExecutedQuantity(order.getQuantity());
						order.setOrderStatus(OrderStateEnum.EXECUTED);
						matchedOrders.add(executionRecord);
						removelist.add(bid);
						break;
					} else if (quantity < 0) {
						OrderExecutionRecord executionRecord = createExecutionRecord(order, bid, order.getPrice(),executionBatch);
						bid.setExecutedQuantity(bid.getQuantity() + quantity);
						bid.setOrderStatus(OrderStateEnum.PARTIAL_MATCH);
						order.setExecutedQuantity(order.getQuantity());
						order.setOrderStatus(OrderStateEnum.EXECUTED);
						matchedOrders.add(executionRecord);						removedMatched(order);
						break; // order full filled no need to search more
					} else { // more quantities left
						OrderExecutionRecord executionRecord = createExecutionRecord(order, bid, order.getPrice(),executionBatch);
						bid.setExecutedQuantity(bid.getQuantity());
						order.setExecutedQuantity(order.getQuantity() - quantity);
						bid.setOrderStatus(OrderStateEnum.EXECUTED);
						matchedOrders.add(executionRecord);						removelist.add(bid);
					}

				}
				// remove matched orders from the order book
				orderList.removeAll(removelist);

			} else {
				return matchedOrders;
			}
		}
		if (quantity > 0 && foundMatch) {
			order.setExecutedQuantity(order.getQuantity() - quantity);
			order.setOrderStatus(OrderStateEnum.PARTIAL_MATCH);
		}
		return matchedOrders;

	}
	
	//keep record of bid, ask match pair, create entry in Order_Execution_Record
	private OrderExecutionRecord createExecutionRecord(TradeOrder order1, TradeOrder order2, Double price, OrderExecutionBatch batch){
		OrderExecutionRecord executionRecord = new OrderExecutionRecord();
		if (order1.getOrderType() == OrderTypeEnum.BID) {
			executionRecord.setBidOrder(order1);
			executionRecord.setAskOrder(order2);
			executionRecord.setExecutionPrice(price);
		} else {
			executionRecord.setBidOrder(order2);
			executionRecord.setAskOrder(order1);
			executionRecord.setExecutionPrice(price);
		}
		executionRecord.setCreatedBy(Constants.CREATED_BY);
		executionRecord.setCreatedTime(Utility.converDateToLong(new Date()));
		int qtyAvailable1 = order1.getQuantity() - order1.getExecutedQuantity();
		int qtyAvailable2 = order2.getQuantity() - order2.getExecutedQuantity();
		executionRecord.setQuantity(Math.min(qtyAvailable1, qtyAvailable2));
		executionRecord.setExecutionBatch(batch);
		return executionRecord;
	}

	//remote the matched order from the order book
	private void removedMatched(TradeOrder order) {
		Map<Double, List<TradeOrder>> orderMap;
		if (order.getOrderType() == OrderTypeEnum.BID) {
			orderMap = bidOrderMap;
		} else {
			orderMap = askOrderMap;
		}

		List<TradeOrder> orderList = orderMap.get(order.getPrice());
		if (orderList != null) {
			orderList.remove(order);
		}

	}

	//check if the price matches for the order
	private boolean matchPrice(Double price, TradeOrder o2) {
		if (o2.getOrderType() == OrderTypeEnum.BID) {
			if (price <= o2.getPrice())
				return true;
			return false;
		} else {
			if (price >= o2.getPrice())
				return true;
			return false;
		}
	}

	//add order in the order book
	private void addOrder(Map<Double, List<TradeOrder>> m, TradeOrder order) {
		List<TradeOrder> orderList = m.get(order.getPrice());
		if (orderList == null) {
			orderList = new LinkedList<TradeOrder>();
			m.put(order.getPrice(), orderList);
		}
		orderList.add(order);
	}

	//remove order for the order book
	private void removeOrder(Map<Double, List<TradeOrder>> m, TradeOrder order) {
		List<TradeOrder> orderList = m.get(order.getPrice());
		if (orderList != null) {
			orderList.remove(order);
		}
	}

}
