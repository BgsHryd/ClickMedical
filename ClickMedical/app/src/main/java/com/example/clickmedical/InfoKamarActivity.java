package com.example.clickmedical;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InfoKamarActivity extends AppCompatActivity {
    TextView namaRS, namaKamar, jenisKamar, jumlahKamar, hargaKamar;
    ImageView imageKamar;
    Button orderButton;
    AlertDialog.Builder builder;
    DBHelper db;
    HelperSaver saver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_kamar);
        HelperSaver saver = new HelperSaver();
        RumahSakit RStarget = saver.getRumahSakit();
        Kamar kamarTarget = saver.getKamar();

        db = new DBHelper();
        imageKamar = findViewById(R.id.imageKamar);
        namaRS = findViewById(R.id.namaRS);
        namaKamar = findViewById(R.id.namaKamar);
        jenisKamar = findViewById(R.id.jenisKamar);
        jumlahKamar = findViewById(R.id.jumlahKamar);
        hargaKamar = findViewById(R.id.hargaKamar);
        orderButton = findViewById(R.id.orderButton);

        // nampilin data ke layar
        imageKamar.setImageResource(saver.getImageKamar());
        namaRS.setText(RStarget.getNama());
        namaKamar.setText(": "+kamarTarget.getNama());
        jenisKamar.setText(": "+kamarTarget.getJenis());
        jumlahKamar.setText(": "+kamarTarget.getJumlah() + " Kamar");
        hargaKamar.setText(": Rp." + giveDotToHarga(kamarTarget.getHarga()) + ",00");

        // inisialisasi dialog pop up
        builder = new AlertDialog.Builder(this);

        // case kalo tombol pesan ditekan
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // menambahkan rule ke dalam dialog
                builder.setTitle("Konfirmasi pesanan")
                        .setMessage("Yakin melakukan pesanan?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // nanti disini kirim data ke RS
                                // kalo tekan yes nanti dia akan menambahkan pesanan ke dalam DB
                                addPesananToDB();
                                startActivity(new Intent(getApplicationContext(), SimulationAcceptActivity.class));
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // kalo tekan no entar si pop up nya ilang
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });
    }
    public Map<String, Object> createHashMapPesanan(String custId, String kamarId, boolean status){
        // membuat hashmap pesanan biar gampang nambahinnya ke dalam firebase

        Map<String, Object> result = new HashMap<>();
        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();

        result.put("customerID", firestoreDB.document("Customer/" + custId));
        result.put("kamarID", firestoreDB.document("Kamar/" + kamarId));
        result.put("accepted", status);
        result.put("checked", false);
        result.put("tanggal", new Date());

        return result;
    }
    public void addPesananToDB(){
        // nambahin data pesanan ke dalam database
        Pesanan res;
        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
        Map<String, Object> map;

        String custId = db.currentUser.getId();
        String kamarId = saver.getKamar().getKamarId();
        Boolean status = false;
        Boolean checked = false;

        res = new Pesanan(custId, kamarId, status, checked);
        map = createHashMapPesanan(custId, kamarId, status);
        db.listPesanan.add(res);
        db.pesananId = firestoreDB.collection("Pesanan").document();
        db.pesananId.set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Document successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "error writing document", e);
                    }
                });

    }

    public String giveDotToHarga(int harga){
        // biar waktu nampilin harga ada titik yang misahin ribuan
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