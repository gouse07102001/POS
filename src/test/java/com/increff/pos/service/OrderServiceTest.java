package com.increff.pos.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.OrderItemsForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemsPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;

public class OrderServiceTest extends AbstractUnitTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private BrandService brandService;

    //  Test for checking order
    @Test
    public void testGetCheckOrder() throws Exception {
        OrderItemsForm o = getInventoryFormTest();
        List<OrderItemsForm> orders = new ArrayList<OrderItemsForm>();
        orders.add(o);
        OrderPojo op = OrderDto.addTime();
        orderService.addOrder(op);
        ;
        assertEquals(orderService.getCheckOrder(op.getOrderId()).getOrderId(), op.getOrderId());
        try{
          orderService.getCheckOrder(100);
        }
        catch(Exception e){
            assertEquals("Order with the given ID does not exist",e.getMessage());
        }
    }

    //  Test for adding order
    @Test
    public void testAdd() throws Exception {
        OrderItemsForm o = getInventoryFormTest();
        List<OrderItemsForm> orders = new ArrayList<OrderItemsForm>();
        orders.add(o);
        OrderPojo op = OrderDto.addTime();
        orderService.addOrder(op);
        OrderItemsPojo p = 	OrderDto.convert(orders.get(0));
        p.setOrderId(op.getOrderId());
        orderService.add(p);
        assertEquals(orderService.getByOrderId(op.getOrderId()).get(0).getQuantity(), p.getQuantity());
    }

    //  Test for getting order by id
    @Test
    public void testGetByOrderId() throws Exception {
        OrderItemsForm o = getInventoryFormTest();
        List<OrderItemsForm> orders = new ArrayList<OrderItemsForm>();
        orders.add(o);
        OrderPojo op = OrderDto.addTime();
        orderService.addOrder(op);
        OrderItemsPojo p = OrderDto.convert(orders.get(0));
        p.setOrderId(op.getOrderId());
        orderService.add(p);
        assertEquals(orderService.getByOrderId(op.getOrderId()).get(0).getQuantity(), p.getQuantity());
    }

    //  Test for getting order by order id
    @Test
    public void testGetOrderItemsByOrderId() throws Exception {
        OrderItemsForm o = getInventoryFormTest();
        List<OrderItemsForm> orders = new ArrayList<OrderItemsForm>();
        orders.add(o);
        OrderPojo op = OrderDto.addTime();
        orderService.addOrder(op);
        OrderItemsPojo p = OrderDto.convert(orders.get(0));
        p.setOrderId(op.getOrderId());
        orderService.add(p);
        assertEquals(orderService.getOrderItemsUsingOrderId(op.getOrderId()).get(0).getQuantity(), p.getQuantity());
    }


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

}
