package com.increff.pos.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.model.SalesReportData;
import com.increff.pos.model.SalesReportItem;
import com.increff.pos.util.StringUtil;

@Repository
public class ReportDao extends AbstractDao {

    public static String SELECT_ORDERS_BY_TIME = "select NEW com.increff.pos.model.SalesReportItem(r.brandCategory, sum(q.quantity), sum(q.quantity*q.sellingPrice)) from OrderPojo p, OrderItemsPojo q, ProductPojo r where p.orderId = q.orderId and q.productId = r.productId and p.time >= :startTime and p.time <= :endTime group by r.brandCategory";
    public static String SELECT_ALL_BRANDS = "select NEW com.increff.pos.model.SalesReportData(b.brandId, b.brand, b.category) from BrandPojo b";
    public static String SELECT_BY_BRAND = "select NEW com.increff.pos.model.SalesReportData(b.brandId, b.brand, b.category) from BrandPojo b where b.brand = :brand";
    public static String SELECT_BY_CATEGORY = "select NEW com.increff.pos.model.SalesReportData(b.brandId, b.brand, b.category) from BrandPojo b where b.category = :category";
    public static String SELECT_BY_BRAND_CATEGORY = "select NEW com.increff.pos.model.SalesReportData(b.brandId, b.brand, b.category) from BrandPojo b where b.brand = :brand and b.category = :category";

    @PersistenceContext
    private EntityManager em;

    public List<SalesReportItem> selectByTime(String startDate, String endDate) {
        TypedQuery<SalesReportItem> query = em.createQuery(SELECT_ORDERS_BY_TIME, SalesReportItem.class);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime sTime = LocalDate.parse(startDate, formatter).atStartOfDay();
        LocalDateTime eTime = LocalDate.parse(endDate, formatter).atTime(23, 59);
        query.setParameter("startTime", sTime);
        query.setParameter("endTime", eTime);
        return query.getResultList();
    }

    public List<SalesReportData> selectByBrandCategory(String brand, String category) { 

        TypedQuery<SalesReportData> query;

        if (StringUtil.isEmpty(brand) && StringUtil.isEmpty(category)) {
            query = em.createQuery(SELECT_ALL_BRANDS, SalesReportData.class);
        } else if (StringUtil.isEmpty(brand)) {
            query = em.createQuery(SELECT_BY_CATEGORY, SalesReportData.class);
            query.setParameter("category", category);
        } else if (StringUtil.isEmpty(category)) {
            query = em.createQuery(SELECT_BY_BRAND, SalesReportData.class);
            query.setParameter("brand", brand);
        } else {
            query = em.createQuery(SELECT_BY_BRAND_CATEGORY, SalesReportData.class);
            query.setParameter("brand", brand);
            query.setParameter("category", category);
        }

        return query.getResultList();
    }
}
