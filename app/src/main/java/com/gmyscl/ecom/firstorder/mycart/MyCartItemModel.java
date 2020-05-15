package com.gmyscl.ecom.firstorder.mycart;

public class MyCartItemModel {

    public static final int CART_TYPE_ITEMS = 0;
    public static final int CART_TYPE_TOTAL_PRICE = 1;

    // Types...
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    // Types...

    //---  Cart Total Price....
    public MyCartItemModel(int type) {
        this.type = type;
    }
    //---  Cart Total Price....

    private String productImage;
    private String productName;
    private String productPrice;
    private String productCutPrice;
    private String productId;
    private int productQuantity;
    private String productQtyType;

    public MyCartItemModel( int type, String productId,int productQuantity,String productImage, String productName, String productPrice, String productCutPrice, String productQtyType ) {
        this.type = type;
        this.productId = productId;
        this.productQuantity = productQuantity;
        this.productImage = productImage;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCutPrice = productCutPrice;
        this.productQtyType = productQtyType;
    }

    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }
    public int getProductQuantity() {
        return productQuantity;
    }
    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }
    public String getProductImage() {
        return productImage;
    }
    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getProductPrice() {
        return productPrice;
    }
    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }
    public String getProductCutPrice() {
        return productCutPrice;
    }
    public void setProductCutPrice(String productCutPrice) {
        this.productCutPrice = productCutPrice;
    }

    public String getProductQtyType() {
        return productQtyType;
    }

    public void setProductQtyType(String productQtyType) {
        this.productQtyType = productQtyType;
    }
}
