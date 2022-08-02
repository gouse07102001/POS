package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemsForm {

    private Integer productId;
    private Integer quantity;
    private Double sellingPrice;

}
