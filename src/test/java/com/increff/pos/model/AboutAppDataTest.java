package com.increff.pos.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import com.increff.pos.service.AbstractUnitTest;

public class AboutAppDataTest extends AbstractUnitTest{
	
	@Test
	public void aboutAppData() {
		AboutAppData aboutAppData = new AboutAppData();
		aboutAppData.setName("nestle");
		aboutAppData.setVersion("1.001");
		assertEquals(aboutAppData.getName(),"nestle");
		assertEquals(aboutAppData.getVersion(),"1.001");
	}
}
