package com.increff.pos.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
	
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
	
	public Integer getBrandCategory() {
		return brandCategory;
	}
	public void setBrandCategory(Integer brandCategory) {
		this.brandCategory = brandCategory;
	}
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Double getMrp() {
		return mrp;
	}
	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}
}
