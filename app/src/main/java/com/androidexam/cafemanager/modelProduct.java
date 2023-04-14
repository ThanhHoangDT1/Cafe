package com.androidexam.cafemanager;

public class modelProduct {

    String maSP, tenSP, purl;
    int giaSP;

    public modelProduct() {
    }

    public modelProduct(String maSP, String tenSP, String purl, int giaSP) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.purl = purl;
        this.giaSP = giaSP;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    public int getGiaSP() {
        return giaSP;
    }

    public void setGiaSP(int giaSP) {
        this.giaSP = giaSP;
    }
}
