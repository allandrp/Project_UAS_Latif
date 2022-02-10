package com.example.project_uas.model;

public class Barang {
    String nama;
    int harga;
    int imageResource;

    public Barang(String nama, int harga, int imageResource) {
        this.nama = nama;
        this.harga = harga;
        this.imageResource = imageResource;
    }

    public Barang() {
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getNama() {
        return nama;
    }

    public int getHarga() {
        return harga;
    }
}
