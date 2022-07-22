package com.increff.pos.model;

public class SalesReportData {
    private String brand;
    private String category;
    private Integer id;
    private Long quantity;
    private Double revenue;

    public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public SalesReportData(Integer id, String brand, String category) {
        super();
        this.brand = brand;
        this.category = category;
        this.id = id;
    }
}
