package com.capone.lob.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class OrderExecutionBatch extends BaseEntity{
	
	 @OneToMany(cascade = CascadeType.ALL, mappedBy = "executionBatch", fetch = FetchType.LAZY)
	 private Set<OrderExecutionRecord> executionRecords = new HashSet();

	public Set<OrderExecutionRecord> getExecutionRecords() {
		return executionRecords;
	}

	public void setExecutionRecords(Set<OrderExecutionRecord> executionRecords) {
		this.executionRecords = executionRecords;
	}
	

}
