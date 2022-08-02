package com.increff.pos.dto;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.*;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.*;
import com.increff.pos.util.TestDataUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class ReportDtoTest extends AbstractUnitTest {

	@Autowired
	private OrderDto dto;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private InventoryDao inventoryDao;

	@Autowired
	private BrandDto brandDto;

	@Autowired
	private BrandDao brandDao;

	@Autowired
	private ReportDto reportDto;

	@Autowired
	private InventoryDto inventoryDto;

	// Test for getting sales report
	@Test
	public void testGetSalesReport() throws Exception {
		OrderItemsForm o = getInventoryFormTestOrder();
		List<OrderItemsForm> orders = new ArrayList<OrderItemsForm>();
		orders.add(o);
		OrderData ord = dto.add(orders);
		SalesReportForm form = new SalesReportForm();
		form.setBrand(null);
		form.setCategory("dairy");
		form.setEndDate("2022-09-30");
		form.setStartDate("2022-07-18");
		List<SalesDataList> sales = reportDto.getSalesReport(form);
		assertEquals(sales.get(0).getQuantity(),5,0);
		assertEquals(sales.get(0).getRevenue(), 25.0,0.01);
		form.setBrand(null);
		form.setCategory(null);
		form.setEndDate("2022-09-30");
		form.setStartDate("2022-07-18");
		 sales = reportDto.getSalesReport(form);
		assertEquals(sales.get(0).getQuantity(),5,0);
		assertEquals(sales.get(0).getRevenue(), 25.0,0.01);
		form.setBrand("nestle");
		form.setCategory(null);
		form.setEndDate("2022-09-30");
		form.setStartDate("2022-07-18");
		 sales = reportDto.getSalesReport(form);
		form.setBrand("nestle");
		form.setCategory("dairy");
		form.setEndDate("2022-09-30");
		form.setStartDate("2022-07-18");
		sales = reportDto.getSalesReport(form);
		assertEquals(sales.get(0).getBrand(), form.getBrand());
		assertEquals(sales.get(0).getCategory(), form.getCategory());
		assertEquals(sales.get(0).getQuantity(),5,0);
		assertEquals(sales.get(0).getRevenue(), 25.0,0.01);

		try {
			form.setEndDate(null);
			sales = reportDto.getSalesReport(form);
		} catch (Exception e) {
			assertEquals("Date cannot be empty", e.getMessage());
		}
		try {
			form.setEndDate("2021-09-30");
			form.setStartDate("2022-07-18");
			sales = reportDto.getSalesReport(form);
		} catch (Exception e) {
			assertEquals("End Date must be after Start Date", e.getMessage());
		}
		try {
			form.setEndDate("2022-09-30");
			form.setStartDate("2022-07-18");
			form.setBrand("abcd");
			form.setCategory("abcd");
		} catch (Exception e) {
			assertEquals("Brand Category Not Found", e.getMessage());
		}
	}

	// Test for getting brand report
	@Test
	public void testGetBrandReport() throws Exception {
		BrandForm brandForm = TestDataUtil.getBrandFormDto("nestLE", "DairY");
		brandDto.add(brandForm);
		List<BrandForm> sales = reportDto.getBrandReport();

		assertEquals(sales.get(0).getBrand(), "nestle");
	}

	// Test for getting inventory reports
	@Test
	public void testGetInventoryReport() throws Exception {
		InventoryForm inventoryForm = getInventoryFormTest();
		inventoryDto.add(inventoryForm);
		List<InventoryReportData> inventory = reportDto.getInventoryReport();

		assertEquals(inventory.get(0).getQuantity().intValue(), 10);
		assertEquals(inventory.get(0).getBarcode(),"bar123");
		assertEquals(inventory.get(0).getCategory(),"dairy");
		assertEquals(inventory.get(0).getBrand(),"nestle");
		assertEquals(inventory.get(0).getProductName(),"product");
	}

	private OrderItemsForm getInventoryFormTestOrder() throws Exception {
		OrderItemsForm o = new OrderItemsForm();
		InventoryPojo i = new InventoryPojo();
		// create data
		String barcode = "bar123";
		BrandPojo b = new BrandPojo();
		ProductPojo p = new ProductPojo();
		b.setBrand("nestle");
		b.setCategory("dairy");
		brandDao.insert(b);

		double mrp = 10.25;
		p.setBarcode(barcode);
		p.setBrandCategory(b.getBrandId());
		p.setProductName("ProDuct");
		p.setMrp(mrp);
		productDao.insert(p);
		Integer quantity = 10;
		i.setProductId(p.getProductId());
		i.setBarcode(barcode);
		i.setQuantity(quantity);
		inventoryDao.insert(i);
		o.setProductId(p.getProductId());
		o.setQuantity(5);
		o.setSellingPrice(5.00);
		return o;
	}

	private InventoryForm getInventoryFormTest() throws Exception {
		InventoryForm i = new InventoryForm();
		// create data
		String barcode = "bar123";
		BrandPojo b = new BrandPojo();
		ProductPojo p = new ProductPojo();
		b.setBrand("nestle");
		b.setCategory("dairy");
		brandDao.insert(b);

		double mrp = 10.25;
		p.setBarcode(barcode);
		p.setBrandCategory(b.getBrandId());
		p.setProductName("product");
		p.setMrp(mrp);
		productDao.insert(p);
		Integer quantity = 10;
		i.setProductId(p.getProductId());
		i.setBarcode(barcode);
		i.setQuantity(quantity);
		return i;
	}

}
