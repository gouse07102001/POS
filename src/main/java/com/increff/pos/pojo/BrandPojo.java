
package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
@Getter
@Setter

@Entity
@Table(name="Brand",uniqueConstraints = 
{ //other constraints
@UniqueConstraint(name = "brand_category", columnNames = { "brand", "category" })})

public class BrandPojo {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer brandId;
	@Column(nullable = false)
	private String brand;
	@Column(nullable = false)
	private String category;
}



