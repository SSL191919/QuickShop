package com.gmyscl.ecom.firstorder.buyprocess;

public class BuyNowModel {

    private String productId;
    private String productName;
    private String productImg;
    private String productQty;
    private String productPrice;

    public BuyNowModel(String productId, String productName, String productImg, String productPrice,String productQty) {
        this.productId = productId;
        this.productName = productName;
        this.productImg = productImg;
        this.productPrice = productPrice;
        this.productQty = productQty;
    }

    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getProductImg() {
        return productImg;
    }
    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }
    public String getProductPrice() {
        return productPrice;
    }
    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }
    public String getProductQty() {
        return productQty;
    }
    public void setProductQty(String productQty) {
        this.productQty = productQty;
    }

}
