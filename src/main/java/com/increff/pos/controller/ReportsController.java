package com.increff.pos.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.increff.pos.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.ReportDto;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.ReportService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Api
@RestController
public class ReportsController {

    @Autowired
    public ReportDto dto;
    @Autowired
    public ReportService service; //todo to remove this.

    @ApiOperation(value = " Get SalesReport")
    @RequestMapping(path = "/api/report/sales", method = RequestMethod.POST)
    public List<SalesDataList> getSalesReport(@RequestBody SalesReportForm form) throws Exception {
        return dto.getSalesReport(form);
    }

    @ApiOperation(value = " Get InventoryReport")
    @RequestMapping(path = "/api/report/inventory", method = RequestMethod.GET)
    public List<InventoryReportData> getInventoryReport() throws Exception {
        return dto.getInventoryReport();
    }

    @ApiOperation(value = " Get BrandReport")
    @RequestMapping(path = "/api/report/brand", method = RequestMethod.GET)
    public List<BrandForm> getBrandReport() throws Exception {
        return dto.getBrandReport();
    }

}		
