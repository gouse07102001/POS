package com.increff.pos.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.StringUtil;

@Service
public class ProductService {

	@Autowired
	private ProductDao dao;

	@Transactional
	public void add(ProductPojo p) {
		normalize(p);
		dao.insert(p);
	}
	@Transactional
	public void addAll(List<ProductPojo> pojo) {
		for(ProductPojo iter:pojo) {
			normalize(iter);
			dao.insert(iter);
		}
	}


	@Transactional(rollbackOn = ApiException.class)
	public ProductPojo get(Integer id) throws Exception {
		return getCheck(id);
	}
	
	@Transactional
    public List<ProductPojo> getUsingBrandCategory(Integer brandCategory) {
        return dao.selectByBrandCategory(brandCategory);
    }
	
	@Transactional(rollbackOn = ApiException.class)
	public ProductPojo get(String barcode) throws Exception {
		return getCheckBarcode(barcode);
	}

	@Transactional
	public List<ProductPojo> getAll() {
		return dao.selectAll();
	}
	
	@Transactional
    public ProductPojo getBarcodeById(Integer id) throws Exception {
        return dao.select(id);
    }

	@Transactional(rollbackOn  = ApiException.class)
	public void update(Integer id, ProductPojo p) throws Exception {
		normalize(p);
		ProductPojo ex = getCheck(id);
		ex.setBarcode(p.getBarcode());
		ex.setBrandCategory(p.getBrandCategory());
		ex.setProductName(p.getProductName());
		ex.setMrp(p.getMrp());
		dao.update(p);
	}
	
	@Transactional(rollbackOn = ApiException.class)
    public ProductPojo checkBarcodeSwap(String barcode) throws Exception {
		if(StringUtil.isEmpty(barcode)) {
			throw new ApiException("Barcode cannot be empty");
		}
        ProductPojo p = dao.selectByParam(barcode);
        if (p == null) {
            throw new ApiException("Barcode doesnot exist");
        }
        return p;
    }
	
	
	
	@Transactional
    public double getMrpByBarcode(String barcode) throws Exception {
        return dao.getMrpByBarcode(barcode);
    }

	@Transactional
	public ProductPojo getCheck(Integer productId) throws Exception {
		ProductPojo p = dao.select(productId);
		if (p == null) {
			throw new ApiException("Product with given ID does not exist, id: " + productId);
		}
		return p;
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public ProductPojo getCheckBarcode(String barcode) throws Exception {
		ProductPojo p = dao.selectByBarcode(barcode);
		return p;
	}

	protected static void normalize(ProductPojo p) {
		p.setProductName(p.getProductName().toLowerCase().trim());
		p.setBarcode(p.getBarcode().toLowerCase().trim());
	}

	public List<String> getAllBarcodes() {
		List<ProductPojo> list = new ArrayList<ProductPojo>();
		list = getAll();
		List<String> barcodes = new ArrayList<String>();
		for(ProductPojo iter:list) {
			barcodes.add(iter.getBarcode());
		}
		return barcodes;
		
	}
	
	@Transactional(rollbackOn = ApiException.class)
    public ProductPojo checkBarcode(String barcode) throws Exception {
        ProductPojo p = dao.selectByBarcode(barcode);
        if (p != null) {
            throw new ApiException("Barcode already exist");
        }
        return p;
    }

    @Transactional(rollbackOn = ApiException.class)
    public ProductPojo getUsingBarcode(String barcode) throws Exception {
        ProductPojo p = dao.selectByBarcode(barcode);
        if (p == null) {
            throw new ApiException("Barcode does not exist");
        }
        return p;
    }
    
    @Transactional(rollbackOn = ApiException.class)
    public ProductPojo getByBarcode(String barcode) throws Exception {
        ProductPojo p = dao.selectByBarcode(barcode);
        return p;
    }


	

}
