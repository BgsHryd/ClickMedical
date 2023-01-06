package com.example.clickmedical;

import java.util.ArrayList;

public class Customer {
    private String nama;
    private String email;
    private String id;
    private static ArrayList<Pesanan> history;
    private static ArrayList<RumahSakit> bookmarks;

    public Customer(String nama, String email, String id){
        this.nama = nama;
        this.email = email;
        this.id = id;
        history = new ArrayList<>();
        bookmarks = new ArrayList<>();
    }

    public static void setBookmarks(ArrayList<RumahSakit> bookmarks) {
        Customer.bookmarks = bookmarks;
    }
    public static void setHistory(ArrayList<Pesanan> history) {
        Customer.history = history;
    }
    public ArrayList<RumahSakit> getBookmarks() {
        return bookmarks;
    }
    public String getEmail() {
        return email;
    }
    public String getId() {
        return id;
    }
    public String getNama() {
        return nama;
    }
    public ArrayList<Pesanan> getHistory() {
        return history;
    }
}
