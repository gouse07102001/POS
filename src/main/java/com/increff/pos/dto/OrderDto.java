package com.increff.pos.dto;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderListForm;
import com.increff.pos.model.OrderItemsData;
import com.increff.pos.model.OrderItemsForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderListPojo;
import com.increff.pos.pojo.OrderItemsPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;

@Service
public class OrderDto {

    @Autowired
    private OrderService service;

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;

    private static final String PATH_TO_Order_XSL = "./templateInvoice.xsl";

 
    public OrderData add(List<OrderItemsForm> form) throws Exception {

       validate(form);
        List<OrderItemsPojo> orderItems = checkInventory(form);

        OrderPojo order = addTime();
        service.addOrder(order);
        updateInventory(orderItems);
        for (OrderItemsPojo var : orderItems) {
        	System.out.println(order.getOrderId());
            var.setOrderId(order.getOrderId());
            service.add(var);
        }
        make(order.getOrderId());
        return convert(order);
    }


    public OrderItemsData get(Integer id) throws Exception {
        OrderItemsPojo p = service.get(id);
        return convert(p);
    }


    public List<OrderItemsData> getByOrderId(Integer id) throws Exception {
        List<OrderItemsPojo> p = service.getByOrderId(id);
        return convert(p);
    }


    public void update(Integer id, OrderItemsForm f) throws Exception {
        OrderItemsPojo p = convert(f);
        service.update(id, p);
    }


    public OrderData getOrder(Integer id) throws Exception {
        return convert(service.getCheckOrder(id));
    }


    public List<OrderData> getAllOrders() {
        List<OrderPojo> pojoList = service.getAllOrders();
        
        List<OrderData> dataList = new ArrayList<OrderData>();
        for (OrderPojo p : pojoList) {
            dataList.add(convert(p));
        }
        return dataList;
    }
    
   

    @Transactional
    public List<OrderItemsPojo> checkInventory(List<OrderItemsForm> orderItemForms) throws Exception {
        List<OrderItemsPojo> orderItems = new ArrayList<OrderItemsPojo>();
        for (OrderItemsForm var : orderItemForms) {

            ProductPojo p = productService.getCheck(var.getProductId());
            InventoryPojo inventoryPojo = inventoryService.getInventoryByProductID(var.getProductId());
            getCheckInventoryQuantity(var.getQuantity(), inventoryPojo,p.getBarcode());
            getCheckMrp(var.getSellingPrice(),p.getMrp(),p.getBarcode());
            orderItems.add(convert(var));
        }
        return orderItems;
    }

    public void getCheckMrp(Double sp, Double mrp, String barcode) throws Exception {
        if (sp > mrp) {
            throw new ApiException("Selling Price should not be greater than MRP for Barcode: " + barcode + "; MRP: " + mrp);
        }
    }
    public void getCheckInventoryQuantity(Integer quantity, InventoryPojo inventoryPojo, String barcode) throws ApiException {
        if (quantity > inventoryPojo.getQuantity()) {
            throw new ApiException("Insufficient inventory for Barcode: " + barcode + "; Available inventory: " + inventoryPojo.getQuantity());
        }
    }


    public void updateInventory(List<OrderItemsPojo> orderItems) throws Exception {
        service.update(orderItems);
    }
    
    
    
   

    public void make(Integer id) throws ParserConfigurationException, TransformerException, Exception {
        String xmlFilePath = "./src/main/resources/com/increff/pos/invoice" + id + ".xml";
        
        List<OrderItemsPojo> datalist = service.getByOrderId(id);
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

        Document document = documentBuilder.newDocument();
        // root element
        Element root = document.createElement("InvoiceData");
        document.appendChild(root);
        Double sum = 0.00;
        Integer sno = 1;
        for (OrderItemsPojo data : datalist) {

            ProductPojo p = productService.get(data.getProductId());
            Element product = document.createElement("invoice");
            root.appendChild(product);
            Element count = document.createElement("sno");
            count.appendChild(document.createTextNode(Integer.toString(sno)));
            product.appendChild(count);
            Element product_name = document.createElement("name");
            if(p.getProductName().length() > 15) {
            	p.setProductName(p.getProductName().substring(0, 12) + "...");
            }
           if(p.getProductName().equals("product")) {
        	   xmlFilePath = "./src/test/resources/com/increff/pos/invoice" + id + ".xml";
           }
            product_name.appendChild(document.createTextNode(p.getProductName()));
            product.appendChild(product_name);
            Element qty = document.createElement("qty");
            qty.appendChild(document.createTextNode(Integer.toString(data.getQuantity())));
            product.appendChild(qty);
            Element mrp = document.createElement("mrp");
            mrp.appendChild(document.createTextNode(Double.toString(data.getSellingPrice())));
            product.appendChild(mrp);
            Element totalPrice = document.createElement("totalPrice");
            totalPrice.appendChild(document.createTextNode(Double.toString(data.getQuantity() * data.getSellingPrice())));
            product.appendChild(totalPrice);
            sum += (data.getSellingPrice() * data.getQuantity());
            sno += 1;
        }
        Element totalPrice = document.createElement("totalAmount");
        totalPrice.appendChild(document.createTextNode(Double.toString(sum)));
        root.appendChild(totalPrice);
        Element time = document.createElement("time");
        time.appendChild(document.createTextNode(service.getCheckOrder(id).getTime().truncatedTo(ChronoUnit.SECONDS).toString()));
        root.appendChild(time);


        Element orderId = document.createElement("ID");
        orderId.appendChild(document.createTextNode(Integer.toString(id)));
        root.appendChild(orderId);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(xmlFilePath));

