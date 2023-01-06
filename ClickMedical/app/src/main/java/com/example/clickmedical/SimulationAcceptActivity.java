package com.example.clickmedical;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;
import java.util.Map;

public class SimulationAcceptActivity extends AppCompatActivity {
    TextView namaCust, emailCust, namaKamar, hargaKamar;
    Button reject, accept;
    HelperSaver saver;
    DBHelper db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation_accept);

        namaCust = findViewById(R.id.namaCustomer);
        emailCust = findViewById(R.id.emailCustomer);
        namaKamar = findViewById(R.id.namaKamarAcc);
        hargaKamar = findViewById(R.id.hargaKamarAcc);
        reject = findViewById(R.id.rejectButton);
        accept = findViewById(R.id.acceptButton);



        db = new DBHelper();
        saver = new HelperSaver();
        Customer current = db.currentUser;
        Kamar kamarTarget = saver.getKamar();

        namaCust.setText(current.getNama());
        emailCust.setText(current.getEmail());
        namaKamar.setText(kamarTarget.getNama());
        hargaKamar.setText("Rp."+ giveDotToHarga(kamarTarget.getHarga()) + ",00 /malam");

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.pesananId
                        .update("checked", true)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "Document successfully updated");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });
                startActivity(new Intent(getApplicationContext(), Home.class));
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<>();
                map.put("accepted", true);
                map.put("checked", true);
                db.pesananId
                        .update(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "Document successfully updated");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });
                startActivity(new Intent(getApplicationContext(), Home.class));
            }
        });
    }
    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
        Toast.makeText(this, "Please either reject or accept this order", Toast.LENGTH_SHORT).show();
    }
    public String giveDotToHarga(int harga){
        String strHarga = Integer.toString(harga);
        int len = strHarga.length();
        int count_three = 0; String result = "";

        for (int i = len-1; i >= 0; i--){
            result = strHarga.charAt(i) + result;
            count_three++;
            if (count_three == 3 && i != 0){
                result = "." + result;
                count_three = 0;
            }
        }
        return result;
    }
}