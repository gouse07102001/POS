package com.increff.pos.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.spring.AbstractUnitTest;

public class InventoryServiceTest extends AbstractUnitTest {
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private BrandService brandService;

    @Autowired
    private ProductService productService;

    // test Inventory Service
    @Test
    public void testAdd() throws Exception {
        InventoryPojo inventoryPojo = getInventoryPojoTest();
        // Add
        inventoryService.add(inventoryPojo);
        InventoryPojo inventoryPojoFinal = inventoryService.get(inventoryPojo.getInventoryId());
        assertEquals(inventoryPojo.getQuantity(), inventoryPojoFinal.getQuantity());
    }

    //  Test for getting an inventory
    @Test
    public void testGet() throws Exception {
        InventoryPojo inventoryPojo = getInventoryPojoTest();
        inventoryService.add(inventoryPojo);
        InventoryPojo ip = inventoryService.get(inventoryPojo.getInventoryId());
        // test for same quantity
        assertEquals(inventoryPojo.getQuantity(), ip.getQuantity());
    }

    //  Test for getting all the inventory
    @Test
    public void testGetAll() throws Exception {
        InventoryPojo inventoryPojo = getInventoryPojoTest();
        inventoryService.add(inventoryPojo);
        // test get all function
        List<InventoryPojo> inventoryPojos = inventoryService.getAll();
        assertEquals(1, inventoryPojos.size());
    }

    //  Test for updating an inventory
    @Test
    public void testUpdate() throws Exception {
        InventoryPojo inventoryPojo = getInventoryPojoTest();
        inventoryService.add(inventoryPojo);
        InventoryPojo ip = inventoryService.get(inventoryPojo.getInventoryId());
        int newQuantity = 20;
        ip.setQuantity(newQuantity);
        // update data
        inventoryService.update(ip.getInventoryId(), ip.getQuantity());
        InventoryPojo inventoryPojoFinal = inventoryService.get(ip.getInventoryId());
        // test for updated data
        assertEquals((int) Integer.valueOf(newQuantity), (int) inventoryPojoFinal.getQuantity());
    }

    //  Test for checking inventory using id
    @Test(expected = ApiException.class)
    public void testGetCheckInventoryId() throws Exception {
        InventoryPojo inventoryPojo = getInventoryPojoTest();
        inventoryService.add(inventoryPojo);
        // select data for given id
        InventoryPojo ip = inventoryService.getCheckInventoryId(inventoryPojo.getInventoryId());
        assertEquals(inventoryPojo.getInventoryId(), ip.getInventoryId());
        // Throw exception
        inventoryService.getCheckInventoryId(ip.getInventoryId() + 1);
    }

    //  Test for getting id by barcode
    @Test
    public void testGetIdByBarcode() throws Exception {
        InventoryPojo inventoryPojo = getInventoryPojoTest();
        // Add
        inventoryService.add(inventoryPojo);
        Integer id = inventoryService.getIdByBarcode(inventoryPojo.getBarcode()).getInventoryId();
        InventoryPojo inventoryPojoFinal = inventoryService.get(id);
        assertEquals(inventoryPojo.getQuantity(), inventoryPojoFinal.getQuantity());
        assertEquals(inventoryPojo.getBarcode(), inventoryPojoFinal.getBarcode());
        assertEquals(inventoryPojo.getProductId(), inventoryPojoFinal.getProductId());
    }

    //  Test for get inventory
    @Test
    public void testGetInventory() throws Exception {
        InventoryPojo inventoryPojo = getInventoryPojoTest();
        // Add
        inventoryService.add(inventoryPojo);
        Integer id = inventoryService.getIdByBarcode(inventoryPojo.getBarcode()).getInventoryId();

        InventoryPojo inventoryPojoFinal = inventoryService.getInventory(id);
        System.out.println(inventoryPojo.getQuantity() + " " + inventoryPojoFinal.getQuantity());
        assertEquals(inventoryPojo.getQuantity(), inventoryPojoFinal.getQuantity());

    }

    private InventoryPojo getInventoryPojoTest() throws Exception {
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
