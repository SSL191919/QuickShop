package com.gmyscl.ecom.firstorder.homecatframe;

public class HomeCategoryIconModel {

    private String categoryIcon;
    private String categoryName;

    public HomeCategoryIconModel(String categoryIcon, String categoryName) {
        this.categoryIcon = categoryIcon;
        this.categoryName = categoryName;
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
