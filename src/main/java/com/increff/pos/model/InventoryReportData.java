package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryReportData {

	private String productName;
	private String barcode;
    private String brand;
    private String category;
    private Integer quantity;
   

}
