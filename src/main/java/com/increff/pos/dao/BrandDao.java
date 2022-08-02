
package com.increff.pos.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.BrandPojo;

@Repository
public class BrandDao extends AbstractDao {
	
	private static String SELECT_BRAND_ID = "select p from BrandPojo p where BrandId=:id";
	private static String SELECT_ALL = "select p from BrandPojo p";
	private static String SELECT_BRAND_CATEGORY = "select p from BrandPojo p where brand=:brand and category=:category";
	
	
	@PersistenceContext
	private EntityManager em;
	
	@Transactional
	public void insert(BrandPojo p) {
		em.persist(p);
	}
	

	public BrandPojo select(Integer id) {
		TypedQuery<BrandPojo> query = getQuery(SELECT_BRAND_ID);
		query.setParameter("id", id);
		return getSingle(query);
	}
	
	public BrandPojo select(String brand,String category) {
		TypedQuery<BrandPojo> query = getQuery(SELECT_BRAND_CATEGORY, BrandPojo.class);
		query.setParameter("brand", brand);
		query.setParameter("category", category);
		return getSingle(query);
	}

	public List<BrandPojo> selectAll() {
		TypedQuery<BrandPojo> query = getQuery(SELECT_ALL);
		return query.getResultList();
	}

	public void update(BrandPojo p) {
		
	}

	public TypedQuery<BrandPojo> getQuery(String jpql) { 
		return em.createQuery(jpql, BrandPojo.class);
	}

}
