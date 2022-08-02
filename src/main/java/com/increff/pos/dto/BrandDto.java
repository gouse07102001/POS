package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
@Service
public class BrandDto {
	
	@Autowired
	private BrandService service;
	

	public void add(BrandForm form)throws Exception {
		// TODO Auto-generated method stub
		
		String s = isNull(form);
		if(s != null) {
			throw new ApiException(s);
		}
		if(!isValid(form,-1)) {
			BrandPojo d = convert(form);
			service.add(d);
		}
		else {
			throw new ApiException("Brand Category pair already exist");	
		}
	}

	public void addAll(List<BrandForm> form) throws Exception {
		List<BrandPojo> pojo = new ArrayList<BrandPojo>();
		Integer index = 0;
		for(BrandForm iter:form) {
			String s = isNull(iter);
			if(s != null) {
				throw new ApiException(s +"/"+index);
			}
			if(isValid(iter,-1)) {
				throw new ApiException("Brand Category pair already exist /"+index);	
			}
			index += 1;
		}
		for(BrandForm iter:form) {
			pojo.add(convert(iter));
		}
		service.addAll(pojo);
	}
	

	public BrandData get(Integer brandId) throws Exception {
		BrandPojo d = service.get(brandId);
		return convert(d);
	}

	
	public List<BrandData> getAll() {
		List<BrandPojo> list = service.getAll();
		List<BrandData> list_brandData = new ArrayList<BrandData>();
		for(BrandPojo i:list)
		{
			list_brandData.add(convert(i));
		}
		return list_brandData;
	}
	
	public void update(Integer brandId, BrandForm form) throws Exception {
		// TODO Auto-generated method stub
		
		String s = isNull(form);
		if(s != null) {
			throw new ApiException(s);
		}
		else if(!isValid(form,brandId)) {
			BrandPojo d = convert(form);
			service.update(brandId, d);
		}
		else {
			throw new ApiException("Brand Category pair already exist");	
		}
		
	}
	
	private static BrandData convert(BrandPojo p) {
		BrandData d = new BrandData();
		d.setBrand(p.getBrand());
		d.setCategory(p.getCategory());
		d.setBrandId(p.getBrandId());
		return d;
	}

	private static BrandPojo convert(BrandForm f) {
		BrandPojo p = new BrandPojo();
		p.setBrand(f.getBrand().toLowerCase().trim());
		p.setCategory(f.getCategory().toLowerCase().trim());
		return p;
	}
	
	
	private boolean isValid(BrandForm d,Integer brandId) {
		List<BrandPojo> p = service.getAll();
		d.setBrand(d.getBrand().trim());
		d.setCategory(d.getCategory().trim());
		for(BrandPojo i:p)
		{
			if((i.getBrand().equalsIgnoreCase(d.getBrand())) && (i.getCategory().equalsIgnoreCase(d.getCategory())) && (brandId != i.getBrandId()))
				return true;
		}
		return false;
	}
	
	private String isNull(BrandForm d) {
		if(d.getBrand()==null || d.getBrand().isEmpty()) {
			return "Brand Can't be empty";
		}
		else if(d.getCategory() == null || d.getCategory().isEmpty())
			return "Category Can't be empty";
		else if(d.getBrand().length()>256)
			return "Maximum limit exceeded for Brand";
		else if(d.getCategory().length()>256)
			return "Maximum limit exceeded for Category";
		return null;
	}

	public List<String> getAllBrandCategory() {
		return service.getAllBrandCategory();
	}



	

	

	
}
