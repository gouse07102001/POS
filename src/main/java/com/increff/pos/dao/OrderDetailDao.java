package com.increff.pos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.increff.pos.pojo.OrderListPojo;
import com.increff.pos.pojo.OrderPojo;

@Service
public class OrderDetailDao extends AbstractDao {
    public static String SELECT_ORDER_ID = "select p from OrderPojo p where orderId=:id";
    public static String SELECT_ALL = "select p from OrderPojo p";
    public static String SELECT_ALL_ORDER_DETAILS = "select p from OrderListPojo p";

    @PersistenceContext
    EntityManager em;

    public void add(OrderPojo p) {
        em.persist(p);
    }

    public OrderPojo select(Integer id) {
        TypedQuery<OrderPojo> query = getQuery(SELECT_ORDER_ID, OrderPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public List<OrderPojo> selectAll() {
        TypedQuery<OrderPojo> query = getQuery(SELECT_ALL, OrderPojo.class);
        return query.getResultList();
    }

    public List<OrderListPojo> selectAllOrderDetails() {
    	 TypedQuery<OrderListPojo> query = getQuery(SELECT_ALL_ORDER_DETAILS, OrderListPojo.class);
         return query.getResultList();
	}

}
