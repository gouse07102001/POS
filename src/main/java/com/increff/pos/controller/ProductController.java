package com.increff.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.ProductDto;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class ProductController {

	@Autowired
	private ProductDto dto;

	@ApiOperation(value = "Adds an Product")
	@RequestMapping(path = "/api/products", method = RequestMethod.POST)
	public void add(@RequestBody ProductForm form) throws Exception {
		dto.add(form);
		
	}
	
	@ApiOperation(value = "Adds an Product")
	@RequestMapping(path = "/api/products/list", method = RequestMethod.POST)
	public void addAll(@RequestBody List<ProductForm> form) throws Exception {
		dto.addAll(form);
		
	}

	@ApiOperation(value = "Gets an Product by ID")
	@RequestMapping(path = "/api/products/{id}", method = RequestMethod.GET)
	public ProductData get(@PathVariable Integer id) throws Exception {
		return dto.get(id);
	}
	
	@ApiOperation(value = "Gets an Product by Barcode")
	@RequestMapping(path = "/api/products/search/{barcode}", method = RequestMethod.GET)
	public ProductData getByBarcode(@PathVariable String barcode) throws Exception {
		return dto.getCheck(barcode);
		 
	}

	@ApiOperation(value = "Gets list of all Products")
	@RequestMapping(path = "/api/products", method = RequestMethod.GET)
	public List<ProductData> getAll() {
		return  dto.getAll();	
	}
	
	@ApiOperation(value = "Gets list of all Barcodes")
	@RequestMapping(path = "/api/product-barcodes", method = RequestMethod.GET)
	public List<String> getAllBarcodes() {
		return  dto.getAllBarcodes();	
	}

	@ApiOperation(value = "Updates an Product")
	@RequestMapping(path = "/api/products/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable Integer id, @RequestBody ProductForm form) throws Exception {
		dto.update(id, form); 
	}
	

	
}
