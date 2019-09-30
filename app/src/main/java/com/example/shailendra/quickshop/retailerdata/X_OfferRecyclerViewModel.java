package com.example.shailendra.quickshop.retailerdata;

import android.media.Image;
import android.widget.ImageView;
import android.widget.TextView;

public class X_OfferRecyclerViewModel {

    private int xOfferItemImage;
    private String xOfferItemPrice, xOfferItemName;

    public X_OfferRecyclerViewModel(int xOfferItemImage, String xOfferItemPrice, String xOfferItemName) {
        this.xOfferItemImage = xOfferItemImage;
        this.xOfferItemPrice = xOfferItemPrice;
        this.xOfferItemName = xOfferItemName;
    }

    public int getxOfferItemImage() {
        return xOfferItemImage;
    }

    public void setxOfferItemImage(int xOfferItemImage) {
        this.xOfferItemImage = xOfferItemImage;
    }

    public String getxOfferItemPrice() {
        return xOfferItemPrice;
    }

    public void setxOfferItemPrice(String xOfferItemPrice) {
        this.xOfferItemPrice = xOfferItemPrice;
    }

    public String getxOfferItemName() {
        return xOfferItemName;
    }

    public void setxOfferItemName(String xOfferItemName) {
        this.xOfferItemName = xOfferItemName;
    }
}
