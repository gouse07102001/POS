package com.increff.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.BrandDto;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;

import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class BrandController {

	@Autowired
	private BrandDto dto;

	@ApiOperation(value = "Adds a Brand")
	@RequestMapping(path = "/api/brands", method = RequestMethod.POST)
	public void add(@RequestBody BrandForm form) throws Exception {
		dto.add(form);
	}
	@ApiOperation(value = "Adds list of Brand")
	@RequestMapping(path = "/api/brands/list", method = RequestMethod.POST)
	public void addAll(@RequestBody List<BrandForm> form) throws Exception {
		dto.addAll(form);
	}
	@ApiOperation(value = "Gets a Brand by ID")
	@RequestMapping(path = "/api/brands/{brandId}", method = RequestMethod.GET)
	public BrandData get(@PathVariable Integer brandId) throws Exception {
		return dto.get(brandId);
	}
	@ApiOperation(value = "Gets list of all Brands")
	@RequestMapping(path = "/api/brands", method = RequestMethod.GET)
	public List<BrandData> getAll() {
		return dto.getAll();
	}
	@ApiOperation(value = "Updates a Brand")
	@RequestMapping(path = "/api/brands/{brandId}", method = RequestMethod.PUT)
	public void update(@PathVariable Integer brandId, @RequestBody BrandForm form) throws Exception {
		dto.update(brandId,form);
	}
	
	@ApiOperation(value = "Gets list of all BrandCategory")
	@RequestMapping(path = "/api/brand-category", method = RequestMethod.GET)
	public List<String> getAllBrandCategory() {
		return  dto.getAllBrandCategory();	
	}
	
}
