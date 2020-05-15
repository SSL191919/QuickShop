package com.gmyscl.ecom.firstorder.userprofile;

public class UserInfoClass {

    private String userId;
    private String userName;
    private String userMobile;
    private String userEmail;
    private String userImage;

    private String userCurrentCity;
    private String userCurrentArea;

    public UserInfoClass() {
    }

    public UserInfoClass(String userId, String userName, String userMobile, String userEmail, String userImage, String userCurrentCity, String userCurrentArea) {
        this.userId = userId;
        this.userName = userName;
        this.userMobile = userMobile;
        this.userEmail = userEmail;
        this.userImage = userImage;
        this.userCurrentCity = userCurrentCity;
        this.userCurrentArea = userCurrentArea;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserCurrentCity() {
        return userCurrentCity;
    }

    public void setUserCurrentCity(String userCurrentCity) {
        this.userCurrentCity = userCurrentCity;
    }

    public String getUserCurrentArea() {
        return userCurrentArea;
    }

    public void setUserCurrentArea(String userCurrentArea) {
        this.userCurrentArea = userCurrentArea;
    }
}
