package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;

import java.util.List;

import com.increff.pos.service.ApiException;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.BrandService;
import com.increff.pos.util.TestDataUtil;


public class BrandDtoTest extends AbstractUnitTest {

    @Autowired
    private BrandDto dto;

    @Autowired
    private BrandService service;


    //  Test for adding an brand
    @Test
    public void testAdd() throws Exception {
        BrandForm brandForm = TestDataUtil.getBrandFormDto("nesTLe", "DaiRy");
        dto.add(brandForm);
        BrandPojo brandPojo = service.getCheck(brandForm.getBrand(), brandForm.getCategory());
        // test added data
        System.out.println(brandPojo.getBrand());
        assertEquals("nestle", brandPojo.getBrand());
        assertEquals("dairy", brandPojo.getCategory());
    }

    //  Test for getting a brand
    @Test
    public void testGet() throws Exception {
        // add
        BrandForm brandForm = TestDataUtil.getBrandFormDto("nestLE", "DairY");
        dto.add(brandForm);
        BrandPojo brandPojo = service.getCheck(brandForm.getBrand(), brandForm.getCategory());
        // get data
        BrandData brandData = dto.get(brandPojo.getBrandId());
        assertEquals("nestle", brandData.getBrand());
        assertEquals("dairy", brandData.getCategory());
    }


    //  Test for getting all brands
    @Test
    public void testGetAll() throws Exception {
        BrandForm brandForm = TestDataUtil.getBrandFormDto("nestLE", "DairY");
        dto.add(brandForm);
        // get all data
        List<BrandData> brandDataList = dto.getAll();
        assertEquals(1, brandDataList.size());
    }

    //  Test for updating a brand
    @Test
    public void testUpdate() throws Exception {
        // add
        BrandForm brandForm = TestDataUtil.getBrandFormDto("nestLE", "DairY");
        dto.add(brandForm);
        BrandPojo brandPojo = service.getCheck(brandForm.getBrand(), brandForm.getCategory());
        // create update form
        BrandForm brandFormUpdate = TestDataUtil.getBrandFormDto("nestle", "FOOd");
        dto.update(brandPojo.getBrandId(), brandFormUpdate);
        BrandPojo brandPojoUpdate = service.getCheck(brandFormUpdate.getBrand(), brandFormUpdate.getCategory());
        // test update
        assertEquals("nestle", brandPojoUpdate.getBrand());
        assertEquals("food", brandPojoUpdate.getCategory());
    }

    //  Test for checking whether a brand pair exists or not
    @Test(expected = ApiException.class)
    public void testCheckBrandExists() throws Exception {
        // add
        BrandForm brandForm = TestDataUtil.getBrandFormDto("nestLE", "DairY");
        dto.add(brandForm);
        BrandPojo brandPojo = service.getCheck(brandForm.getBrand(), brandForm.getCategory());

        dto.checkBrandExits(brandPojo);

    }

}
