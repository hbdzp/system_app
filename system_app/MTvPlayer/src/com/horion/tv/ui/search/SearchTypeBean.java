package com.horion.tv.ui.search;

/**
 * Created by xiacf on 2017/1/9.
 */

public class SearchTypeBean {
    private int itemType;
    private String searchType;
    private String searchTypeValue;
    private String[] searchTypeValueArr;


    public String[] getSearchTypeValueArr() {
        return searchTypeValueArr;
    }

    public void setSearchTypeValueArr(String[] searchTypeValueArr) {
        this.searchTypeValueArr = searchTypeValueArr;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getSearchTypeValue() {
        return searchTypeValue;
    }

    public void setSearchTypeValue(String searchTypeValue) {
        this.searchTypeValue = searchTypeValue;
    }
}
