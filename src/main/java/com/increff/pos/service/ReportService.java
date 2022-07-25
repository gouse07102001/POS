package com.increff.pos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.increff.pos.dao.ReportDao;
import com.increff.pos.model.SalesReportData;
import com.increff.pos.model.SalesReportItem;

@Service
public class ReportService { 

    @Autowired
    private ReportDao dao;


    @Transactional
    public List<SalesReportData> selectByBrandCategory(String brand, String category) {
        return dao.selectByBrandCategory(brand, category);
    }

    @Transactional
    public List<SalesReportItem> selectByTime(String startDate, String endDate) {
        return dao.selectByTime(startDate, endDate);
    }


}
