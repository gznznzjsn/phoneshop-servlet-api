package com.es.phoneshop.web.servlet;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class AdvancedSearchPageServlet extends HttpServlet {
    private ProductDao productDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> errors = new HashMap<>();
        String productCode = setProductCode(request,errors);
        BigDecimal minPrice = setMinPrice(request,errors);
        BigDecimal maxPrice = setMaxPrice(request,errors);
        int minStock= setMinStock(request,errors);
        boolean firstVisit = Boolean.parseBoolean(request.getParameter("firstVisit"));

        if (!errors.isEmpty() && !firstVisit) {
            request.setAttribute("errors", errors);
        } else {
            request.setAttribute("success", productDao.findAdvancedProducts(productCode,minPrice,maxPrice,minStock).size() +" products found successfully");
        }
        request.setAttribute("products", productDao.findAdvancedProducts(productCode, minPrice, maxPrice, minStock));
        request.getRequestDispatcher("/WEB-INF/pages/advancedSearch.jsp").forward(request,response);
    }

    private String setProductCode(HttpServletRequest request, Map<String, String> errors) {
        String value = request.getParameter("productCode");
        if (value == null || value.trim().isEmpty()) {
            errors.put("productCode", "Value is required");
            return "";
        }
            return value;
    }

    private BigDecimal setMinPrice(HttpServletRequest request, Map<String, String> errors) {
        String value = request.getParameter("minPrice");
        if (value == null || value.trim().isEmpty()) {
            errors.put("minPrice", "Value is required");
            return BigDecimal.ZERO;
        }
        if(!value.matches("[0-9]+")){
            errors.put("minPrice", "Not a number");
            return BigDecimal.ZERO;
        }
            return new BigDecimal(value);
    }

    private BigDecimal setMaxPrice(HttpServletRequest request, Map<String, String> errors) {
        String value = request.getParameter("maxPrice");
        if (value == null || value.trim().isEmpty()) {
            errors.put("maxPrice", "Value is required");
            return BigDecimal.valueOf(Long.MAX_VALUE);
        }
        if(!value.matches("[0-9]+")){
            errors.put("maxPrice", "Not a number");
            return BigDecimal.valueOf(Long.MAX_VALUE);
        }
        return new BigDecimal(value);
    }
    private int setMinStock(HttpServletRequest request, Map<String, String> errors) {
        String value = request.getParameter("minStock");
        if (value == null || value.trim().isEmpty()) {
            errors.put("minStock", "Value is required");
            return 0;
        }
        if(!value.matches("[0-9]+")){
            errors.put("minStock", "Not a number");
            return 0;
        }
        return Integer.parseInt(value);
    }


}
