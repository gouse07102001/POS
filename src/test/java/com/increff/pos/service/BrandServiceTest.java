package com.increff.pos.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.util.StringUtil;


public class BrandServiceTest extends AbstractUnitTest {

    @Autowired
    private BrandService service;


    //  Test for adding brand
    @Test
    public void testAdd() throws ApiException {
        BrandPojo p = new BrandPojo();
        p.setBrand("nestle");
        p.setCategory("dairy");
        service.add(p);
    }


    //  Test for normalizing
    @Test
    public void testNormalize() {
        BrandPojo p = new BrandPojo();
        p.setBrand("  Nestle  ");
        p.setBrand(StringUtil.toLowerCase((String) p.getBrand()));
        p.setCategory(StringUtil.toLowerCase(p.getCategory()));
        assertEquals("nestle", p.getBrand());
    }

}
