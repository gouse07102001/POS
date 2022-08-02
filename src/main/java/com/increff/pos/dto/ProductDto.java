package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductService;

@Service
public class ProductDto {

    @Autowired
    private ProductService service;

    @Autowired
    private BrandService brandService;


    public void add(ProductForm form) throws Exception {

        String error = validateProductForm(form);
        if (error != null) {
            throw new ApiException(error);
        } else if (!isBarcodeUnique(form.getBarcode())) {
            throw new ApiException("Barcode must be unique");
        } else {
            service.add(convert(form));
        }
    }

    public void addAll(List<ProductForm> form) throws Exception {
        List<ProductPojo> pojo = new ArrayList<ProductPojo>();
        Integer index = 0;
        for (ProductForm iter : form) {
            String error = validateProductForm(iter);
            if (error != null) {
                throw new ApiException(error + "/" + index);
            } else if (!isBarcodeUnique(iter.getBarcode())) {
                throw new ApiException("Barcode must be unique/" + index);
            } else {
                pojo.add(convert(iter));
            }
            index += 1;
        }
        service.addAll(pojo);

    }


    public ProductData get(Integer id) throws Exception {
        // TODO Auto-generated method stub
        ProductPojo d = service.get(id);
        return convert(d);
    }

    public List<ProductData> getUsingBrandCategory(Integer brandCategory) {
        List<ProductPojo> pojolist = service.getUsingBrandCategory(brandCategory);
        List<ProductData> datalist = new ArrayList<ProductData>();
        for (ProductPojo p : pojolist) {
            datalist.add(convert(p));

        }
        return datalist;
    }

    public ProductData getCheck(String barcode) throws Exception {
        ProductPojo p = service.getCheckBarcode(barcode);
        if (p == null) {
            throw new ApiException("Barcode does not exist");
        }
        return convert(p);
    }

    public List<ProductData> getAll() {
        // TODO Auto-generated method stub
        List<ProductPojo> list = service.getAll();
        List<ProductData> list_brandData = new ArrayList<ProductData>();
        for (ProductPojo i : list) {
            list_brandData.add(convert(i));
        }
        return list_brandData;
    }

    public void update(Integer id, ProductForm f) throws Exception {
        ProductPojo s = service.getCheckBarcode(f.getBarcode());
        if (s != null && s.getProductId() != id) {
            throw new ApiException("Barcode already exist");
        }
        brandService.getCheck(f.getBrandCategory());
        String error = validateProductForm(f);
        if (error != null) {
            throw new ApiException(error);
        }
        service.update(id, convert(f));
    }

    public List<String> getAllBarcodes() {
        return service.getAllBarcodes();
    }

    public ProductPojo getIdByBarcode(String barcode) throws Exception {
        return service.checkBarcodeSwap(barcode);

    }

    public double getMrpByBarcode(String barcode) throws Exception {
        return service.getMrpByBarcode(barcode);
    }


    private static ProductData convert(ProductPojo p) {
        ProductData d = new ProductData();
        d.setBarcode(p.getBarcode());
        d.setBrandCategory(p.getBrandCategory());
        d.setMrp(p.getMrp());
        d.setProductName(p.getProductName());
        d.setProductId(p.getProductId());
        return d;
    }

    private static ProductPojo convert(ProductForm f) {
        ProductPojo p = new ProductPojo();
        p.setBarcode(f.getBarcode().trim());
        p.setBrandCategory(f.getBrandCategory());
        p.setMrp(f.getMrp());
        p.setProductName(f.getProductName().trim());
        return p;
    }

    private boolean isBarcodeUnique(String barcode) {
        List<ProductPojo> list = service.getAll();
        barcode = barcode.trim();
        for (ProductPojo p : list) {
            if (p.getBarcode().equalsIgnoreCase(barcode)) {
                return false;
            }
        }
        return true;
    }

    private static String validateProductForm(ProductForm form) {

        if (form.getBarcode() == null || form.getBarcode().isEmpty()) {
            return ("Barcode cannot be empty");
        }

        if (form.getBrandCategory() == null) {
            return ("Brand Category did not selected");
        }
        if (form.getProductName() == null || form.getProductName().isEmpty()) {
            return ("Product Name cannot be empty");
        }
        if (form.getMrp() == null) {
            return ("MRP cannot be empty");
        }
        if (form.getMrp() == 0) {
            return ("MRP cannot be Zero");
        }
        if (form.getMrp() < 0) {
            return ("MRP cannot be negative");
        }
        if (form.getBarcode().length() > 255) {
            return "Maximum limit exceeded for Barcode";
        }
        if (form.getProductName().length() > 255) {
            return "Maximum limit exceeded for Name";
        }
        return null;
    }


}
