package com.example.clickmedical;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class RecommendActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationProviderClient;
    private final int REQUEST_CODE = 100;
    DBHelper dbHelper;
    HelperSaver saver;
    RumahSakit recRS;
    private int idxClose;
    private Button recPesanBtn;
    private Address lokasi;
    private ImageView imageHospital;
    private TextView namaRS, alamatRS, phoneRS, webRS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        saver = new HelperSaver();
        dbHelper = new DBHelper();
        dbHelper.getQueryRumahSakit("RumahSakit");
        while (!dbHelper.taskQueryRS.isComplete());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        imageHospital = findViewById(R.id.imageRS);
        namaRS = findViewById(R.id.namaRSTitle);
        alamatRS = findViewById(R.id.RecRSAlamat);
        phoneRS = findViewById(R.id.RecRSPhone);
        webRS = findViewById(R.id.RecRSWebsite);
        recPesanBtn = findViewById(R.id.recPesanButton);

        getLastLocation(); // panggil ini buat dapetin lokasi disimpen di variable lokasi

        // case kalo tombol pesan ditekan
        recPesanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.getQueryKamar("Kamar", idxClose);
                while (!dbHelper.taskQueryKamar.isComplete()) ;
                saver.setRS(recRS);
                startActivity(new Intent(getApplicationContext(), ListKamarActivity.class));
            }
        });
    }
    private void getLastLocation(){
        // ambil data lokasi
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            Geocoder geoCoder = new Geocoder(RecommendActivity.this, Locale.getDefault());
                            List<Address> addresses = null;
                            try {
                                addresses = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                lokasi = addresses.get(0);
                                idxClose = getIdxClosestHospital(lokasi.getLatitude(), lokasi.getLongitude());
                                recRS = dbHelper.listRS.get(idxClose);
                                int image = dbHelper.imagesRS[idxClose];

                                imageHospital.setImageResource(image);
                                namaRS.setText(recRS.getNama());
                                alamatRS.setText(": " + recRS.getAlamat());
                                phoneRS.setText(": " + recRS.getPhone());
                                webRS.setText(": " + recRS.getWebsite());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }else{
            askPermission();
        }
    }
    private void askPermission(){
        // minta permission ke user buat ngambil data lokasi
        ActivityCompat.requestPermissions(RecommendActivity.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // kalo diizinin pake lokasi maka ambil lokasi
        if (requestCode == REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }else{
                Toast.makeText(this, "Required Permission", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private int getIdxClosestHospital(double latitude, double longitude){
        // return index rumah sakit yang terdekat dengan user
        // jarak dihitung dengan euclidean
        int idxRes = 0;
        RumahSakit temp = dbHelper.listRS.get(idxRes);
        double minDistance = euclideanDistance(latitude, longitude, temp.getLatitude(), temp.getLongitude());
        for (int i = 1; i< dbHelper.listRS.size();i++){
            temp = dbHelper.listRS.get(i);
            double distance = euclideanDistance(latitude, longitude, temp.getLatitude(), temp.getLongitude());
            if (distance < minDistance){
                minDistance = distance;
                idxRes = i;
            }
        }
        return idxRes;
    }

    // perhitungan euclidean distance
    private double euclideanDistance(double x1, double y1, double x2, double y2){
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}