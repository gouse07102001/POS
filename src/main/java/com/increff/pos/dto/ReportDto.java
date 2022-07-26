package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;


import com.increff.pos.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.service.ReportService;
import com.increff.pos.util.StringUtil;

@Service
public class ReportDto {

    @Autowired
    private ReportService service;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ProductService productservice;

    @Autowired
    private BrandService brandservice;


    @Transactional
    public List<SalesDataList> getSalesReport(SalesReportForm form) throws Exception {
        if (StringUtil.isEmpty(form.getStartDate()) || StringUtil.isEmpty(form.getEndDate())) {
            throw new ApiException("Date cannot be empty");
        }
        if ((form.getEndDate()).compareTo((form.getStartDate())) < 0) {
            throw new ApiException("End Date must be after Start Date");
        }

        if (!StringUtil.isEmpty(form.getBrand()) && !StringUtil.isEmpty(form.getCategory())) {
            BrandPojo p = new BrandPojo();
            p.setBrand(form.getBrand());
            p.setCategory(form.getCategory());
            if (!checkBrandExits(p)) {
                throw new ApiException("Brand Category Not Found");
            }

        }
        List<SalesReportData> brands = service.selectByBrandCategory(form.getBrand(), form.getCategory());
        List<SalesReportItem> data = service.selectByTime(form.getStartDate(), form.getEndDate());
        List<SalesDataList> output = new ArrayList<SalesDataList>();


        for (SalesReportData brand : brands) {
            for (SalesReportItem item : data) {
                if (brand.getId() == item.getBrandCategory()) {
                    SalesDataList lis = new SalesDataList();
                    brand.setQuantity(item.getQuantity());
                    brand.setRevenue(item.getRevenue());

                    lis.setBrand(brand.getBrand());
                    lis.setCategory(brand.getCategory());
                    lis.setQuantity(brand.getQuantity());
                    lis.setRevenue(brand.getRevenue());
                    output.add(lis);
                    break;
                }
            }
        }

        return output;
    }

    @Transactional
    public List<InventoryReportData> getInventoryReport() throws Exception {
        List<InventoryPojo> pojolist = inventoryService.getAll();
        List<InventoryReportData> datalist = new ArrayList<InventoryReportData>();


        for (InventoryPojo p : pojolist) {
            InventoryReportData data = new InventoryReportData();
            ProductPojo propojo = productservice.get(p.getProductId());
            BrandPojo pojo = brandservice.get(propojo.getBrandCategory());
            data.setProductName(propojo.getProductName());
            data.setBarcode(propojo.getBarcode());
            data.setBrand((String) pojo.getBrand());
            data.setCategory(pojo.getCategory());
            data.setQuantity(p.getQuantity());
            datalist.add(data);
        }
        return datalist;
    }

	@Transactional
    public List<BrandForm> getBrandReport() throws Exception {
        List<BrandPojo> pojolist = brandservice.getAll();
        List<BrandForm> datalist = new ArrayList<BrandForm>();

        for (BrandPojo p : pojolist) {
        	BrandForm data = new BrandForm();
            data.setBrand((String) p.getBrand());
            data.setCategory(p.getCategory());
            
            datalist.add(data);
        }
        return datalist;
    }

    public boolean checkBrandExits(BrandPojo p) throws Exception {

        List<BrandPojo> all = brandservice.getAll();
        for (BrandPojo brand : all) {
            if (brand.getBrand().equals(p.getBrand()) && brand.getCategory().equals(p.getCategory())) {
                return true;
            }
        }
        return false;
    }
    
   
}
