package com.wingrez.lovelypet.bean;

public class NoticeBean {
    private String appName;
    private String title;
    private String content;

    public NoticeBean(String appName, String title, String content) {
        this.appName = appName;
        this.title = title;
        this.content = content;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return appName+"消息"+"#"+title+":"+content;
    }
}
