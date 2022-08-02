package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.ProductDao;
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
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;


public class OrderDtoTest extends AbstractUnitTest {

    @Autowired
    private OrderDto dto;
    
    @Autowired
    private ProductDto productDto;
    
    @Autowired
    private InventoryDto inventoryDto;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private InventoryDao inventoryDao;

    @Autowired
    private BrandDao brandDao;

    //  Test for adding order
    @Test
    public void testAdd() throws Exception {
    	// set env var
        OrderItemsForm o = getInventoryFormTest();
        List<OrderItemsForm> orders = new ArrayList<OrderItemsForm>();
        orders.add(o);
        OrderData ord = dto.add(orders);
        assertEquals(dto.getByOrderId(ord.getOrderId()).get(0).getQuantity().intValue(), 5);
        try {
        	orders.get(0).setQuantity(0);
        	ord = dto.add(orders);
        }
        catch(Exception e) {
        	assertEquals("Quantity cannot be empty",e.getMessage());
        }
        try {
        	orders.get(0).setQuantity(10);
        	orders.get(0).setSellingPrice(0.0);
        	ord = dto.add(orders);
        }
        catch(Exception e) {
        	assertEquals("SellingPrice cannot be empty",e.getMessage());
        }
        try {
            orders.get(0).setQuantity(5);
            orders.get(0).setSellingPrice(100000000.0);
            ord = dto.add(orders);
        }
        catch(Exception e) {
            assertEquals("Selling Price should not be greater than MRP",e.getMessage());
        }
        try {
            orders.get(0).setQuantity(10);
            ord = dto.add(orders);
        }
        catch(Exception e) {
            assertEquals("Insufficient inventory",e.getMessage());
        }
        try {
        	orders.get(0).setProductId(0);
        	ord = dto.add(orders);
        }
        catch(Exception e) {
        	assertEquals("ProductId cannot be empty",e.getMessage());
        }
        try {
            orders.get(0).setProductId(10000);
            orders.get(0).setSellingPrice(10.0);
            ord = dto.add(orders);
        }
        catch(Exception e) {
            assertEquals("Product with given ID does not exist",e.getMessage());
        }
        
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
    public void testGetOrderId() throws Exception {
        OrderItemsForm o = getInventoryFormTest();
        List<OrderItemsForm> orders = new ArrayList<OrderItemsForm>();
        orders.add(o);
        OrderData ord = dto.add(orders);
        OrderItemsData orderData = dto.getByOrderId(ord.getOrderId()).get(0);
        OrderItemsData d = dto.get(orderData.getOrderId());
        assertEquals(d.getQuantity().intValue(), 5);
        assertEquals(d.getSellingPrice().intValue(), 5);
        
        try {
        	d = dto.get(1000);
        }
        catch(Exception e) {
        	assertEquals("Order with given ID does not exist",e.getMessage());
        }
    }

    @Test
    public void testGetOrder(){
        try{
            dto.getOrder(1000);
        }
        catch(Exception e){
            assertEquals("Order with the given ID does not exist",e.getMessage());
        }
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
        try {
            d.setProductId(1);
            d.getProductId();
            d.setOrderItemId(1);
            d.getOrderItemId();
        	dto.update(10000, o);

        }
        catch(Exception e) {
        	assertEquals("Order does not found",e.getMessage());
        }
    }

    //  Test for getting all the orders
    @Test
    public void testGetAllOrders() throws Exception {
        OrderItemsForm o = getInventoryFormTest();
        List<OrderItemsForm> orders = new ArrayList<OrderItemsForm>();
        orders.add(o);
        dto.add(orders);
        OrderData p = dto.getAllOrders().get(0);
        p.getTime();
        OrderItemsData data = dto.getByOrderId(p.getOrderId()).get(0);
        assertEquals(data.getQuantity().intValue(), 5);
    }



    @Test
    public void testCreateInvoice() throws Exception {
        HttpServletResponse response = new MockHttpServletResponse();
        dto.createInvoice(response, 1);
    }

    @Test
    public void testDeleteOrderList() throws Exception {
        OrderItemsForm o = getInventoryFormTest();
        List<OrderItemsForm> orders = new ArrayList<OrderItemsForm>();
        orders.add(o);
        OrderData ord = dto.add(orders);
        dto.deleteOrderList();
        List<OrderListForm> forms = dto.getAllOrderDetails();
        assertEquals(forms.size(),0);
    }
    
    @Test
    public void testAddOrderDetails()throws Exception{
    	ProductForm productForm = new ProductForm();
    	productForm.setBarcode("bar123");
    	productForm.setBrandCategory(1);
    	productForm.setProductName("testing");
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
        Integer productId = form.getProductId();
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
        brandDao.insert(b);
        double mrp = 10.25;
        p.setBarcode(barcode);
        p.setBrandCategory(b.getBrandId());
        p.setProductName("ProDuct");
        p.setMrp(mrp);
        productDao.insert(p);
        int quantity = 10;
        i.setProductId(p.getProductId());
        i.setBarcode(barcode);
        i.setQuantity(quantity);
        inventoryDao.insert(i);
        o.setProductId(p.getProductId());
        o.setQuantity(5);
        o.setSellingPrice(5.00);
        return o;
    }
    
}
