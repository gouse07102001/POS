package com.increff.pos.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
public class OrderListPojo {
		@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		private Integer orderListId;
		
		@Column(nullable = false)
		private String productName;
		@Column(nullable = false)
		private String barcode;
		@Column(nullable = false)
		private Integer quantity;
		@Column(nullable = false)
		private Integer productId;
		private Double sellingPrice;
		private Double amount;
		
		
		
}
