package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;

public class InventoryDtoTest extends AbstractUnitTest {
    @Autowired
    private InventoryDto inventoryDto;


    @Autowired
    private BrandService brandService;

    @Autowired
    private InventoryService inventoryService;


    @Autowired
    private ProductService productService;

    //  Test for adding an inventory
    @Test
    public void testAdd() throws Exception {
        InventoryForm inventoryForm = getInventoryFormTest();

        inventoryDto.add(inventoryForm);
        Integer inventoryId = inventoryDto.getIdByBarcodeForSearch(inventoryForm.getBarcode());
        InventoryPojo inventoryPojoFinal = inventoryDto.get(inventoryId);
        assertEquals(inventoryForm.getQuantity(), inventoryPojoFinal.getQuantity());
    }

    //  Test for updating an inventory
    @Test
    public void testUpdate() throws Exception {
        InventoryForm inventoryForm = getInventoryFormTest();
        inventoryDto.add(inventoryForm);
        inventoryForm.setQuantity(5);
        inventoryDto.update(inventoryForm.getBarcode(), inventoryForm.getQuantity());
        InventoryPojo p = inventoryDto.getByBarcode(inventoryForm.getBarcode());
        assertEquals(inventoryForm.getQuantity(), p.getQuantity());
    }

    //  Test for getting all the inventory
    @Test
    public void testGetAll() throws Exception {
        InventoryPojo inventoryPojo = getInventoryPojoTest();
        inventoryService.add(inventoryPojo);
        // test get all function
        List<InventoryData> inventoryPojos = inventoryDto.getAll();
        assertEquals(1, inventoryPojos.size());
    }

    //  Test for getting inventory by barcode
    @Test
    public void testGetIdByBarcode() throws Exception {
        InventoryForm inventoryForm = getInventoryFormTest();
        // Add
        inventoryDto.add(inventoryForm);
        Integer id = inventoryDto.getIdByBarcodeForSearch(inventoryForm.getBarcode());
        InventoryPojo inventoryPojoFinal = inventoryDto.get(id);
        assertEquals(inventoryForm.getQuantity(), inventoryPojoFinal.getQuantity());
        assertEquals(inventoryForm.getBarcode(), inventoryPojoFinal.getBarcode());
        assertEquals(inventoryForm.getProductId(), inventoryPojoFinal.getProductId());
    }

    //  Test for getting an Id of inventory from barcode
    @Test
    public void testGetIdFromBarcode() throws Exception {
        InventoryForm inventoryForm = getInventoryFormTest();
        // Add
        inventoryDto.add(inventoryForm);
        Integer inventoryId = inventoryDto.getIdByBarcode(inventoryForm.getBarcode());
        InventoryPojo inventoryPojoFinal = inventoryDto.get(inventoryId);
        assertEquals(inventoryForm.getQuantity(), inventoryPojoFinal.getQuantity());
        assertEquals(inventoryForm.getBarcode(), inventoryPojoFinal.getBarcode());
        assertEquals(inventoryForm.getProductId(), inventoryPojoFinal.getProductId());
    }


    //  Test for getting quantity from barcode
    @Test
    public void testQuantityIdByBarcode() throws Exception {
        InventoryForm inventoryForm = getInventoryFormTest();
        // Add
        inventoryDto.add(inventoryForm);
        Integer quantity = inventoryDto.getQuantityByBarcode(inventoryForm.getBarcode());
        assertEquals(inventoryForm.getQuantity(), quantity);
    }

    //  Test for getting inventory using barcode
    @Test
    public void testGetByBarcode() throws Exception {
        InventoryForm inventoryForm = getInventoryFormTest();
        // Add
        inventoryDto.add(inventoryForm);
        InventoryPojo pojo = inventoryDto.getByBarcode(inventoryForm.getBarcode());
        assertEquals(inventoryForm.getQuantity(), pojo.getQuantity());
    }


    private InventoryForm getInventoryFormTest() throws ApiException {
        InventoryForm i = new InventoryForm();
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
