package com.increff.pos.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="Product")
public class ProductPojo {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer productId;
	@Column(unique=true,nullable = false)
	private String barcode;
	@Column(nullable = false)
	private Integer brandCategory;
	@Column(nullable = false)
	private String productName;
	@Column(nullable = false)
	private Double mrp;
	
}
