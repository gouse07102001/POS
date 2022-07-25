package com.increff.pos.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.InventoryDto;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class InventoryController {

	@Autowired
	private InventoryDto dto;

	@ApiOperation(value = "Adds an Inventory")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.POST)
	public void add(@RequestBody InventoryForm form) throws Exception {
		dto.add(form);
	}
	
	@ApiOperation(value = "Adds List of Inventory")
	@RequestMapping(path = "/api/inventory/list", method = RequestMethod.POST)
	public void addAll(@RequestBody List<InventoryForm> form) throws Exception {
		dto.addAll(form);
	}

	@ApiOperation(value = "Gets an Inventory by ID")
	@RequestMapping(path = "/api/inventory/{inventoryId}", method = RequestMethod.GET)
	public InventoryPojo get(@PathVariable Integer inventoryId) throws Exception {
		return dto.get(inventoryId);
		 
	}
	

	@ApiOperation(value = "Gets list of all Inventory")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
	public List<InventoryData> getAll() {
		return dto.getAll()
;	}
	
	@ApiOperation(value = "Updates a Inventory")
    @RequestMapping(path = "/api/inventory/{inventoryId}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer inventoryId, @RequestBody InventoryForm form)
            throws Exception {
        dto.update(inventoryId, form);
    }


    @ApiOperation(value = "Gets Quantity of Inventory By Barcode")
    @RequestMapping(path = "/api/inventory/quantity/{barcode}", method = RequestMethod.GET)
    public Integer getQuantity(@PathVariable String barcode) throws Exception {
        return dto.getQuantityByBarcode(barcode);
    }
	

	
}
