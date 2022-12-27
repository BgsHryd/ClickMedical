package com.example.clickmedical;

public class RumahSakit {
    private String RSID;
    private String nama;
    private double longitude;
    private double latitude;
    private String phone;
    private String website;
    private String alamat;
    private int image;

    public RumahSakit(String RSID, String nama, double latitude, double longitude, String phone, String website, String alamat){
        this.RSID = RSID;
        this.nama = nama;
        this.longitude = longitude;
        this.latitude = latitude;
        this.phone = phone;
        this.website = website;
        this.alamat = alamat;
    }

    public String getRSID() {
        return RSID;
    }
    public String getNama() {
        return nama;
    }
    public double getAltitude(){
        return longitude;
    }
    public double getLatitude(){return latitude;}
    public String getPhone(){
        return phone;
    }
    public String getWebsite(){
        return website;
    }
    public String getAlamat(){return alamat;}
}
