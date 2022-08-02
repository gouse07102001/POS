package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesDataList {

    private String brand;
    private String category;
    private Long quantity;
	private Double revenue;
}
