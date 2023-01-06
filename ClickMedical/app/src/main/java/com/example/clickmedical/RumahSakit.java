package com.example.clickmedical;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RumahSakit {
    private String RSID;
    private String nama;
    private double longitude;
    private double latitude;
    private String phone;
    private String website;
    private String alamat;
    private String imagePath;

    public RumahSakit(String RSID, String nama, double latitude, double longitude, String phone, String website, String alamat, String imagePath){
        this.RSID = RSID;
        this.nama = nama;
        this.longitude = longitude;
        this.latitude = latitude;
        this.phone = phone;
        this.website = website;
        this.alamat = alamat;
        this.imagePath = imagePath;
    }

    public String getRSID() {
        return RSID;
    }
    public String getNama() {
        return nama;
    }
    public double getLongitude(){
        return longitude;
    }
    public double getLatitude(){return latitude;}
    public String getPhone(){
        return phone;
    }
    public String getWebsite(){
        return website;
    }
    public String getAlamatFull(){return alamat;}
    public String getImagePath(){return imagePath;}
    public String getAlamat(){
        Pattern pattern = Pattern.compile("Jl..+?,");
        Matcher matcher = pattern.matcher(alamat);
        while (matcher.find()) {
            return matcher.group(0).substring(0, matcher.group(0).length() - 1);
        }
        return alamat;
    }
}
