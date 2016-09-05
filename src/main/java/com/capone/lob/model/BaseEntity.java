package com.capone.lob.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
	private Long Id;
	
	@Column (name="created_time", nullable=false)
	private Long createdTime;
	
	@Column (name="created_by", nullable=false)
	private String createdBy;
	
	@Column (name="updated_time")
	private Long updatedTime;
	
	@Column (name="updated_by")
	private String updatedBy;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public Long getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Long createdTime) {
		this.createdTime = createdTime;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Long updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	
}
