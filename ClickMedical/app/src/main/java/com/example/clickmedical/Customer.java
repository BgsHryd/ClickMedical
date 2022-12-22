package com.example.clickmedical;

import java.util.ArrayList;

public class Customer {
    private String nama;
    private ArrayList<Pesanan> history;
    private double xLoc;
    private double yLoc;

    public Customer(String nama, double xLoc, double yLoc){
        this.nama = nama;
        this.xLoc = xLoc;
        this.yLoc = yLoc;
        history = new ArrayList<>();
    }
    public String getNama() {
        return nama;
    }
    public double getxLoc() {
        return xLoc;
    }
    public double getyLoc(){
        return yLoc;
    }
    public ArrayList<Pesanan> getHistory() {
        return history;
    }
}
