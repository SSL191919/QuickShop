package com.example.shailendra.quickshop;

public class W_Example_Item {
    private int wItemImageResource;
    private String wItemName, wItemDescription;


    public W_Example_Item(int wItemImageResource, String wItemName, String wItemDescription) {
        this.wItemImageResource = wItemImageResource;
        this.wItemName = wItemName;
        this.wItemDescription = wItemDescription;
    }

    public int getwItemImageResource() {
        return wItemImageResource;
    }

    public void setwItemImageResource(int wItemImageResource) {
        this.wItemImageResource = wItemImageResource;
    }

    public String getwItemName() {
        return wItemName;
    }

    public void setwItemName(String wItemName) {
        this.wItemName = wItemName;
    }

    public String getwItemDescription() {
        return wItemDescription;
    }

    public void setwItemDescription(String wItemDescription) {
        this.wItemDescription = wItemDescription;
    }


}
