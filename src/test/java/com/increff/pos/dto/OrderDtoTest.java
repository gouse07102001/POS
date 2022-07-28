package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderItemsData;
import com.increff.pos.model.OrderItemsForm;
import com.increff.pos.model.OrderListForm;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;


public class OrderDtoTest extends AbstractUnitTest {

    @Autowired
    private OrderDto dto;
    
    @Autowired
    private ProductDto productDto;
    
    @Autowired
    private InventoryDto inventoryDto;

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private BrandService brandService;

    //  Test for adding order
    @Test
    public void testAdd() throws Exception {
    	// set env var
        OrderItemsForm o = getInventoryFormTest();
        List<OrderItemsForm> orders = new ArrayList<OrderItemsForm>();
        orders.add(o);
        OrderData ord = dto.add(orders);
        assertEquals(dto.getByOrderId(ord.getOrderId()).get(0).getQuantity().intValue(), 5);
    }

    //  Test for getting order
    @Test
    public void testGet() throws Exception {
        OrderItemsForm o = getInventoryFormTest();
        List<OrderItemsForm> orders = new ArrayList<OrderItemsForm>();
        orders.add(o);
        OrderData ord = dto.add(orders);
        assertEquals(dto.getByOrderId(ord.getOrderId()).get(0).getQuantity().intValue(), 5);
    }

    //  Test for getting order using order Id
    @Test
    public void testGetByOrderId() throws Exception {
        OrderItemsForm o = getInventoryFormTest();
        List<OrderItemsForm> orders = new ArrayList<OrderItemsForm>();
        orders.add(o);
        OrderData ord = dto.add(orders);
        assertEquals(dto.getByOrderId(ord.getOrderId()).get(0).getQuantity().intValue(), 5);
    }

    //  Test for getting order using order detail Id
    @Test
    public void testGetOrder() throws Exception {
        OrderItemsForm o = getInventoryFormTest();
        List<OrderItemsForm> orders = new ArrayList<OrderItemsForm>();
        orders.add(o);
        OrderData ord = dto.add(orders);
        OrderItemsData orderData = dto.getByOrderId(ord.getOrderId()).get(0);
        OrderItemsData d = dto.get(orderData.getOrderId());
        assertEquals(d.getQuantity().intValue(), 5);
        assertEquals(d.getSellingPrice().intValue(), 5);
    }

    //  Test for order update
    @Test
    public void testUpdate() throws Exception {
        OrderItemsForm o = getInventoryFormTest();
        List<OrderItemsForm> orders = new ArrayList<OrderItemsForm>();
        orders.add(o);
        OrderData ord = dto.add(orders);
        o.setSellingPrice(6.00);
        OrderItemsData orderData = dto.getByOrderId(ord.getOrderId()).get(0);
        OrderItemsData d = dto.get(orderData.getOrderId());
        dto.update(d.getOrderId(), o);
        assertEquals(dto.getByOrderId(ord.getOrderId()).get(0).getSellingPrice().intValue(), 6);
    }

    //  Test for getting all the orders
    @Test
    public void testGetAllOrders() throws Exception {
        OrderItemsForm o = getInventoryFormTest();
        List<OrderItemsForm> orders = new ArrayList<OrderItemsForm>();
        orders.add(o);
        dto.add(orders);
        OrderData p = dto.getAllOrders().get(0);
        OrderItemsData data = dto.getByOrderId(p.getOrderId()).get(0);
        assertEquals(data.getQuantity().intValue(), 5);
    }
    
    @Test
    public void testAddOrderDetails()throws Exception{
    	ProductForm productForm = new ProductForm();
    	productForm.setBarcode("bar123");
    	productForm.setBrandCategory(1);
    	productForm.setproductName("testing");
    	productForm.setMrp(12.00);
    	productDto.add(productForm);
    	ProductData productData = productDto.getCheck("bar123");
    	InventoryForm inventoryForm = new InventoryForm();
    	inventoryForm.setBarcode("bar123");
    	inventoryForm.setProductId(productData.getProductId());
    	inventoryForm.setQuantity(1000);
    	inventoryDto.add(inventoryForm);
    	List<OrderListForm> forms = new ArrayList<OrderListForm>();
    	OrderListForm form = new OrderListForm();
    	form.setBarcode("bar123");
    	form.setProductId(1);
    	form.setProductName("testing");
    	form.setQuantity(10);
    	form.setSellingPrice(10.00);
    	form.setAmount(form.getSellingPrice()*form.getQuantity());
    	forms.add(form);
    	dto.addOrderDetails(forms);
    	assertEquals(dto.getAllOrderDetails().get(0).getBarcode(),"bar123");
    }


    //TODO create helpers
    private OrderItemsForm getInventoryFormTest() throws Exception {
        OrderItemsForm o = new OrderItemsForm();
        InventoryPojo i = new InventoryPojo();
        // create data
        String barcode = "bar123";
        BrandPojo b = new BrandPojo();
        ProductPojo p = new ProductPojo();
        b.setBrand("nestle");
        b.setCategory("dairy");
        brandService.add(b);
        double mrp = 10.25;
        p.setBarcode(barcode);
        p.setBrandCategory(b.getBrandId());
        p.setProductName("ProDuct");
        p.setMrp(mrp);
        productService.add(p);
        int quantity = 10;
        i.setProductId(p.getProductId());
        i.setBarcode(barcode);
        i.setQuantity(quantity);
        inventoryService.add(i);
        o.setProductId(p.getProductId());
        o.setQuantity(5);
        o.setSellingPrice(5.00);
        return o;
    }
    
    private void setup() {
    	
    }
    
    
}
