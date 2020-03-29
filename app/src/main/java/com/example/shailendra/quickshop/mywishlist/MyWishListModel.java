package com.example.shailendra.quickshop.mywishlist;

public class MyWishListModel {

    private String wishImage;
    private String wishName;
    private String wishPrice;
    private String wishCutPrice;
    private String wishListProductId;
    private String wishStockInfo;
    private boolean wishCODinfo;

    public MyWishListModel(String wishImage, String wishName, String wishPrice, String wishCutPrice, String wishListProductId, String wishStockInfo, boolean wishCODinfo) {
        this.wishImage = wishImage;
        this.wishName = wishName;
        this.wishPrice = wishPrice;
        this.wishCutPrice = wishCutPrice;
        this.wishListProductId = wishListProductId;
        this.wishStockInfo = wishStockInfo;
        this.wishCODinfo = wishCODinfo;
    }

    public String getWishImage() {
        return wishImage;
    }

    public void setWishImage(String wishImage) {
        this.wishImage = wishImage;
    }

    public String getWishName() {
        return wishName;
    }

    public void setWishName(String wishName) {
        this.wishName = wishName;
    }

    public String getWishPrice() {
        return wishPrice;
    }

    public void setWishPrice(String wishPrice) {
        this.wishPrice = wishPrice;
    }

    public String getWishCutPrice() {
        return wishCutPrice;
    }

    public void setWishCutPrice(String wishCutPrice) {
        this.wishCutPrice = wishCutPrice;
    }

    public String getWishListProductId() {
        return wishListProductId;
    }

    public void setWishListProductId(String wishListProductId) {
        this.wishListProductId = wishListProductId;
    }

    public String getWishStockInfo() {
        return wishStockInfo;
    }

    public void setWishStockInfo(String wishStockInfo) {
        this.wishStockInfo = wishStockInfo;
    }

    public boolean getWishCODinfo() {
        return wishCODinfo;
    }

    public void setWishCODinfo(boolean wishCODinfo) {
        this.wishCODinfo = wishCODinfo;
    }
}
