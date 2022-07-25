package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.BrandService;
import com.increff.pos.util.TestDataUtil;


public class ProductDtoTest extends AbstractUnitTest {

    @Autowired
    private BrandDto brandDto;

    @Autowired
    private BrandService brandService;

    @Autowired
    private ProductDto productDto;

    //  Test for adding a product
    @Test
    public void testAdd() throws Exception {
        BrandForm brandForm = TestDataUtil.getBrandFormDto("nestLE", "DairY");
        brandDto.add(brandForm);
        BrandPojo brandPojo = brandService.getCheck("nestle", "dairy");
        ProductForm productForm = TestDataUtil.getProductFormDto(brandPojo.getBrandId(), "bar123", "munch", 10.50);
        
        productDto.add(productForm);

        //System.out.println(brandPojo.getBrandId());
        List<ProductData> productDataList = productDto.getUsingBrandCategory((brandPojo.getBrandId()));
        //System.out.println(productDataList.get(0).getBarcode());
        assertEquals("bar123", productDataList.get(0).getBarcode());
        assertEquals("munch", productDataList.get(0).getproductName());
        assertEquals(10.50, productDataList.get(0).getMrp(), 0.01);
    }
    
    @Test
    public void testAddAll() throws Exception{
    	BrandForm brandForm = TestDataUtil.getBrandFormDto("nestLE", "DairY");
        List<BrandForm> forms = new ArrayList<BrandForm>();
        forms.add(brandForm);
        brandDto.addAll(forms);
        BrandPojo brandPojo = brandService.getCheck("nestle", "dairy");
        ProductForm productForm = TestDataUtil.getProductFormDto(brandPojo.getBrandId(), "bar123", "munch", 10.50);
        List<ProductForm> productForms = new ArrayList<ProductForm>();
        productForms.add(productForm);
        productDto.addAll(productForms);
        List<ProductData> productDataList = productDto.getAll();
        assertEquals("bar123", productDataList.get(0).getBarcode());
        assertEquals("munch", productDataList.get(0).getproductName());
        assertEquals(10.50, productDataList.get(0).getMrp(), 0.01);
    }


    //  Test for getting a product
    @Test
    public void testGet() throws Exception {
        // add data
        BrandForm brandForm = TestDataUtil.getBrandFormDto("nestLE", "DairY");
        brandDto.add(brandForm);
        BrandPojo brandPojo = brandService.getCheck("nestle", "dairy");
        ProductForm productForm = TestDataUtil.getProductFormDto(brandPojo.getBrandId(), "bar123", "munch", 10.50);
       
        System.out.println("Product Test Get" + brandPojo.getBrand());
        productDto.add(productForm);
        // search product

        List<ProductData> productDataList = productDto.getUsingBrandCategory(brandPojo.getBrandId());
        // get data

        ProductData productData = productDto.get(productDataList.get(0).getProductId());

        assertEquals("bar123", productData.getBarcode());
        assertEquals("munch", productData.getproductName());
        assertEquals(10.50, productData.getMrp(), 0.01);
    }

    //  Test for getting product using a barcode
    @Test
    public void testGetUsingBarcode() throws Exception {
        // add data
        BrandForm brandForm = TestDataUtil.getBrandFormDto("nestLE", "DairY ");
        brandDto.add(brandForm);
        BrandPojo brandPojo = brandService.getCheck("nestle", "dairy");
        ProductForm productForm = TestDataUtil.getProductFormDto(brandPojo.getBrandId(), "bar123", "munch", 10.50);
        // search product
       
        System.out.println("Product Test Get Using Barcode" + brandPojo.getBrand());
        productDto.add(productForm);

        List<ProductData> productDataList = productDto.getUsingBrandCategory(brandPojo.getBrandId());
        // get data
        //System.out.println(brandPojo.getId());
        ProductData productData = productDto.getCheck(productDataList.get(0).getBarcode());
        assertEquals("bar123", productData.getBarcode());
        assertEquals("munch", productData.getproductName());
        assertEquals(10.50, productData.getMrp(), 0.01);
    }

    //  Test for getting all the products
    @Test
    public void testGetAll() throws Exception {
        // add data
        BrandForm brandForm = TestDataUtil.getBrandFormDto("nestLE", "DairY ");
        brandDto.add(brandForm);
        BrandPojo brandPojo = brandService.getCheck("nestle", "dairy");
        ProductForm productForm = TestDataUtil.getProductFormDto(brandPojo.getBrandId(), "bar123", "munch", 10.50);
       
        System.out.println("Product Test Get aLL" + brandPojo.getBrand());
        productDto.add(productForm);
        // get all
        List<ProductData> productDataList = productDto.getAll();
        assertEquals(1, productDataList.size());
    }

    //  Test for updating a product
    @Test
    public void testUpdate() throws Exception {
        // add data
        BrandForm brandForm = TestDataUtil.getBrandFormDto("nestLE", "DairY");
        brandDto.add(brandForm);
        BrandPojo brandPojo = brandService.getCheck("nestle", "dairy");
        System.out.println("Product Test Update" + brandPojo.getBrand());
        ProductForm productForm = TestDataUtil.getProductFormDto(brandPojo.getBrandId(), "bar123", "munch", 10.50);
        productDto.add(productForm);
        // search product
        
        List<ProductData> productDataList = productDto.getUsingBrandCategory(brandPojo.getBrandId());
        ProductForm productForm2 = TestDataUtil.getProductFormDto(brandPojo.getBrandId(), "bar123", "kitkat", 10.50);
        // update product
        ProductForm productUpdateForm = new ProductForm();
        productUpdateForm.setBarcode(productForm2.getBarcode());
        productUpdateForm.setBrandCategory(brandPojo.getBrandId());
        productUpdateForm.setMrp(productForm2.getMrp());
        productUpdateForm.setproductName(productForm2.getproductName());
        System.out.println(productDataList.size());
        productDto.update(productDataList.get(0).getProductId(), productUpdateForm);
        // get and test
        ProductData productData = productDto.get(productDataList.get(0).getProductId());

        assertEquals("kitkat", productData.getproductName());
    }

    //  Test for getting a product using barcode
    @Test
    public void testGetIdByBarcode() throws Exception {
        BrandForm brandForm = TestDataUtil.getBrandFormDto("nestLE", "DairY");
        brandDto.add(brandForm);
        BrandPojo brandPojo = brandService.getCheck("nestle", "dairy");
        ProductForm productForm = TestDataUtil.getProductFormDto(brandPojo.getBrandId(), "bar123", "munch", 10.50);
        
        System.out.println("Test Id By Barcode : " + brandPojo.getBrand());
        productDto.add(productForm);

        ProductPojo p = productDto.getIdByBarcode(productForm.getBarcode());
        assertEquals(p.getProductName(), productForm.getproductName());
    }

    //  Test for getting mrp of a product using barcode
    @Test
    public void testGetMrpByBarcode() throws Exception {
        BrandForm brandForm = TestDataUtil.getBrandFormDto("nestLE", "DairY");
        brandDto.add(brandForm);
        BrandPojo brandPojo = brandService.getCheck("nestle", "dairy");
        ProductForm productForm = TestDataUtil.getProductFormDto(brandPojo.getBrandId(), "bar123", "munch", 10.50);
        
        System.out.println("Test Mrp By Barcode : " + brandPojo.getBrand());
        productDto.add(productForm);

        Double p = productDto.getMrpByBarcode(productForm.getBarcode());
        assertEquals(p, productForm.getMrp());
    }
}
