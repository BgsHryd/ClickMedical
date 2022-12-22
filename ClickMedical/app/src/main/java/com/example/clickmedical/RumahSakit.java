package com.example.clickmedical;

public class RumahSakit {
    private String nama;
    private double xLoc;
    private double yLoc;

    public RumahSakit(String nama, double xloc, double yloc){
        this.nama = nama;
        this.xLoc = xloc;
        this.yLoc = yloc;
    }
    public String getNama(){
        return nama;
    }
    public double getXLoc(){
        return xLoc;
    }
    public double getYLoc(){
        return yLoc;
    }
}
