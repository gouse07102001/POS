package com.increff.pos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.OrderListPojo;
import com.increff.pos.pojo.OrderItemsPojo;

@Repository
public class OrderDao extends AbstractDao {

    private static final String SELECT_ORDER_ID = "select p from OrderItemsPojo p where orderId=:id";
    private static final String DELETE_ALL = "delete from OrderListPojo";
    @PersistenceContext
    EntityManager em;

    public void insert(OrderItemsPojo p) {
        em.merge(p);
    }
    
    public void insert(OrderListPojo pojo) {
    	em.merge(pojo);	
	}


    public OrderItemsPojo select(Integer id) {
        TypedQuery<OrderItemsPojo> query = getQuery(SELECT_ORDER_ID, OrderItemsPojo.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    public List<OrderItemsPojo> selectByOrdersId(Integer id) {
        TypedQuery<OrderItemsPojo> query = getQuery(SELECT_ORDER_ID, OrderItemsPojo.class);
        query.setParameter("id", id);
        return query.getResultList();
    }


    public void update(OrderItemsPojo p) {

    }

	public void deleteOrderList() {
		Query query = em.createNativeQuery(DELETE_ALL);
		query.executeUpdate();	
		
		
	}


	


}
