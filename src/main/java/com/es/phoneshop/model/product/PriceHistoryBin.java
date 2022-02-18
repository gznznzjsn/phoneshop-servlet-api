package com.es.phoneshop.model.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PriceHistoryBin implements Serializable {
    private final LocalDate date;
    private final BigDecimal price;

    public PriceHistoryBin() {
        this.date = LocalDate.now();
        this.price = null;
    }

    public PriceHistoryBin(LocalDate date, BigDecimal price) {
        this.date = date;
        this.price = price;
    }


    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
