package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;

@Service
public class InventoryService {

	@Autowired
	private InventoryDao dao;

	@Transactional
	public void add(InventoryPojo p) throws Exception {
		InventoryPojo invPojo = getIdByBarcode(p.getBarcode());
		if (invPojo == null) {
			dao.insert(p);
		}
        else {
            Integer InventoryId = invPojo.getInventoryId();
            InventoryPojo inv = get(InventoryId);
            if(inv == null) {
            	dao.insert(p);
            }
            else {
            	InventoryPojo pojo = getIdByBarcode(p.getBarcode());
            	pojo.setQuantity(pojo.getQuantity() + p.getQuantity());
            	dao.update(pojo);
            }
        }
	}
	
	@Transactional
	public void addAll(List<InventoryPojo> pojo) throws Exception {
		// TODO Auto-generated method stub
		for(InventoryPojo iter:pojo) {
			add(iter);
		}
	}

	
	@Transactional(rollbackOn = ApiException.class)
	public InventoryPojo get(Integer inventoryId) throws Exception {
		return getCheck(inventoryId);
	}
	
	@Transactional
	public List<InventoryPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional(rollbackOn  = ApiException.class)
	public void update(Integer id, Integer quantity) throws Exception {
		InventoryPojo ex = getCheck(id);
		ex.setQuantity(quantity);
        dao.update(ex);
	}

	@Transactional
	public InventoryPojo getCheck(Integer inventoryId) throws Exception {
		InventoryPojo p = dao.getInventory(inventoryId);
		if (p == null) {
			throw new ApiException("Inventory with given ID does not exist, inventoryId: " + inventoryId);
		}
		return p;
	}
	
	@Transactional
	public InventoryPojo getInventoryByProductID(Integer productId) throws Exception {
		InventoryPojo p = dao.getInventoryByProductID(productId);
		if (p == null) {
			throw new ApiException("Inventory with given ID does not exist, id: " + productId);
		}
		return p;
	}
	
	@Transactional
    public InventoryPojo getIdByBarcode(String barcode) throws Exception {
        return dao.getIdByBarcode(barcode);
    }

    @Transactional
    public Integer getQuantityByBarcode(String barcode) throws Exception {
        InventoryPojo p = dao.getIdByBarcode(barcode);
        if(p==null)
            throw new ApiException("No Inventory Available");
        return p.getQuantity();
    }
    
    @Transactional
    public InventoryPojo getInventory(Integer inventoryId) throws Exception {
        InventoryPojo p = dao.getInventory(inventoryId);
        if (p == null) {
            throw new ApiException("Inventory with given id does not exist, ID: " + inventoryId);
        }
        return p;
    }
    
    @Transactional
    public InventoryPojo getInventoryPojo(Integer inventoryId) throws Exception {
        InventoryPojo p = dao.getInventory(inventoryId);
        return p;
    }
    
    @Transactional
    public InventoryPojo getCheckInventoryId(Integer id) throws Exception {
        InventoryPojo p = dao.select(id);
        if (p.getInventoryId() == null) {
            throw new ApiException("Inventory with given id does not exist, ID: " + id);
        }
        return p;
    }

    @Transactional
    public void getCheckInventoryQuantity(Integer quantity, InventoryPojo inventoryPojo, String barcode) throws Exception {
        if (quantity > inventoryPojo.getQuantity()) {
            throw new ApiException("Insufficient inventory for, Barcode: " + barcode);
        }
    }

		

}
