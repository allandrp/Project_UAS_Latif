package com.example.project_uas.model;

public class Toko {
    private String nama, jenisToko;
    private Double latitude, logitude;

    public Toko(String nama, String jenisToko, Double latitude, Double logitude) {
        this.nama = nama;
        this.latitude = latitude;
        this.logitude = logitude;
        this.jenisToko = jenisToko;
    }

    public Toko(){

    }

    public String getNama() {
        return nama;
    }

    public Double getLatitude() {
        return latitude;
    }

    public String getJenisToko() {
        return jenisToko;
    }

    public Double getLogitude() {
        return logitude;
    }
}
