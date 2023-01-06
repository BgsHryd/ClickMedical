package com.example.clickmedical;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class History {
    private String namaUser;
    private String namaKamar;
    private String namaRS;
    private String kamarID;
    private String RSID;
    private String userID;
    private Date tanggal;

    public History(String namaUser, String namaKamar, String namaRS, String userID, String kamarID, String RSID, Date tanggal){
        this.namaUser = namaUser;
        this.namaKamar = namaKamar;
        this.namaRS = namaRS;
        this.userID = userID;
        this.kamarID = kamarID;
        this.RSID = RSID;
        this.tanggal = tanggal;
    }

    public String getRSID() {
        return RSID;
    }

    public String getKamarID() {
        return kamarID;
    }

    public String getNamaKamar() {
        return namaKamar;
    }

    public String getNamaRS() {
        return namaRS;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public String getUserID() {
        return userID;
    }
    public String getDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        return formatter.format(tanggal);
    }
}
