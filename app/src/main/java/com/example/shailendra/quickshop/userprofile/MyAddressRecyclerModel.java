package com.example.shailendra.quickshop.userprofile;

public class MyAddressRecyclerModel {

    private Boolean isSelectedAddress = false;

    public Boolean getSelectedAddress() {
        return isSelectedAddress;
    }

    public void setSelectedAddress(Boolean selectedAddress) {
        isSelectedAddress = selectedAddress;
    }

    // new Address...
    private String addUserName;
    private String addHNo;
    private String addColony;
    private String addCity;
    private String addState;
    private String addAreaCode;
    private String addLandmark;

    public MyAddressRecyclerModel(String addUserName, String addHNo, String addColony, String addCity, String addState, String addAreaCode, String addLandmark) {
        this.addUserName = addUserName;
        this.addHNo = addHNo;
        this.addColony = addColony;
        this.addCity = addCity;
        this.addState = addState;
        this.addAreaCode = addAreaCode;
        this.addLandmark = addLandmark;
    }

    public String getAddUserName() {
        return addUserName;
    }
    public void setAddUserName(String addUserName) {
        this.addUserName = addUserName;
    }
    public String getAddHNo() {
        return addHNo;
    }
    public void setAddHNo(String addHNo) {
        this.addHNo = addHNo;
    }
    public String getAddColony() {
        return addColony;
    }
    public void setAddColony(String addColony) {
        this.addColony = addColony;
    }
    public String getAddCity() {
        return addCity;
    }
    public void setAddCity(String addCity) {
        this.addCity = addCity;
    }
    public String getAddState() {
        return addState;
    }
    public void setAddState(String addState) {
        this.addState = addState;
    }
    public String getAddAreaCode() {
        return addAreaCode;
    }
    public void setAddAreaCode(String addAreaCode) {
        this.addAreaCode = addAreaCode;
    }
    public String getAddLandmark() {
        return addLandmark;
    }
    public void setAddLandmark(String addLandmark) {
        this.addLandmark = addLandmark;
    }

    // new Address...

}




