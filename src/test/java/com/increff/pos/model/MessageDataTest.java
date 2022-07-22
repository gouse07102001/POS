package com.increff.pos.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.increff.pos.service.AbstractUnitTest;

public class MessageDataTest extends AbstractUnitTest {
	
	
	 
	
	@Test
	public void messageTest() {
		MessageData messageData = new MessageData("Hello!!");
		messageData.setMessage("Hello!!");
		assertEquals(messageData.getMessage(),"Hello!!");
	}

}
