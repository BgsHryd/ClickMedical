package com.example.clickmedical;

public class Pesanan {
    public String customerID;
    public String kamarID;
    public boolean status;
    public boolean checked;

    public Pesanan(String customerID, String kamarID, boolean status, boolean checked){
        this.customerID = customerID;
        this.kamarID = kamarID;
        this.status = status;
        this.checked = checked;
    }

}
