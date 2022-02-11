package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.PriceHistoryBin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class Product {
    private Long id;
    private String code;
    private String description;
    /** null means there is no price because the product is outdated or new */
    private BigDecimal price;
    /** can be null if the price is null */
    private Currency currency;
    private int stock;
    private String imageUrl;

    private List<PriceHistoryBin> priceHistory;

    public Product() {
    }

    public Product(Long id, String code, String description, BigDecimal price, Currency currency, int stock, String imageUrl) {
        this.id = id;
        this.code = code;
        this.description = description;
        priceHistory = new ArrayList<>();
        setPrice(price);
        this.currency = currency;
        this.stock = stock;
        this.imageUrl = imageUrl;
    }

    public Product(String code, String description, BigDecimal price, Currency currency, int stock, String imageUrl) {
        this.code = code;
        this.description = description;
        priceHistory = new ArrayList<>();
        setPrice(price);
        this.currency = currency;
        this.stock = stock;
        this.imageUrl = imageUrl;
}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
        priceHistory.add(new PriceHistoryBin(LocalDate.now(),this.price));
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<PriceHistoryBin> getPriceHistory(){
        return priceHistory;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this){
            return true;
        }
        if(obj == null || obj.getClass() != this.getClass()){
            return false;
        }

        Product product = (Product) obj;
        return (id == product.getId() || (product.getId() != null && id.equals(product.getId()))) &&
        (code == product.getCode() || (product.getCode() != null && code.equals(product.getCode()))) &&
        (description == product.getDescription() || (product.getDescription() != null && description.equals(product.getDescription()))) &&
        (price == product.getPrice() || (product.getPrice() != null && price.equals(product.getPrice()))) &&
        (currency == product.getCurrency() || (product.getCurrency() != null && currency.equals(product.getCurrency()))) &&
        (stock == product.getStock()) &&
        (imageUrl == product.getImageUrl() || (product.getImageUrl() != null && imageUrl.equals(product.getImageUrl())));
    }
}