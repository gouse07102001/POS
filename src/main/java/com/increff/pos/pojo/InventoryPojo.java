package com.increff.pos.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name="Inventory", uniqueConstraints = @UniqueConstraint(columnNames = "productId"))
public class InventoryPojo {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer inventoryId;
    @Column(nullable = false)
    private Integer productId;
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false)
    private String barcode;	
}
