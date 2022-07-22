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

    public static BrandForm getBrandForm() {
        BrandForm b = new BrandForm();
        String brand = "nestle";
        String category = "dairy";
        b.setBrand(brand);
        b.setCategory(category);
        return b;
    }

    public static BrandPojo getBrandPojo() throws ApiException {
        BrandPojo b = new BrandPojo();
        String brand = "nestle";
        String category = "dairy";
        b.setBrand(brand);
        b.setCategory(category);
        return b;
    }

    public static BrandPojo getBrandPojo(String brand, String category) throws ApiException {
        BrandPojo b = new BrandPojo();
        b.setBrand(brand);
        b.setCategory(category);
        return b;
    }

    public static BrandForm getBrandFormDto(String brand, String category) {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand(brand);
        brandForm.setCategory(category);
        return brandForm;
    }

    public static ProductData getProductData() {
        ProductData productData = new ProductData();
        String barcode = "abc123";
        int brandCategory = 1;
        int id = 1;
        double mrp = 10.50;
        String name = "munch";
        productData.setBarcode(barcode);
        productData.setBrandCategory(brandCategory);
        productData.setProductId(id);
        productData.setMrp(mrp);
        productData.setproductName(name);
        return productData;
    }

    public static ProductForm getProductForm() throws ApiException {
        ProductForm f = new ProductForm();
        BrandPojo b = getBrandPojo();
        Integer brandCategory = b.getBrandId();
        String name = "munch";
        double mrp = 10.50;
        f.setBrandCategory(brandCategory);
        f.setMrp(mrp);
        f.setproductName(name);
        return f;
    }

    public static ProductPojo getProductPojo() throws ApiException {
        ProductPojo p = new ProductPojo();
        String barcode = "abc123";
        String name = "munch";
        double mrp = 10;
        BrandPojo brandPojo = getBrandPojo();
        p.setBarcode(barcode);
        p.setBrandCategory(brandPojo.getBrandId());
        p.setProductName(name);
        p.setMrp(mrp);
        return p;
    }

    public static ProductForm getProductFormDto(Integer brandCategory, String barcode, String name, double mrp) {
        ProductForm productForm = new ProductForm();
        productForm.setBrandCategory(brandCategory);
        productForm.setBarcode(barcode);
        productForm.setproductName(name);
        productForm.setMrp(mrp);
        return productForm;
    }

    public static InventoryForm getInventoryForm() {
        InventoryForm f = new InventoryForm();
        int id = 1;
        int quantity = 35;
        f.setProductId(id);
        f.setQuantity(quantity);
        return f;
    }

    public static InventoryPojo getInventoryPojo() throws ApiException {
        InventoryPojo p = new InventoryPojo();
        int quantity = 25;
        p.setProductId(getProductPojo().getProductId());
        p.setQuantity(quantity);
        return p;
    }

    public static InventoryForm getInventoryFormDto(int id, int quantity) {
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setProductId(id);
        inventoryForm.setQuantity(quantity);
        return inventoryForm;
    }

    public static List<OrderItemsForm> getOrderItemFormArrayDto(int productId1, int productId2, int quantity1, int quantity2, double mrp1, double mrp2) {
        List<OrderItemsForm> orderItemForms = new ArrayList<OrderItemsForm>();
        OrderItemsForm form1 = new OrderItemsForm();
        form1.setProductId(productId1);
        form1.setQuantity(quantity1);
        form1.setSellingPrice(mrp1);
        orderItemForms.add(form1);
        OrderItemsForm form2 = new OrderItemsForm();
        form2.setProductId(productId2);
        form2.setQuantity(quantity2);
        form2.setSellingPrice(mrp2);
        orderItemForms.add(form2);
        return orderItemForms;
    }

}
