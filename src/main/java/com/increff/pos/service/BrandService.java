
package com.increff.pos.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;

@Service
public class BrandService {

	@Autowired
	private BrandDao dao;

	@Transactional
	public void add(BrandPojo p) {
		normalize(p);
		dao.insert(p);
	}
	
	@Transactional
	public void addAll(List<BrandPojo> pojos) {
		// TODO Auto-generated method stub
		for (BrandPojo pojo : pojos) {
			normalize(pojo);
			dao.insert(pojo);
		}
	}

	@Transactional(rollbackOn = ApiException.class)
	public BrandPojo get(Integer brandId) throws Exception {
		return getCheck(brandId);
	}

	@Transactional
	public List<BrandPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional(rollbackOn = ApiException.class)
	public void update(Integer brandId, BrandPojo p) throws Exception {
		normalize(p);
		BrandPojo ex = getCheck(brandId);
		ex.setCategory(p.getCategory());
		ex.setBrand(p.getBrand());
		dao.update(p);
	}

	@Transactional
	public BrandPojo getCheck(Integer brandId) throws Exception {
		BrandPojo p = dao.select(brandId);
		if (p == null) {
			throw new ApiException("Brand with given ID does not exist, brandId: " + brandId);
		}
		return p;
	}

	@Transactional
	public BrandPojo getCheck(String brand, String category) throws Exception {
		BrandPojo p = dao.select(brand.toLowerCase(), category.toLowerCase());
		if (p == null) {
			throw new ApiException(
					"Brand and Category with given names does not exist, brand: " + brand + " category: " + category);
		}
		return p;
	}

	private static void normalize(BrandPojo p) {
		p.setBrand(p.getBrand().toLowerCase().trim());
		p.setCategory(p.getCategory().toLowerCase().trim());
	}

	public List<String> getAllBrandCategory() {
		List<BrandPojo> pojos = new ArrayList<BrandPojo>();
		pojos = getAll();
		List<String> brandCategoryList = new ArrayList<String>();
		for (BrandPojo pojo : pojos) {
			brandCategoryList.add(pojo.getBrand() + "-" + pojo.getCategory());
		}
		return brandCategoryList;
	}

}
