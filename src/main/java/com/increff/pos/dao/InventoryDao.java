package com.increff.pos.dao;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.service.ApiException;

@Repository
public class InventoryDao extends AbstractDao {

	private static String SELECT_ALL = "select p from InventoryPojo p";
	private static String SELECT_INVENTORY = "select i from InventoryPojo i where inventoryId=:id";
    private static String SELECT_INVENTORY_ID_BY_BARCODE = "select i from InventoryPojo i where barcode=:barcode";
    private static String UPDATE_INVENTORY = "update InventoryPojo set quantity=:quantity where id=:id ";
    private static String SELECT_INVENTORY_BY_PRODUCT_ID = "select i from InventoryPojo i where productId=:id";

	@PersistenceContext
	private EntityManager em;
	
	public void insert(InventoryPojo p) throws Exception { 
		em.persist(p);
	}
	
	public List<InventoryPojo> selectAll() {
		TypedQuery<InventoryPojo> query = getQuery(SELECT_ALL);
		return query.getResultList();
	}
	public InventoryPojo select(Integer id) {
		TypedQuery<InventoryPojo> query = getQuery(SELECT_INVENTORY);
		query.setParameter("id", id);
		if (query.getResultList().size() == 0) {
            return new InventoryPojo();
        }
        return getSingle(query);
	}
	
	public InventoryPojo getInventory(Integer inventoryId) {
        TypedQuery<InventoryPojo> query = getQuery(SELECT_INVENTORY);
        query.setParameter("id", inventoryId);
        return getSingle(query);
    }
	
	public InventoryPojo getInventoryByProductID(Integer productId) {
        TypedQuery<InventoryPojo> query = getQuery(SELECT_INVENTORY_BY_PRODUCT_ID);
        query.setParameter("id", productId);
        return getSingle(query);
    }

	public void update(InventoryPojo p) {
	}

	TypedQuery<InventoryPojo> getQuery(String jpql) {
		return em.createQuery(jpql, InventoryPojo.class);
	}
	
	public void update(Integer id, Integer quantity) {
        Query query = getQueryUpdate(UPDATE_INVENTORY);
        query.setParameter("id", id);
        query.setParameter("quantity", quantity);
        query.executeUpdate();
    }

    public InventoryPojo getIdByBarcode(String barcode) throws Exception {
        TypedQuery<InventoryPojo> query = getQuery(SELECT_INVENTORY_ID_BY_BARCODE);
        query.setParameter("barcode", barcode);
        return getSingle(query);
    }



    Query getQueryUpdate(String jpql) {
        return em.createQuery(jpql);
    }

    TypedQuery<InventoryPojo> getQueryInventory(String jpql) {
        return em.createQuery(jpql, InventoryPojo.class);
    }
	
}