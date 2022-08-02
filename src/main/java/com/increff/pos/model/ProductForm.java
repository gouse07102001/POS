package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductForm {

	private String barcode;
	private String productName;
	private Double mrp;
	private Integer brandCategory;	
	
}
