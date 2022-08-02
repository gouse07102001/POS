package com.increff.pos.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.spring.AbstractUnitTest;

public class ProductServiceTest extends AbstractUnitTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;

    // test product service
    @Test
    public void testAdd() throws Exception {
        ProductPojo ProductPojo = getProductPojoTest();
        // Add one time
        productService.add(ProductPojo);
        String b1 = productService.get(ProductPojo.getProductId()).getBarcode();
        assertEquals(b1, "bar123");
    }

    //  Test for getting product using barcode
    @Test(expected = Exception.class)
    public void testGetUsingBarcode() throws Exception {
        ProductPojo ProductPojo = getProductPojoTest();
        productService.add(ProductPojo);
        ProductPojo ProductPojo2 = productService.get(ProductPojo.getBarcode());
        assertEquals(ProductPojo.getProductName(), ProductPojo2.getProductName());
        assertEquals(ProductPojo.getBrandCategory(), ProductPojo2.getBrandCategory());
        assertEquals(ProductPojo.getMrp(), ProductPojo2.getMrp(), 0.01);
        // throw exception while getting data for unavailable barcode
        productService.getUsingBarcode("barcode");
    }

    //  Test for getting product by barcode
    @Test(expected = Exception.class)
    public void testGetByBarcodeBlank() throws Exception {
        ProductPojo p = getProductPojoTest();
        productService.add(p);
        // throw exception
        productService.getUsingBarcode("");
    }

    //  Test for getting product
    @Test
    public void testGet() throws Exception {
        ProductPojo p = getProductPojoTest();
        productService.add(p);
        ProductPojo pr = productService.get(p.getProductId());
        // test added data
        assertEquals("munch", pr.getProductName());
    }

    //  Test for getting product by barcode
    @Test
    public void testGetIdByBarcode() throws Exception {
        ProductPojo p = getProductPojoTest();
        productService.add(p);
        ProductPojo pr = productService.getUsingBarcode(p.getBarcode());
        // test added data
        assertEquals("bar123", pr.getBarcode());
    }

    //  Test for getting all the products
    @Test
    public void testGetAll() throws Exception {
        ProductPojo p = getProductPojoTest();
        productService.add(p);
        List<ProductPojo> ProductPojos = productService.getAll();
        assertEquals(1, ProductPojos.size());
    }

    //  Test for updating products
    @Test
    public void testUpdate() throws Exception {
        ProductPojo ProductPojo = getProductPojoTest();
        productService.add(ProductPojo);
        ProductPojo ProductPojoFinal = productService.get(ProductPojo.getProductId());
        // update data
        ProductPojoFinal.setProductName("check");
        productService.update(ProductPojoFinal.getProductId(), ProductPojoFinal);
        ProductPojo pm = productService.get(ProductPojoFinal.getProductId());
        // test updated data with normalization
        assertEquals("check", pm.getProductName());
    }


    //  Test for checking product existence
    @Test(expected = Exception.class)
    public void testGetCheck() throws Exception {
        ProductPojo ProductPojo = getProductPojoTest();
        productService.add(ProductPojo);

        ProductPojo ProductPojo2 = productService.get(ProductPojo.getProductId());
        assertEquals(ProductPojo.getProductName(), ProductPojo2.getProductName());
        assertEquals(ProductPojo.getBrandCategory(), ProductPojo2.getBrandCategory());
        assertEquals(ProductPojo.getMrp(), ProductPojo2.getMrp(), 0.01);

        // throw exception while getting data for id+1
        productService.get(ProductPojo2.getProductId() + 1);

    }

    private ProductPojo getProductPojoTest() throws Exception {
        ProductPojo p = new ProductPojo();
        BrandPojo b = new BrandPojo();
        // create data
        String barcode = "bar123";
        b.setBrand("nestle");
        b.setCategory("dairy");
        brandService.add(b);
        double mrp = 10.25;
        p.setBarcode(barcode);
        p.setBrandCategory(b.getBrandId());
        p.setProductName("munch");
        p.setMrp(mrp);
        return p;
    }
}
