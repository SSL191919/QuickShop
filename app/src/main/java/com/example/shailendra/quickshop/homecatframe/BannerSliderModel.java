package com.example.shailendra.quickshop.homecatframe;

public class BannerSliderModel {
    private String bannerImage;
    private String backgroundColor;

    public BannerSliderModel(String bannerImage, String backgroundColor) {
        this.bannerImage = bannerImage;
        this.backgroundColor = backgroundColor;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
