package com.example.shailendra.quickshop;

public class G_Database_One {

    String UserName, UserMobile, UserEmail, UserID, UserPass;

    public G_Database_One(){
        // Default Constructor...
    }

//    -----------  Retailer User Registration on Google firebase...
    public G_Database_One(String UserName,String UserEmail,String UserMobile,String UserID){
        this.UserName = UserName;
        this.UserEmail = UserEmail;
        this.UserMobile = UserMobile;
        this.UserID = UserID;
    }


}
