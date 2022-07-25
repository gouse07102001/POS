package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;

@Service
public class InventoryDto {

	@Autowired
	private InventoryService service;

	@Autowired
	ProductService productService;

	
	public void add(InventoryForm form) throws Exception {
		// TODO Auto-generated method stub
		ProductPojo p = productService.getBarcodeById(form.getProductId());
		if (Objects.isNull(p)) {
			throw new ApiException("Product Does not Found");
		}
		String barcode = p.getBarcode();
		InventoryPojo i = convert(form);
		i.setBarcode(barcode);

		if (form.getQuantity() < 0)
			throw new ApiException("Quantity should be Positive");
		if (form.getQuantity() == 0)
			throw new ApiException("Quantity should not be Zero");
		service.add(i);
	}

	
	public void addAll(List<InventoryForm> form) throws Exception {
		List<InventoryPojo> pojo = new ArrayList<InventoryPojo>();
		Integer index = 0;
		for (InventoryForm iter : form) {
			ProductPojo p = productService.getUsingBarcode(iter.getBarcode());
			if (Objects.isNull(p)) {
				throw new ApiException("Barcode Does not Found/" + index);
			}
			String barcode = iter.getBarcode();
			InventoryPojo i = convert(iter);
			i.setBarcode(barcode);
			i.setProductId(p.getProductId());
			if (iter.getQuantity() < 0)
				throw new ApiException("Quantity should be Positive/" + index);
			if (iter.getQuantity() == 0)
				throw new ApiException("Quantity should not be Zero/" + index);
			pojo.add(i);
			index += 1;
		}
		service.addAll(pojo);

	}

	public InventoryPojo get(Integer inventoryId) throws Exception {
		return service.get(inventoryId);
	}

	
	public List<InventoryData> getAll() {
		// TODO Auto-generated method stub
		List<InventoryPojo> list = service.getAll();
		List<InventoryData> list2 = new ArrayList<InventoryData>();
		for (InventoryPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}


	public void update(Integer id, InventoryForm f) throws Exception {
		// TODO Auto-generated method stub
		String s = isNotValidQuantity(f.getQuantity());
		if (s != null) {
			throw new ApiException(s);
		}
		ProductPojo p = productService.get(id);
		service.update(getIdByBarcode(p.getBarcode()), f.getQuantity());

	}

	public Integer getIdByBarcode(String barcode) throws Exception {
		InventoryPojo i = service.getIdByBarcode(barcode);
		if (i == null)
			throw new ApiException("Product Doesnot Exist");
		return i.getInventoryId();
	}

	public InventoryPojo getByBarcode(String barcode) throws Exception {
		InventoryPojo i = service.getIdByBarcode(barcode);
		if (i == null)
			throw new ApiException("Product Doesnot Exist");
		return i;
	}

	public Integer getQuantityByBarcode(String barcode) throws Exception {
		return service.getQuantityByBarcode(barcode);
	}

	private static InventoryData convert(InventoryPojo p) {
		InventoryData d = new InventoryData();
		d.setProductId(p.getProductId());
		d.setQuantity(p.getQuantity());
		d.setBarcode(p.getBarcode());
		d.setInventoryId(p.getInventoryId());
		return d;
	}

	private static InventoryPojo convert(InventoryForm f) {
		InventoryPojo p = new InventoryPojo();
		p.setProductId(f.getProductId());
		p.setQuantity(f.getQuantity());
		p.setBarcode(f.getBarcode());
		p.setProductId(f.getProductId());
		return p;
	}

	private String isNotValidQuantity(Integer quantity) {
		// TODO Auto-generated method stub
		if (quantity == null) {
			return "Quantity can't be Empty";
		}
		if (quantity == 0) {
			return "Quantity can't be Zero";
		}
		if (quantity < 0) {
			return "Quantity can't be Negative";
		}
		return null;
	}

	public Integer getIdByBarcodeForSearch(String barcode) throws Exception {
		InventoryPojo i = service.getIdByBarcode(barcode);
		if (i == null)
			throw new ApiException("Barcode Does not Exist");
		return i.getInventoryId();
	}
	
	public void update(String barcode, Integer quantity) throws Exception {

		String s = isNotValidQuantity(quantity);
		if(s != null) {
			throw new ApiException(s);
		}
        Integer id = getIdByBarcode(barcode);
        service.update(id, quantity);
    }

}
