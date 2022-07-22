package com.increff.pos.dao;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductService;

public class InventoryDaoTest extends AbstractUnitTest {
    @Autowired
    private InventoryDao inventoryDao;


    @Autowired
    private BrandService brandService;


    @Autowired
    private ProductService productService;

    // Test for updating an inventory
    @Test
    public void testUpdate() throws Exception {
        InventoryPojo inventoryPojo = getInventoryPojoTest();
        // Add
        inventoryDao.insert(inventoryPojo);
        inventoryPojo.setQuantity(5);
        inventoryDao.update(inventoryPojo.getInventoryId(), inventoryPojo.getQuantity());
        InventoryPojo p = inventoryDao.getIdByBarcode(inventoryPojo.getBarcode());
        assertEquals(inventoryPojo.getQuantity(), p.getQuantity());
    }

    // Test for getting an inventory
    private InventoryPojo getInventoryPojoTest() throws ApiException {
        InventoryPojo i = new InventoryPojo();
        // create data
        String barcode = "bar123";
        BrandPojo b = new BrandPojo();
        ProductPojo p = new ProductPojo();
        b.setBrand("nestle");
        b.setCategory("dairy");
        brandService.add(b);
        //System.out.println("Invnetory Test getInvPojo rand" + b.getId());
        double mrp = 10.25;
        p.setBarcode(barcode);
        p.setBrandCategory(b.getBrandId());
        p.setProductName("ProDuct");
        p.setMrp(mrp);
        productService.add(p);
        int quantity = 10;
        i.setProductId(p.getProductId());
        i.setBarcode(barcode);

        //System.out.println("Invnetory Test getInvPojo pro" + p.getProduct_id());
        i.setQuantity(quantity);
        return i;
    }
}
