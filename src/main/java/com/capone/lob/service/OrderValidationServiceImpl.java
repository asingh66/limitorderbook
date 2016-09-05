package com.capone.lob.service;

import org.springframework.stereotype.Service;

import com.capone.lob.model.OrderCategoryEnum;
import com.capone.lob.model.OrderTypeEnum;
import com.capone.lob.model.TradeOrder;

@Service
public class OrderValidationServiceImpl implements OrderValidationService {

	public static final Double PRICE_LIMIT = 100000D;
	public static final Integer QUANTITY_LIMIT = 100000;

	@Override
	public void validate(TradeOrder o) throws InvalidOrderException {
		//convert symbols to upper case and trim
	
		
		if (o.getSymbol() == null || o.getSymbol().trim().length() == 0) {
			throw new InvalidOrderException("Order symbol is required");
		}
		o.setSymbol(o.getSymbol().trim().toUpperCase());
		
		
		
		// check the price
		if (o.getPrice() == null || o.getPrice() <= 0 || o.getPrice() > PRICE_LIMIT) {
			throw new InvalidOrderException("Order price provided is : " + o.getPrice()
					+ " Order price should be between 0 and " + PRICE_LIMIT);
		} else {
			//round the price
			o.setPrice(Utility.roundPrice(o.getPrice()));
		}

		
		if (o.getQuantity() == null || o.getQuantity() <= 0 || o.getQuantity() > QUANTITY_LIMIT) {
			throw new InvalidOrderException("Order quantity provided is : " + o.getQuantity()
					+ " Order price should be between 0 and " + QUANTITY_LIMIT);
		}
		
		
		if ((o.getOrderType() == null) || ((o.getOrderType() != OrderTypeEnum.BID) && (o.getOrderType() != OrderTypeEnum.ASK) )) {
			throw new InvalidOrderException("Order type provided is : " + o.getOrderType()
			+ " valid values are (" + OrderTypeEnum.BID + "," + OrderTypeEnum.ASK + ")");
		}
		
		if ((o.getOrderCategory() == null) || ((o.getOrderCategory() != OrderCategoryEnum.LIMIT) && (o.getOrderCategory() != OrderCategoryEnum.MARKET) )) {
			throw new InvalidOrderException("Order category provided is : " + o.getOrderCategory()
			+ " valid values are (" + OrderCategoryEnum.LIMIT + "," + OrderCategoryEnum.MARKET + ")");
		}

	}
	
	

}
