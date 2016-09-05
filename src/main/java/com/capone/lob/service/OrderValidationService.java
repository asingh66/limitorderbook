package com.capone.lob.service;

import com.capone.lob.model.TradeOrder;

public interface OrderValidationService {

	void validate(TradeOrder o) throws InvalidOrderException;
}
