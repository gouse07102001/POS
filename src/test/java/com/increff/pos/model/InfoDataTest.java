package com.increff.pos.model;

import com.increff.pos.service.AbstractUnitTest;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class InfoDataTest extends AbstractUnitTest{

	@Test
	public void infoTest() {
		InfoData infoData = new InfoData();
		infoData.setMessage("Hello!!");
		assertEquals(infoData.getMessage(),"Hello!!");
	}
}
