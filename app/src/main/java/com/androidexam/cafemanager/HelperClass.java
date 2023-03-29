package com.androidexam.cafemanager;

public class HelperClass {
    String name, email,role, username, password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public HelperClass(String name, String email,String role, String username, String password) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.username = username;
        this.password = password;
    }

    public HelperClass() {
    }
}
