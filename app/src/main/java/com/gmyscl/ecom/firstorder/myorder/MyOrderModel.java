package com.gmyscl.ecom.firstorder.myorder;

public class MyOrderModel {

    // My Order List...
    private String myOrderId;
    private String myOrderImageLink;
    private String myOrderItemName;
    private String myOrderDeliveryStatus;
    // Delivery Status == DELIVERED then it will replaced by Delivery Time... We will get Delivery time from Status...
    private String myOrderDeliveyTime;


    public MyOrderModel(String myOrderId, String myOrderImageLink, String myOrderItemName, String myOrderDeliveryStatus) {
        this.myOrderId = myOrderId;
        this.myOrderImageLink = myOrderImageLink;
        this.myOrderItemName = myOrderItemName;
        this.myOrderDeliveryStatus = myOrderDeliveryStatus;
    }

    public String getMyOrderId() {
        return myOrderId;
    }

    public void setMyOrderId(String myOrderId) {
        this.myOrderId = myOrderId;
    }

    public String getMyOrderImageLink() {
        return myOrderImageLink;
    }
    public void setMyOrderImageLink(String myOrderImageLink) {
        this.myOrderImageLink = myOrderImageLink;
    }
    public String getMyOrderItemName() {
        return myOrderItemName;
    }
    public void setMyOrderItemName(String myOrderItemName) {
        this.myOrderItemName = myOrderItemName;
    }
    public String getMyOrderDeliveryStatus() {
        return myOrderDeliveryStatus;
    }
    public void setMyOrderDeliveryStatus(String myOrderDeliveryStatus) {
        this.myOrderDeliveryStatus = myOrderDeliveryStatus;
    }
    // My Order List...

    // My Order Details list...
    // Product Name
    // product Image
    // Delivery Status
    // Delivery Time..

    // My Order Details list...



}