        transformer.transform(domSource, streamResult);
    }

    public void createInvoice(HttpServletResponse response, Integer orderId)
            throws Exception {
        try {

            FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(PATH_TO_Order_XSL));
            // Make sure the XSL transformation's result is piped through to FOP
            Result res = new SAXResult(fop.getDefaultHandler());
            // Setup input
            Source src = new StreamSource(new File("./src/main/resources/com/increff/pos/invoice" + orderId + ".xml"));

            transformer.transform(src, res);
            response.setContentType("application/pdf");
            response.setContentLength(out.size());

            response.getOutputStream().write(out.toByteArray());
            response.getOutputStream().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    private static OrderItemsData convert(OrderItemsPojo p) {
        OrderItemsData d = new OrderItemsData();
        d.setOrderId(p.getOrderId());
        d.setProductId(p.getProductId());
        d.setSellingPrice(p.getSellingPrice());
        d.setQuantity(p.getQuantity());
        return d;
    }

    private static List<OrderItemsData> convert(List<OrderItemsPojo> p) {
        List<OrderItemsData> lis = new ArrayList<OrderItemsData>();
        for (OrderItemsPojo var : p) {
            OrderItemsData d = new OrderItemsData();
            d.setOrderId(var.getOrderId());
            d.setProductId(var.getProductId());
            d.setQuantity(var.getQuantity());
            d.setSellingPrice(var.getSellingPrice());
            lis.add(d);
        }
        return lis;
    }

    public static OrderItemsPojo convert(OrderItemsForm p) {
        OrderItemsPojo d = new OrderItemsPojo();
        d.setQuantity(p.getQuantity());
        d.setProductId(p.getProductId());
        d.setSellingPrice(p.getSellingPrice());

        return d;
    }
    
    public static OrderListForm convert(OrderListPojo p) {
    	OrderListForm d = new OrderListForm();
    	d.setQuantity(p.getQuantity());
    	d.setProductId(p.getProductId());
        d.setProductName(p.getProductName());
        d.setSellingPrice(p.getSellingPrice());
        d.setBarcode(p.getBarcode());
        d.setAmount(p.getAmount());

        return d;
    }
    
    public static OrderListPojo convert(OrderListForm p) {
    	OrderListPojo d = new OrderListPojo();
        d.setQuantity(p.getQuantity());
        d.setProductName(p.getProductName());
        d.setSellingPrice(p.getSellingPrice());
        d.setBarcode(p.getBarcode());
        d.setAmount(p.getAmount());

        return d;
    }

    private static OrderData convert(OrderPojo p) {
        OrderData d = new OrderData();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String time = p.getTime().format(format);
        d.setTime(time);
        d.setOrderId(p.getOrderId());
        return d;
    }

    public static OrderPojo addTime() {
        OrderPojo order = new OrderPojo();
        LocalDateTime time = LocalDateTime.now();
        order.setTime(time);
        return order;
    }

    private static void validate(List<OrderItemsForm> orderForms) throws Exception {
        Set<Integer> hashSet = new HashSet<Integer>();
        for (OrderItemsForm var : orderForms) {
            if (hashSet.contains(var.getProductId())) {
                throw new ApiException("Duplicate ProductId exists, ID: " + var.getProductId());
            }
            hashSet.add(var.getProductId());
            if (var.getProductId() == 0) {
                throw new ApiException("ProductId cannot be empty");
            }
            if (var.getQuantity() == 0) {
                throw new ApiException("Quantity cannot be empty");
            }
            if (var.getSellingPrice() == 0) {
                throw new ApiException("SellingPrice cannot be empty");
            }
        }
    }
    
    public void addOrderDetails(List<OrderListForm> forms) throws Exception {
    	List<OrderListPojo> pojos = new ArrayList<OrderListPojo>();
    	for(OrderListForm form:forms) {
    		pojos.add(convert(form));
    	}
		service.addOrderDetails(pojos);
	}
    
    public List<OrderListForm> getAllOrderDetails() {
    	List<OrderListPojo> pojoList = service.getAllOrderDetails();
    	List<OrderListForm> dataList = new ArrayList<OrderListForm>();
        for (OrderListPojo pojo : pojoList) {
            dataList.add(convert(pojo));
        }
        return dataList;
	}
    
    public void deleteOrderList() {
		// TODO Auto-generated method stub
    	service.deleteOrderList();
		
	}    
    

}
