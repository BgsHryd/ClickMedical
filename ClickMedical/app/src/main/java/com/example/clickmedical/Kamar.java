package com.example.clickmedical;

public class Kamar {
    private String RSID;
    private int harga;
    private String jenis;
    private int jumlah;
    private String nama;
    private String kamarId;

    public Kamar(String RSID, int harga, String jenis, int jumlah, String nama, String kamarId){
        this.RSID = RSID;
        this.harga = harga;
        this.jenis = jenis;
        this.jumlah = jumlah;
        this.nama = nama;
        this.kamarId = kamarId;
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
    public String getKamarId() {
        return kamarId;
    }
}
