package com.example.clickmedical;

import java.util.ArrayList;

public class Customer {
    private String nama;
    private String email;
    private String id;

    public Customer(String nama, String email, String id){
        this.nama = nama;
        this.email = email;
        this.id = id;
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
}
