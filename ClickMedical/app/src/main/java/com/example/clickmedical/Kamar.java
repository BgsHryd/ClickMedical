package com.example.clickmedical;

public class Kamar {
    private String RSID;
    private int harga;
    private String jenis;
    private int jumlah;
    private String nama;

    public Kamar(String RSID, int harga, String jenis, int jumlah, String nama){
        this.RSID = RSID;
        this.harga = harga;
        this.jenis = jenis;
        this.jumlah = jumlah;
        this.nama = nama;
    }

    public String getNama() {
        return nama;
    }
    public int getHarga() {
        return harga;
    }
    public int getJumlah() {
        return jumlah;
    }
    public String getJenis() {
        return jenis;
    }
    public String getRSID() {
        return RSID;
    }
}
