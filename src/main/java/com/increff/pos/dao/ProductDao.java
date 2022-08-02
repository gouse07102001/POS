package com.increff.pos.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;

@Repository
public class ProductDao extends AbstractDao {

	private static String SELECT_PRODUCT_ID = "select p from ProductPojo p where productId=:id";
	private static String SELECT_ALL = "select p from ProductPojo p";
	private static String SELECT_BARCODE = "select p from ProductPojo p where barcode=:barcode";
	private static final String SELECT_BRAND_CATEGORY = "select p from ProductPojo p where brandCategory=:brandCategory";
	private static final String SELECT_MRP_BY_BARCODE = "select mrp from ProductPojo where barcode=:barcode";

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void insert(ProductPojo p) {
		em.persist(p);
	}

	public ProductPojo select(Integer id) {
		TypedQuery<ProductPojo> query = getQuery(SELECT_PRODUCT_ID);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public List<ProductPojo> selectByBrandCategory(Integer brandCategory) {
		TypedQuery<ProductPojo> query = getQuery(SELECT_BRAND_CATEGORY, ProductPojo.class);
		query.setParameter("brandCategory", brandCategory);
		return query.getResultList();
	}

	public ProductPojo selectByBarcode(String barcode) {
		TypedQuery<ProductPojo> query = getQuery(SELECT_BARCODE);
		query.setParameter("barcode", barcode);
		return getSingle(query);
	}

	public List<ProductPojo> selectAll() {
		TypedQuery<ProductPojo> query = getQuery(SELECT_ALL);
		return query.getResultList();
	}
	
	public Double getMrpByBarcode(String barcode) {
        Query query = em.createQuery(SELECT_MRP_BY_BARCODE);
        query.setParameter("barcode", barcode);
        return (Double) query.getSingleResult();
    }
	
	public ProductPojo selectByParam(String barcode) {
        TypedQuery<ProductPojo> query = getQuery(SELECT_BARCODE, ProductPojo.class);
        query.setParameter("barcode", barcode);
        return getSingle(query);
    }

	public void update(ProductPojo p) {
	}

	TypedQuery<ProductPojo> getQuery(String jpql) {
		return em.createQuery(jpql, ProductPojo.class);
	}


}