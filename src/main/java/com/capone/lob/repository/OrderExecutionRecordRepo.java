package com.capone.lob.repository;

import org.springframework.data.repository.CrudRepository;
import com.capone.lob.model.OrderExecutionRecord;

public interface OrderExecutionRecordRepo extends CrudRepository<OrderExecutionRecord, Long>  {

}
