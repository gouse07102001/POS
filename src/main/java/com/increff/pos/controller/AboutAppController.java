package com.increff.pos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.AboutAppData;
import com.increff.pos.service.AboutAppService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class AboutAppController {

	@Autowired
	private AboutAppService service;

	@ApiOperation(value = "Gives application name and version")
	@RequestMapping(path = "/api/about", method = RequestMethod.GET)
	public AboutAppData getDetails() {
		AboutAppData d = new AboutAppData();
		d.setName(service.getName());
		d.setVersion(service.getVersion());
		return d;
	}



}
