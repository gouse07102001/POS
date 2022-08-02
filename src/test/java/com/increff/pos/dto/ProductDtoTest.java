package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.BrandService;
import com.increff.pos.util.TestDataUtil;

public class ProductDtoTest extends AbstractUnitTest {

	@Autowired
	private BrandDto brandDto;

	@Autowired
	private BrandService brandService;

	@Autowired
	private ProductDto productDto;

	// Test for adding a product
	@Test
	public void testAdd() throws Exception {
		BrandForm brandForm = TestDataUtil.getBrandFormDto("nestLE", "DairY");
		brandDto.add(brandForm);
		BrandPojo brandPojo = brandService.getCheck("nestle", "dairy");
		ProductForm productForm = TestDataUtil.getProductFormDto(brandPojo.getBrandId(), "bar123", "munch", 10.50);
		productDto.add(productForm);
		List<ProductData> productDataList = productDto.getUsingBrandCategory((brandPojo.getBrandId()));
		assertEquals("bar123", productDataList.get(0).getBarcode());
		assertEquals("munch", productDataList.get(0).getProductName());
		assertEquals(10.50, productDataList.get(0).getMrp(), 0.01);
		
		try {
			productForm.setBrandCategory(null);
			productDto.add(productForm);
		}
		catch(Exception e) {
			assertEquals("Brand Category did not selected",e.getMessage());
		}
		try {
			productForm.setBrandCategory(brandPojo.getBrandId());
			productForm.setBarcode("bar123");
			productDto.add(productForm);
		}
		catch(Exception e) {
			assertEquals("Barcode must be unique",e.getMessage());
		}
		try {
			productForm.setBarcode(null);
			productDto.add(productForm);
		}
		catch(Exception e) {
			assertEquals("Barcode cannot be empty",e.getMessage());
		}
		try {
			productForm.setBarcode("bar123");
			productForm.setMrp(null);
			productDto.add(productForm);
		}
		catch(Exception e) {
			assertEquals("MRP cannot be empty",e.getMessage());
		}
		try {
			productForm.setBarcode("bar123");
			productForm.setMrp(-10.0);
			productDto.add(productForm);
		}
		catch(Exception e) {
			assertEquals("MRP cannot be negative",e.getMessage());
		}
		try {
			productForm.setMrp(0.0);
			productDto.add(productForm);
		}
		catch(Exception e) {
			assertEquals("MRP cannot be Zero",e.getMessage());
		}
		try {
			productForm.setMrp(10.50);
			productForm.setProductName(null);
			productDto.add(productForm);
		}
		catch(Exception e) {
			assertEquals("Product Name cannot be empty",e.getMessage());
		}
		try {
			productForm.setProductName("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
			productDto.add(productForm);
		}
		catch(Exception e) {
			assertEquals("Maximum limit exceeded for Name",e.getMessage());
		}
		try {
			productForm.setBarcode("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
			productDto.add(productForm);
		}
		catch(Exception e) {
			assertEquals("Maximum limit exceeded for Barcode",e.getMessage());
		}
	}

	@Test
	public void testAddAll() throws Exception {
		BrandForm brandForm = TestDataUtil.getBrandFormDto("nestLE", "DairY");
		List<BrandForm> forms = new ArrayList<BrandForm>();
		forms.add(brandForm);
		brandDto.addAll(forms);
		BrandPojo brandPojo = brandService.getCheck("nestle", "dairy");
		ProductForm productForm = TestDataUtil.getProductFormDto(brandPojo.getBrandId(), "bar123", "munch", 10.50);
		List<ProductForm> productForms = new ArrayList<ProductForm>();
		productForms.add(productForm);
		productDto.addAll(productForms);
		List<ProductData> productDataList = productDto.getAll();
		assertEquals("bar123", productDataList.get(0).getBarcode());
		assertEquals("munch", productDataList.get(0).getProductName());
		assertEquals(10.50, productDataList.get(0).getMrp(), 0.01);
	}

	// Test for getting a product
	@Test
	public void testGet() throws Exception {
		// add data
		BrandForm brandForm = TestDataUtil.getBrandFormDto("nestLE", "DairY");
		brandDto.add(brandForm);
		BrandPojo brandPojo = brandService.getCheck("nestle", "dairy");
		ProductForm productForm = TestDataUtil.getProductFormDto(brandPojo.getBrandId(), "bar123", "munch", 10.50);

		productDto.add(productForm);
		// search product

		List<ProductData> productDataList = productDto.getUsingBrandCategory(brandPojo.getBrandId());
		// get data

		ProductData productData = productDto.get(productDataList.get(0).getProductId());

		assertEquals("bar123", productData.getBarcode());
		assertEquals("munch", productData.getProductName());
		assertEquals(10.50, productData.getMrp(), 0.01);
		
		try {
			productData = productDto.get(100);
		}
		catch(Exception e) {
			assertEquals("Product with given ID does not exist",e.getMessage());
		}
	}

	@Test
	public void testGetAllBarcodes() throws Exception {
		BrandForm brandForm = TestDataUtil.getBrandFormDto("nestLE", "DairY");
		brandDto.add(brandForm);
		BrandPojo brandPojo = brandService.getCheck("nestle", "dairy");
		ProductForm productForm = TestDataUtil.getProductFormDto(brandPojo.getBrandId(), "bar123", "munch", 10.50);
		productDto.add(productForm);
		BrandForm brandForm2 = TestDataUtil.getBrandFormDto("nestLE", "food");
		brandDto.add(brandForm2);
		BrandPojo brandPojo2 = brandService.getCheck("nestle", "food");
		ProductForm productForm2 = TestDataUtil.getProductFormDto(brandPojo2.getBrandId(), "bar1234", "kitkat", 10.50);
		productDto.add(productForm2);

		List<String> barcodes = productDto.getAllBarcodes();
		assertEquals(barcodes.get(0),"bar123");
		assertEquals(barcodes.get(1),"bar1234");
	}

	// Test for getting product using a barcode
	@Test
	public void testGetUsingBarcode() throws Exception {
		// add data
		BrandForm brandForm = TestDataUtil.getBrandFormDto("nestLE", "DairY ");
		brandDto.add(brandForm);
		BrandPojo brandPojo = brandService.getCheck("nestle", "dairy");
		ProductForm productForm = TestDataUtil.getProductFormDto(brandPojo.getBrandId(), "bar123", "munch", 10.50);
		// search product

		productDto.add(productForm);

		List<ProductData> productDataList = productDto.getUsingBrandCategory(brandPojo.getBrandId());
		// get data
		ProductData productData = productDto.getCheck(productDataList.get(0).getBarcode());
		assertEquals("bar123", productData.getBarcode());
		assertEquals("munch", productData.getProductName());
		assertEquals(10.50, productData.getMrp(), 0.01);
		
		try {
			productData = productDto.getCheck("abcd");
		}
		catch(Exception e) {
			assertEquals("Barcode does not exist",e.getMessage());
		}
	}

	// Test for getting all the products
	@Test
	public void testGetAll() throws Exception {
		// add data
		BrandForm brandForm = TestDataUtil.getBrandFormDto("nestLE", "DairY ");
		brandDto.add(brandForm);
		BrandPojo brandPojo = brandService.getCheck("nestle", "dairy");
		ProductForm productForm = TestDataUtil.getProductFormDto(brandPojo.getBrandId(), "bar123", "munch", 10.50);

		productDto.add(productForm);
		// get all
		List<ProductData> productDataList = productDto.getAll();
		assertEquals(1, productDataList.size());
	}

	// Test for updating a product
	@Test
	public void testUpdate() throws Exception {
		// add data
		BrandForm brandForm = TestDataUtil.getBrandFormDto("nestLE", "DairY");
		brandDto.add(brandForm);
		BrandPojo brandPojo = brandService.getCheck("nestle", "dairy");

		ProductForm productForm = TestDataUtil.getProductFormDto(brandPojo.getBrandId(), "bar123", "munch", 10.50);
		productDto.add(productForm);
		// search product
		ProductForm productForm3 = TestDataUtil.getProductFormDto(brandPojo.getBrandId(), "bar1234", "munchcrispy", 10.50);
		productDto.add(productForm3);
		List<ProductData> productDataList = productDto.getUsingBrandCategory(brandPojo.getBrandId());
		ProductForm productForm2 = TestDataUtil.getProductFormDto(brandPojo.getBrandId(), "bar123", "kitkat", 10.50);
		// update product
		ProductForm productUpdateForm = new ProductForm();
		productUpdateForm.setBarcode(productForm2.getBarcode());
		productUpdateForm.setBrandCategory(brandPojo.getBrandId());
		productUpdateForm.setMrp(productForm2.getMrp());
		productUpdateForm.setProductName(productForm2.getProductName());

		productDto.update(productDataList.get(0).getProductId(), productUpdateForm);
		// get and test
		ProductData productData = productDto.get(productDataList.get(0).getProductId());

		assertEquals("kitkat", productData.getProductName());
		try {
			productDto.update(productDataList.get(1).getProductId(), productUpdateForm);
		}
		catch(Exception e) {
			assertEquals("Barcode already exist",e.getMessage());
		}
		try {
			productUpdateForm.setBarcode("bar12345");
			productDto.update(100, productUpdateForm);
		}
		catch(Exception e) {
			assertEquals("Product with given ID does not exist",e.getMessage());
		}
	}

	// Test for getting a product using barcode
	@Test
	public void testGetIdByBarcode() throws Exception {
		BrandForm brandForm = TestDataUtil.getBrandFormDto("nestLE", "DairY");
		brandDto.add(brandForm);
		BrandPojo brandPojo = brandService.getCheck("nestle", "dairy");
		ProductForm productForm = TestDataUtil.getProductFormDto(brandPojo.getBrandId(), "bar123", "munch", 10.50);

		productDto.add(productForm);

		ProductPojo p = productDto.getIdByBarcode(productForm.getBarcode());
		assertEquals(p.getProductName(), productForm.getProductName());
		try {
			p = productDto.getIdByBarcode("bar12345");
		}
		catch(Exception e) {
			assertEquals("Barcode does not exist",e.getMessage());
		}

		try {
			p = productDto.getIdByBarcode(null);
		}
		catch(Exception e) {
			assertEquals("Barcode cannot be empty",e.getMessage());
		}
	}

	// Test for getting mrp of a product using barcode
	@Test
	public void testGetMrpByBarcode() throws Exception {
		BrandForm brandForm = TestDataUtil.getBrandFormDto("nestLE", "DairY");
		brandDto.add(brandForm);
		BrandPojo brandPojo = brandService.getCheck("nestle", "dairy");
		ProductForm productForm = TestDataUtil.getProductFormDto(brandPojo.getBrandId(), "bar123", "munch", 10.50);

		productDto.add(productForm);

		Double p = productDto.getMrpByBarcode(productForm.getBarcode());
		assertEquals(p, productForm.getMrp());
		
		try {
			p = productDto.getMrpByBarcode("bar12345");
		}
		catch(Exception e) {
			assertEquals("Barcode does not exist",e.getMessage());
		}
	}
}
