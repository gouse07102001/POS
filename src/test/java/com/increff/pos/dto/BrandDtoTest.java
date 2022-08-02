package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.util.TestDataUtil;

public class BrandDtoTest extends AbstractUnitTest {

	@Autowired
	private BrandDto dto;

	@Autowired
	private BrandService service;

	// Test for adding an brand
	@Test
	public void testAdd() throws Exception {
		BrandForm brandForm = TestDataUtil.getBrandFormDto("nesTLe", "DaiRy");
		dto.add(brandForm);
		BrandPojo brandPojo = service.getCheck(brandForm.getBrand(), brandForm.getCategory());
		// test added data
		assertEquals("nestle", brandPojo.getBrand());
		assertEquals("dairy", brandPojo.getCategory());
		try {
			brandForm.setCategory(null);
			dto.add(brandForm);
		}
		catch(Exception e) {
			assertEquals("Category Can't be empty",e.getMessage());
		}
		try {
			brandForm.setBrand(null);
			dto.add(brandForm);
		}
		catch(Exception e) {
			assertEquals("Brand Can't be empty",e.getMessage());
		}
		
		brandForm.setBrand("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		brandForm.setCategory("DaiRy");
		try {
			dto.add(brandForm);
		}
		catch(Exception e) {
			assertEquals("Maximum limit exceeded for Brand",e.getMessage());
		}
		
		brandForm.setBrand("nesTLe");
		brandForm.setCategory("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		try {
			dto.add(brandForm);
		}
		catch(Exception e) {
			assertEquals("Maximum limit exceeded for Category",e.getMessage());
		}
		
		brandForm.setBrand("nesTLe");
		brandForm.setCategory("DaiRy");
		
		try {
			dto.add(brandForm);
			brandForm.setBrand("NEsTLe");
			brandForm.setCategory("DAiry");
			dto.add(brandForm);
		}
		catch(Exception e) {
			assertEquals("Brand Category pair already exist",e.getMessage());
		}
	}

	// Test for getting a brand
	@Test
	public void testGet() throws Exception {
		// add
		BrandForm brandForm = TestDataUtil.getBrandFormDto("nestLE", "DairY");
		dto.add(brandForm);
		BrandPojo brandPojo = service.getCheck(brandForm.getBrand(), brandForm.getCategory());
		// get data
		BrandData brandData = dto.get(brandPojo.getBrandId());
		assertEquals("nestle", brandData.getBrand());
		assertEquals("dairy", brandData.getCategory());
		try{
			brandPojo = service.getCheck("brand", "category");
		}
		catch(Exception e){
			assertEquals("Brand and Category with given names does not exist",e.getMessage());
		}
		try {
			 brandData = dto.get(100);
		}
		catch(Exception e) {
			assertEquals("Brand with given ID does not exist",e.getMessage());
		}
	}

	// Test for getting all brands
	@Test
	public void testGetAll() throws Exception {
		BrandForm brandForm = TestDataUtil.getBrandFormDto("nestLE", "DairY");
		dto.add(brandForm);
		// get all data
		List<BrandData> brandDataList = dto.getAll();
		assertEquals(1, brandDataList.size());
	}

	@Test
	public void testGetAllBrandCategory() throws Exception {
		BrandForm brandForm = TestDataUtil.getBrandFormDto("nestLE", "DairY");
		dto.add(brandForm);
		assertEquals(dto.getAllBrandCategory().get(0),"nestle-dairy");
	}

	// Test for updating a brand
	@Test
	public void testUpdate() throws Exception {
		// add
		BrandForm brandForm = TestDataUtil.getBrandFormDto("nestLE", "DairY");
		dto.add(brandForm);
		BrandPojo brandPojo = service.getCheck(brandForm.getBrand(), brandForm.getCategory());
		// create update form
		BrandForm brandFormUpdate = TestDataUtil.getBrandFormDto("nestle", "FOOd");
		dto.update(brandPojo.getBrandId(), brandFormUpdate);
		BrandForm brandForm2 = TestDataUtil.getBrandFormDto("nestLE", "DairY");
		dto.add(brandForm2);
		BrandPojo brandPojoUpdate = service.getCheck(brandFormUpdate.getBrand(), brandFormUpdate.getCategory());
		// test update
		assertEquals("nestle", brandPojoUpdate.getBrand());
		assertEquals("food", brandPojoUpdate.getCategory());
		
		try {
			brandForm.setBrand("nestle");
			brandForm.setCategory("dairy");
			dto.update(2,brandForm);
		}
		catch(Exception e) {
			assertEquals("Brand Category pair already exist",e.getMessage());
		}
		try {
			brandForm.setBrand("nestle");
			brandForm.setCategory(null);
			dto.update(2,brandForm);
		}
		catch(Exception e) {
			assertEquals("Category Can't be empty",e.getMessage());
		}
	}
}
