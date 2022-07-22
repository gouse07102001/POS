package com.increff.pos.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SalesReportItem {
    private Integer brandCategory;
    private Long quantity;
    public Integer getBrandCategory() {
		return brandCategory;
	}

	public void setBrandCategory(Integer brandCategory) {
		this.brandCategory = brandCategory;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Double getRevenue() {
		return revenue;
	}

	public void setRevenue(Double revenue) {
		this.revenue = revenue;
	}

	private Double revenue;

    public SalesReportItem(Integer brandCategory, Long quantity, Double revenue) {
        super();
        this.brandCategory = brandCategory;
        this.quantity = quantity;
        BigDecimal bd = new BigDecimal(revenue).setScale(2, RoundingMode.HALF_UP);
        this.revenue = bd.doubleValue();
    }
}
