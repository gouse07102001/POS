package com.increff.pos.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class InfoData {

	private String message;

	public InfoData() {
		message = "Activity time: " + LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
