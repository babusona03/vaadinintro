package dev.ui.entity;


import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;


@MappedSuperclass
public class AbstractUser {
	
	@Id
	@GeneratedValue(strategy =GenerationType.SEQUENCE, generator = "EMP_REST_SEQ")
	@SequenceGenerator(name="EMP_REST_SEQ",sequenceName = "EMP_REST_SEQ",allocationSize = 1)
	@Column(name="ID")
	private Integer id;
	
	@Column(name="CREATION_TIME")
	private LocalDateTime createdOn;
	
	@Column(name="MODIFICATION_TIME")
	private LocalDateTime updatedOn;
	
	
	public AbstractUser() {
		super();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public LocalDateTime getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}
	public LocalDateTime getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(LocalDateTime updatedOn) {
		this.updatedOn = updatedOn;
	}
	
	

}
