package com.example.shailendra.quickshop.retailerdata;

public class RetRecyclerListModel {

    String itemName, itemDiscrption;
    int imageResource;

    public RetRecyclerListModel(String itemName, String itemDiscrption, int imageResource) {
        this.itemName = itemName;
        this.itemDiscrption = itemDiscrption;
        this.imageResource = imageResource;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDiscrption() {
        return itemDiscrption;
    }

    public void setItemDiscrption(String itemDiscrption) {
        this.itemDiscrption = itemDiscrption;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
