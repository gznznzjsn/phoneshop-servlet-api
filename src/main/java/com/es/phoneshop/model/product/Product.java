package com.es.phoneshop.model.product;

import com.es.phoneshop.generic.DaoItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class Product extends DaoItem implements Serializable {
    private String code;
    private String description;
    private BigDecimal price;
    private int stock;
    private String imageUrl;

    private List<PriceHistoryBin> priceHistory;

    public Product() {
    }

    public Product(Long id, String code, String description, BigDecimal price, Currency currency, int stock, String imageUrl) {
        this.code = code;
        this.description = description;
        priceHistory = new ArrayList<>();
        setPrice(price);
        setCurrency(currency);
        this.stock = stock;
        this.imageUrl = imageUrl;
    }

    public Product(String code, String description, BigDecimal price, Currency currency, int stock, String imageUrl) {
        this.code = code;
        this.description = description;
        priceHistory = new ArrayList<>();
        setPrice(price);
        setCurrency(currency);
        this.stock = stock;
        this.imageUrl = imageUrl;
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
        priceHistory.add(new PriceHistoryBin(LocalDate.now(), this.price));
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

    public List<PriceHistoryBin> getPriceHistory() {
        return priceHistory;
    }

    @Override
    public boolean equals(Object obj) {
        if(!super.equals(obj)){
            return false;
        }
        Product product = (Product) obj;
        return (code == product.getCode() || (product.getCode() != null && code.equals(product.getCode()))) &&
                (description == product.getDescription() || (product.getDescription() != null && description.equals(product.getDescription()))) &&
                (price == product.getPrice() || (product.getPrice() != null && price.equals(product.getPrice()))) &&
                (stock == product.getStock()) &&
                (imageUrl == product.getImageUrl() || (product.getImageUrl() != null && imageUrl.equals(product.getImageUrl())));
    }
}