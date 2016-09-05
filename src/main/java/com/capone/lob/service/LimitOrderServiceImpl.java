package com.capone.lob.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capone.lob.model.TradeOrder;
import com.capone.lob.model.ExecutionResult;
import com.capone.lob.model.OrderCategoryEnum;
import com.capone.lob.model.OrderExecutionRecord;
import com.capone.lob.model.OrderStateEnum;
import com.capone.lob.repository.LimitOrderRepo;
import com.capone.lob.repository.OrderExecutionRecordRepo;

@Service
@Transactional
public class LimitOrderServiceImpl implements LimitOrderService {
	
	private static Map<String, OrderBook> orderBooks;
	
	@Autowired
	private LimitOrderRepo limitOrderRepo;
	
	@Autowired
	private OrderValidationService orderValidationService;
	
	@Autowired
	private OrderExecutionRecordRepo orderExecutionRecordRepo;
	
	//if enabled, order is automatically matched when created/entered in the system
	@Value("${capone.order.matchoncreate}")
	private boolean matchOnCreate;
	
	public LimitOrderServiceImpl () {
		orderBooks = new HashMap();				
	}
	
	@Override
	@Transactional
	public void createOrder(TradeOrder o) throws InvalidOrderException {
		//validate the order
		orderValidationService.validate(o);
		o.setExecutedQuantity(0);
		o.setOrderStatus(OrderStateEnum.NEW);
		o.setCreatedBy(Constants.CREATED_BY);
		o.setCreatedTime(Utility.converDateToLong(new Date()));
		//validation passed, continue with processing
		limitOrderRepo.save(o);
		OrderBook orderBook = orderBooks.get(o.getSymbol());
		if (orderBook == null) {
			orderBook = new OrderBook();
			orderBooks.put(o.getSymbol(), orderBook);
		}
		if (o.getOrderCategory() == OrderCategoryEnum.LIMIT) {
			orderBook.addOrder(o);
		} else {
			o.setOrderStatus(OrderStateEnum.NOT_MATCHED);
		}
		if (matchOnCreate)
			executeOrder(orderBook,o);		
	}

	@Override
	@Transactional(readOnly=true)
	public Iterable<TradeOrder> findOrders()  {
		// TODO Auto-generated method stub
		return limitOrderRepo.findAll();
	}

	@Override
	@Transactional
	public void cancelOrder(TradeOrder o) {
		o.setUpdatedTime(22222L);
		o.setOrderStatus(OrderStateEnum.CANCELLED);
		limitOrderRepo.save(o);
		OrderBook orderBook = orderBooks.get(o.getSymbol());
		orderBook.cancelOrder(o);
	}

	@Override
	public TradeOrder findOrder(Long id) throws InvalidOrderException {
		if (id == null || id < 0) {
			throw new InvalidOrderException("Order Id " + id + " is invalid");
		}
		return limitOrderRepo.findOne(id);
	}

	@Override
	@Transactional
	public List<ExecutionResult> executeOrders() {
		boolean done = false;
		List <ExecutionResult> result = new ArrayList();
		//go over all the order books and execute
		for (Map.Entry<String, OrderBook> entry : orderBooks.entrySet()) {
			result.add(executeOrders(entry.getKey()));
		}
		return result;
	}

	private List<OrderExecutionRecord> executeOrder(OrderBook orderBook, TradeOrder o) {
		List<OrderExecutionRecord> matchedOrders = orderBook.executeOrder(o);
		if (o.getOrderCategory() == OrderCategoryEnum.MARKET) {
			if (matchedOrders.size() == 0) { //no matches
				o.setOrderStatus(OrderStateEnum.NOT_MATCHED);
			}
			limitOrderRepo.save(o);
		}
		for (OrderExecutionRecord order : matchedOrders) {
			orderExecutionRecordRepo.save(order);
			limitOrderRepo.save(order.getAskOrder());
			limitOrderRepo.save(order.getBidOrder());
		}
		return matchedOrders;
	}

	@Override
	@Transactional
	public ExecutionResult executeOrders(String symbol) {

		boolean done = false;
		ExecutionResult executionResult = new ExecutionResult();
		executionResult.setSymbol(symbol);
		executionResult.setTotalTrades(0);
		if (orderBooks.get(symbol) == null) {
			return executionResult;
		}
		//pick the bid orders and start executing
		Map<Double, List<TradeOrder>> bidsMap = orderBooks.get(symbol).getBidOrderMap();

		for (Map.Entry<Double, List<TradeOrder>> bidEntry : bidsMap.entrySet()) {
			for (TradeOrder order : bidEntry.getValue()) {
				List<OrderExecutionRecord> executedOrders = executeOrder(orderBooks.get(symbol), order);
				executionResult.setTotalTrades(executionResult.getTotalTrades() + executedOrders.size() );
				
				if (executedOrders.size() == 0) {
					done = true;
					break;
				}
			}
			if (done) {
				break;
			}
		}
		
		return executionResult;
	
	}

	
}
