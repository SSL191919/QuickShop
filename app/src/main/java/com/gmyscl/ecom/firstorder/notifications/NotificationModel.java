package com.gmyscl.ecom.firstorder.notifications;

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

    // Common in all ...
    private int notifyType;
    private boolean notifyIsRead;
    private String notifyId;

    public NotificationModel(int notifyType, String notifyId, boolean notifyIsRead) {
        this.notifyType = notifyType;
        this.notifyId = notifyId;
        this.notifyIsRead = notifyIsRead;
    }

    public int getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(int notifyType) {
        this.notifyType = notifyType;
    }

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }

    public boolean isNotifyIsRead() {
        return notifyIsRead;
    }

    public void setNotifyIsRead(boolean notifyIsRead) {
        this.notifyIsRead = notifyIsRead;
    }
    // Common in all ...

    ////------------- Order Related Notification...
    // 1. Accepted
    private String notifyOrderId;
    private String notifyOrderHeading;
    private String notifyOrderMessage;

    public NotificationModel(int notifyType, boolean notifyIsRead, String notifyId, String notifyOrderId, String notifyOrderHeading, String notifyOrderMessage) {
        this.notifyType = notifyType;
        this.notifyIsRead = notifyIsRead;
        this.notifyId = notifyId;
        this.notifyOrderId = notifyOrderId;
        this.notifyOrderHeading = notifyOrderHeading;
        this.notifyOrderMessage = notifyOrderMessage;
    }
    public String getNotifyOrderId() {
        return notifyOrderId;
    }

    public void setNotifyOrderId(String notifyOrderId) {
        this.notifyOrderId = notifyOrderId;
    }

    public String getNotifyOrderHeading() {
        return notifyOrderHeading;
    }

    public void setNotifyOrderHeading(String notifyOrderHeading) {
        this.notifyOrderHeading = notifyOrderHeading;
    }

    public String getNotifyOrderMessage() {
        return notifyOrderMessage;
    }

    public void setNotifyOrderMessage(String notifyOrderMessage) {
        this.notifyOrderMessage = notifyOrderMessage;
    }
    //-----------


}
