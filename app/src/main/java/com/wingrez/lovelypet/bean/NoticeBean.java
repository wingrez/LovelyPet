package com.wingrez.lovelypet.bean;

public class NoticeBean {
    private String appName;
    private String contact;
    private String message;

    public NoticeBean(String appName, String contact, String message) {
        this.appName = appName;
        this.contact = contact;
        this.message = message;
    }

    @Override
    public String toString() {
        return appName+"消息: "+"\n"+message;
    }
}
