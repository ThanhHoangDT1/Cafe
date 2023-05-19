package com.androidexam.cafemanager.model;

public class Oder {
    private String id;
    private String idStaff;
    private long totalBillAmount;
    private String createAt;

    public Oder() {
    }

    public Oder(String id, String idStaff, long totalBillAmount, String createAt) {
        this.id = id;
        this.idStaff = idStaff;
        this.totalBillAmount = totalBillAmount;
        this.createAt = createAt;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdStaff() {
        return idStaff;
    }

    public void setIdStaff(String idStaff) {
        this.idStaff = idStaff;
    }

    public long getTotalBillAmount() {
        return totalBillAmount;
    }

    public void setTotalBillAmount(long totalBillAmount) {
        this.totalBillAmount = totalBillAmount;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

}
