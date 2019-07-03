package com.wingrez.lovelypet.bean;

public class CleanserBean {
    private int id; //清洁品编号
    private String name; //清洁品名称
    private int refresh; //恢复值

    public CleanserBean(int id, String name, int refresh) {
        this.id = id;
        this.name = name;
        this.refresh = refresh;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRefresh() {
        return refresh;
    }

    public void setRefresh(int refresh) {
        this.refresh = refresh;
    }
}
