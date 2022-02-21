package com.es.phoneshop.generic;

import java.util.Currency;

public abstract class DaoItem {
    private Long id;
    private Currency currency;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        DaoItem daoItem = (DaoItem) obj;

        return (id == daoItem.getId() || (daoItem.getId() != null && id.equals(daoItem.getId()))) &&
                (currency == daoItem.getCurrency() || (daoItem.getCurrency() != null && currency.equals(daoItem.getCurrency())));

    }
}
