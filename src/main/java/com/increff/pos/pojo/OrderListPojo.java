package com.increff.pos.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
public class OrderListPojo {
		@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		private Integer orderListId;
		public Integer getOrderListId() {
			return orderListId;
		}
		public void setOrderListId(Integer orderListId) {
			this.orderListId = orderListId;
		}
		@Column(nullable = false)
		private String productName;
		@Column(nullable = false)
		private String barcode;
		@Column(nullable = false)
		private Integer quantity;
		@Column(nullable = false)
		private Integer productId;
		public Integer getProductId() {
			return productId;
		}
		public void setProductId(Integer productId) {
			this.productId = productId;
		}
		private Double sellingPrice;
		private Double amount;
		
		public String getProductName() {
			return productName;
		}
		public void setProductName(String productName) {
			this.productName = productName;
		}
		public String getBarcode() {
			return barcode;
		}
		public void setBarcode(String barcode) {
			this.barcode = barcode;
		}
		public Integer getQuantity() {
			return quantity;
		}
		public void setQuantity(Integer quantity) {
			this.quantity = quantity;
		}
		public Double getSellingPrice() {
			return sellingPrice;
		}
		public void setSellingPrice(Double sellingPrice) {
			this.sellingPrice = sellingPrice;
		}
		public Double getAmount() {
			return amount;
		}
		public void setAmount(Double amount) {
			this.amount = amount;
		}
		
		
}
