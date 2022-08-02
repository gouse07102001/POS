package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesReportData {
    private String brand;
    private String category;
    private Integer id;
    private Long quantity;
    private Double revenue;
	public SalesReportData(Integer id, String brand, String category) {
        super();
        this.brand = brand;
        this.category = category;
        this.id = id;
    }
}
