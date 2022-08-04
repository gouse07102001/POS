package com.increff.pos.util;

import java.util.ArrayList;
import java.util.List;

import com.increff.pos.model.BrandForm;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.OrderItemsForm;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;

public class TestDataUtil {


    public static BrandForm getBrandFormDto(String brand, String category) {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand(brand);
        brandForm.setCategory(category);
        return brandForm;
    }






    public static ProductForm getProductFormDto(Integer brandCategory, String barcode, String name, double mrp) {
        ProductForm productForm = new ProductForm();
        productForm.setBrandCategory(brandCategory);
        productForm.setBarcode(barcode);
        productForm.setProductName(name);
        productForm.setMrp(mrp);
        return productForm;
    }







}
