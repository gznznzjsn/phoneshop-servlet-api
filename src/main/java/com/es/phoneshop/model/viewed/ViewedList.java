package com.es.phoneshop.model.viewed;

import com.es.phoneshop.model.product.Product;

import java.util.ArrayList;
import java.util.List;

public class ViewedList {
    public static final int CONST_MAX_SIZE = 3;

    private List<Product> previousVersionList;
    private List<Product> currentVersionList;


    public ViewedList() {
        this.previousVersionList = new ArrayList<>();
        this.currentVersionList = new ArrayList<>();
    }

    public List<Product> getPreviousVersionList() {
        return previousVersionList;
    }

    public List<Product> getCurrentVersionList() {
        return currentVersionList;
    }

    public void setPreviousVersionList(List<Product> previousVersionList) {
        this.previousVersionList = previousVersionList;
    }

    public void setCurrentVersionList(List<Product> currentVersionList) {
        this.currentVersionList = currentVersionList;
    }

    public boolean isAnyPreviousVersionList(){
        return previousVersionList.size() != 0;
    }

    public boolean isAnyCurrentVersionList(){
        return currentVersionList.size() != 0;
    }
}
