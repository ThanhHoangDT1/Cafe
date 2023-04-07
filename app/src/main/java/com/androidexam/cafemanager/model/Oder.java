package com.androidexam.cafemanager.model;

public class Oder {
    private int id;
    private int idStaff;
    private int idTable;
    private long totalBillAmount;
    private String note;

    public Oder() {
    }

    public Oder(int id, int idStaff, int idTable, long totalBillAmount, String note) {
        this.id = id;
        this.idStaff = idStaff;
        this.idTable = idTable;
        this.totalBillAmount = totalBillAmount;
        this.note = note;
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

    public int getIdTable() {
        return idTable;
    }

    public void setIdTable(int idTable) {
        this.idTable = idTable;
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
}
