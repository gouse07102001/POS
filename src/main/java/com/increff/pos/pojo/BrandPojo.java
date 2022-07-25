
package com.increff.pos.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="Brand",uniqueConstraints = 
{ //other constraints
@UniqueConstraint(name = "brand_category", columnNames = { "brand", "category" })})
public class BrandPojo {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer brandId;

	
	public Integer getBrandId() {
		return brandId;
	}
	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}
	@Column(nullable = false)
	private String brand;
	@Column(nullable = false)
	private String category;
	
	
	
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
	
}



