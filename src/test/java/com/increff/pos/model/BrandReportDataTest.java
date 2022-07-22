package com.increff.pos.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.increff.pos.service.AbstractUnitTest;

public class BrandReportDataTest extends AbstractUnitTest{

	@Test
	public void brandReportData() {
		BrandReportData brandReportData = new BrandReportData();
		brandReportData.setBrand("nestle");
		brandReportData.setBrandId(1);
		brandReportData.setCategory("biscuits");
		assertEquals(brandReportData.getBrand(),"nestle");
		assertEquals(brandReportData.getCategory(),"biscuits");
	}
}
