package com.increff.pos.dto;

import com.increff.pos.model.*;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.*;
import com.increff.pos.util.TestDataUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ReportDtoTest extends AbstractUnitTest {

    @Autowired
    private OrderDto dto;

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private BrandDto brandDto;

    @Autowired
    private BrandService brandService;

    @Autowired
    private ReportDto reportDto;

    @Autowired
    private InventoryDto inventoryDto;

    //  Test for getting sales report
    @Test
    public void testGetSalesReport() throws Exception {
        OrderItemsForm o = getInventoryFormTestOrder();
        List<OrderItemsForm> orders = new ArrayList<OrderItemsForm>();
        orders.add(o);
        OrderData ord = dto.add(orders);
        SalesReportForm form = new SalesReportForm();
        form.setBrand("nestle");
        form.setCategory("dairy");
        form.setEndDate("2022-07-30");
        form.setStartDate("2022-07-18");
        List<SalesDataList> sales = reportDto.getSalesReport(form);

        assertEquals(sales.get(0).getBrand(), form.getBrand());
    }


    //  Test for getting brand report
    @Test
    public void testGetBrandReport() throws Exception {
        BrandForm brandForm = TestDataUtil.getBrandFormDto("nestLE", "DairY");
        brandDto.add(brandForm);
        List<BrandForm> sales = reportDto.getBrandReport();

        assertEquals(sales.get(0).getBrand(), "nestle");
    }

    //  Test for getting inventory reports
    @Test
    public void testGetInventoryReport() throws Exception {
        InventoryForm inventoryForm = getInventoryFormTest();
        inventoryDto.add(inventoryForm);
        List<InventoryReportData> inventory = reportDto.getInventoryReport();

        assertEquals(inventory.get(0).getQuantity().intValue(), 10);
    }


    private OrderItemsForm getInventoryFormTestOrder() throws Exception {
        OrderItemsForm o = new OrderItemsForm();
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
        i.setQuantity(quantity);
        inventoryService.add(i);
        o.setProductId(p.getProductId());
        o.setQuantity(5);
        o.setSellingPrice(5.00);
        return o;
    }

    private InventoryForm getInventoryFormTest() throws Exception {
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


}
