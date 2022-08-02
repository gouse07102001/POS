package com.increff.pos.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import lombok.Getter;
import lombok.Setter;
@Getter
public class SalesReportItem {
    private Integer brandCategory;
    private Long quantity;
	private Double revenue;

    public SalesReportItem(Integer brandCategory, Long quantity, Double revenue) {
        super();
        this.brandCategory = brandCategory;
        this.quantity = quantity;
        BigDecimal bd = new BigDecimal(revenue).setScale(2, RoundingMode.HALF_UP);
        this.revenue = bd.doubleValue();
    }
}
