package com.androidexam.cafemanager.model;

import java.util.Date;

public class Oder {
    private int id;
    private int idStaff;
    private long totalBillAmount;
    private String note;
    private Date createAt;

    public Oder(){
    }
    public Oder(int id, int idStaff, long totalBillAmount, String note, Date createAt) {
        this.id = id;
        this.idStaff = idStaff;
        this.totalBillAmount = totalBillAmount;
        this.note = note;
        this.createAt = createAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdStaff() {
        return idStaff;
    }

    public void setIdStaff(int idStaff) {
        this.idStaff = idStaff;
    }

    public long getTotalBillAmount() {
        return totalBillAmount;
    }

    public void setTotalBillAmount(long totalBillAmount) {
        this.totalBillAmount = totalBillAmount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

}
