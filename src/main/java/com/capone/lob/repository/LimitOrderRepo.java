package com.capone.lob.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.capone.lob.model.TradeOrder;

public interface LimitOrderRepo extends CrudRepository<TradeOrder, Long> {
	
	@Override
	@Query("from TradeOrder where orderStatus in( 'NEW', 'PARTIAL_MATCH')")
	public Iterable<TradeOrder> findAll();

}
