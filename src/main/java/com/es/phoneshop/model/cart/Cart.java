package com.es.phoneshop.model.cart;

import com.es.phoneshop.generic.DaoItem;
import com.es.phoneshop.model.product.Product;

import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class Cart extends DaoItem implements Serializable {
    private List<CartItem> items;
    private int totalQuantity;
    private BigDecimal totalCost;

    public Cart() {
        setCurrency(Currency.getInstance("USD"));
        this.items = new ArrayList<>();
    }


    public List<CartItem> getItems() {
        return items;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Cart[ " + items + " ]";
    }
}
