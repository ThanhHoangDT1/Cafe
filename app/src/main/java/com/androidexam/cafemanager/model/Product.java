package com.androidexam.cafemanager.model;

public class Product {
    private String id;
    private String name;
    private String urlImage;
    private long price;
    private String description;
    private String category;
    private long quantity;

    public Product() {
    }

    public Product(String id, long quantity,String name, String urlImage, long price, String description, String category) {
        this.id = id;
        this.name = name;
        this.urlImage = urlImage;
        this.quantity = quantity;
        this.price = price;
        this.description = description;
        this.category = category;
    }

    public Product(String id, String name, long price, int i) {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public long getPrice() {
        return price;
    }
    public long getquantity() {
        return quantity;
    }

    public void setPrice(long price) {
        this.price = price;
    }
    public void setquantity(long quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
