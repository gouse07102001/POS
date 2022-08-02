package com.increff.pos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.dao.OrderDetailDao;
import com.increff.pos.model.OrderListForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderListPojo;
import com.increff.pos.pojo.OrderItemsPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;

@Service
public class OrderService {

	@Autowired
	private OrderDetailDao orderDao;
	@Autowired
	private OrderDao dao;

	@Autowired
	private ProductService productService;

	@Autowired
	InventoryService inventoryService;

	@Transactional
	public void add(OrderItemsPojo p) {
		dao.insert(p);
	}

	@Transactional(rollbackFor = ApiException.class)
	public OrderItemsPojo get(Integer id) throws Exception {
		return getCheck(id);
	}

	@Transactional(rollbackFor = ApiException.class)
	public List<OrderItemsPojo> getByOrderId(Integer id) throws Exception {
		return dao.selectByOrdersId(id);
	}

	@Transactional
    public OrderItemsPojo getCheck(Integer id) throws Exception {
    	OrderItemsPojo p = null;
    	try {
    		p = dao.select(id);
    	}
    	catch(Exception e) {
    		 if (p == null) {
    	            throw new ApiException("Order with given ID does not exist");
    	        }
    	}
        return p;
    }

	@Transactional
	public List<OrderPojo> getAllOrders() {
		return orderDao.selectAll();
	}

	@Transactional
	public List<OrderListPojo> getAllOrderDetails() {
		// TODO Auto-generated method stub
		return orderDao.selectAllOrderDetails();
	}

	@Transactional
	public OrderPojo getCheckOrder(Integer id) throws Exception {
		OrderPojo p = orderDao.select(id);
		if (p == null) {
			throw new ApiException("Order with the given ID does not exist");
		}
		return p;
	}

	@Transactional(rollbackFor = ApiException.class)
	public void addOrder(OrderPojo p) {
		orderDao.add(p);
	}

	@Transactional(rollbackFor = ApiException.class)
	public void update(List<OrderItemsPojo> orderItems) throws Exception {
		for (OrderItemsPojo var : orderItems) {
			InventoryPojo inventoryPojo = inventoryService.getInventoryByProductID(var.getProductId());
			inventoryService.update(inventoryPojo.getInventoryId(), inventoryPojo.getQuantity() - var.getQuantity());
		}
	}

	@Transactional
	public List<OrderItemsPojo> getOrderItemsUsingOrderId(Integer id) throws Exception {
		return dao.selectByOrdersId(id);
	}

	@Transactional(rollbackFor = ApiException.class)
	public void update(Integer id, OrderItemsPojo p) throws Exception {
		OrderItemsPojo ex=null;
		try {
			ex = getCheck(id);
			ex.setProductId(p.getProductId());
			ex.setSellingPrice(p.getSellingPrice());
			ex.setQuantity(p.getQuantity());
			dao.update(p);
		}
		catch(Exception e) {
			if(ex==null)
				throw new ApiException("Order does not found");
		}
	}

	@Transactional
	public void addOrderDetails(List<OrderListPojo> pojos) throws Exception {
		deleteOrderList();
		for (OrderListPojo pojo : pojos) {
			ProductPojo p = productService.getUsingBarcode(pojo.getBarcode());
			pojo.setProductName(p.getProductName());
			pojo.setProductId(p.getProductId());
			dao.insert(pojo);
		}

	}

	@Transactional
	public void deleteOrderList() {
		// TODO Auto-generated method stub
		dao.deleteOrderList();
	}

}
