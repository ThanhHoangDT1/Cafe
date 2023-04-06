package com.androidexam.cafemanager.model;

public class OderDetail {
    private String id;
    private String idOder;
    private String idProduct;
    private int quantity;
    private long price;

    public OderDetail() {
    }

    public OderDetail(String id, String idOder, String idProduct, int quantity, long price) {
        this.id = id;
        this.idOder = idOder;
        this.idProduct = idProduct;
        this.quantity = quantity;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdOder() {
        return idOder;
    }

    public void setIdOder(String idOder) {
        this.idOder = idOder;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}
