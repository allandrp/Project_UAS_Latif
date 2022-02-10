package com.example.project_uas.model;

public class Cart{
        int jumlahPesan, harga;
        String nama;

        public Cart() {
        }

        public Cart(int jumlahPesan, int harga, String nama) {
            this.jumlahPesan = jumlahPesan;
            this.harga = harga;
            this.nama = nama;
        }

        public void setJumlahPesan(int jumlahPesan) {
            this.jumlahPesan = jumlahPesan;
        }

        public void setHarga(int harga) {
            this.harga = harga;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public int getJumlahPesan() {
            return jumlahPesan;
        }

        public int getHarga() {
            return harga;
        }

        public String getNama() {
            return nama;
        }
}
