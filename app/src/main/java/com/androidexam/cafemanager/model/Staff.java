package com.androidexam.cafemanager.model;

public class Staff {
    private String id;
    private String userName;
    private String password;
    private String name;
    private String urlImageStaff;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlImageStaff() {
        return urlImageStaff;
    }

    public void setUrlImageStaff(String urlImageStaff) {
        this.urlImageStaff = urlImageStaff;
    }

    public Staff(String id, String userName, String password, String name, String urlImageStaff) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.urlImageStaff = urlImageStaff;
    }

    public Staff(){}
}
