package com.example.shailendra.quickshop.notifications;

public class NotificationModel {

    private String notificationImg;
    private String notificationText;
    private boolean isReaded;

    public NotificationModel(String notificationImg, String notificationText, boolean isReaded) {
        this.notificationImg = notificationImg;
        this.notificationText = notificationText;
        this.isReaded = isReaded;
    }

    public String getNotificationImg() {
        return notificationImg;
    }

    public void setNotificationImg(String notificationImg) {
        this.notificationImg = notificationImg;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public boolean isReaded() {
        return isReaded;
    }

    public void setReaded(boolean readed) {
        isReaded = readed;
    }

}
