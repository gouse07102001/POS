package com.increff.pos.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderListForm;
import com.increff.pos.model.OrderItemsData;
import com.increff.pos.model.OrderItemsForm;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class OrderController {

    @Autowired
    private OrderDto dto;


    @ApiOperation(value = "Adds an Order")
    @RequestMapping(path = "/api/order", method = RequestMethod.POST)
    public OrderData add(@RequestBody List<OrderItemsForm> form) throws Exception {
        return dto.add(form);
    }

    @ApiOperation(value = "Gets a order by ID")
    @RequestMapping(path = "/api/order/{id}", method = RequestMethod.GET)
    public OrderItemsData get(@PathVariable Integer id) throws Exception {
        return dto.get(id);
    }

    @ApiOperation(value = "Gets a order by OrderID")
    @RequestMapping(path = "/api/order/detail/{id}", method = RequestMethod.GET)
    public List<OrderItemsData> getByOrderId(@PathVariable Integer id) throws Exception {
        return dto.getByOrderId(id);
    }


    @ApiOperation(value = "Updates an Order")
    @RequestMapping(path = "/api/order/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer id, @RequestBody OrderItemsForm f) throws Exception {
        dto.update(id, f);
    }

    @ApiOperation(value = "Gets an Order's Date and Time")
    @RequestMapping(path = "/api/orderDetail/{id}", method = RequestMethod.GET)
    public OrderData getOrder(@PathVariable Integer id) throws Exception {
        return dto.getOrder(id);
    }

    @ApiOperation(value = "Gets every order's Date and Time")
    @RequestMapping(path = "/api/orderDetail", method = RequestMethod.GET)
    public List<OrderData> getAllOrders() {
        return dto.getAllOrders();
    }

    @ApiOperation(value = "Create Invoice")
    @RequestMapping(path = "/api/order/invoice/{id}", method = RequestMethod.GET)
    public void createInvoice(HttpServletResponse response, @PathVariable Integer id)
            throws Exception {
        dto.createInvoice(response, id);
    }
    
    @ApiOperation(value = "Adds a list of order details")
    @RequestMapping(path = "/api/order-details", method = RequestMethod.POST)
    public void addOrderDetails(@RequestBody List<OrderListForm> form) throws Exception {
         dto.addOrderDetails(form);
    }
    
    @ApiOperation(value = "Gets every order's Date and Time")
    @RequestMapping(path = "/api/order-details/list", method = RequestMethod.GET)
    public List<OrderListForm> getAllOrderDetails() {
        return dto.getAllOrderDetails();
    }
    
    @ApiOperation(value = "To delete All OrderList")
    @RequestMapping(path = "/api/order-details/delete", method = RequestMethod.POST)
    public void deleteOrderList() throws Exception {
         dto.deleteOrderList();
    }
    
    

}
