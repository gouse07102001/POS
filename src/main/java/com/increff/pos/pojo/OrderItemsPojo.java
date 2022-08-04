package com.increff.pos.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="OrderItem")
public class OrderItemsPojo {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer orderItemId;
	@Column(nullable = false)
	private Integer orderId;
	@Column(nullable = false)
	private Integer productId;
	@Column(nullable = false)
	private Integer quantity;
	@Column(nullable = false)
	private double sellingPrice;

}
