package com.example.clickmedical;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper {
    public FirebaseFirestore db;
    public static Customer currentUser;
    public static Task<QuerySnapshot> taskQueryRS;
    public static Task<QuerySnapshot> taskQueryKamar;
    public static Task<QuerySnapshot> taskQueryCustomer;
    public static ArrayList<RumahSakit> listRS;
    public static ArrayList<Bitmap> listImageRS;
    public static ArrayList<Kamar> listKamar;
    public static ArrayList<RumahSakit> listBookmarks;
    public static ArrayList<Pesanan> listPesanan;
    public static int[] imagesRS = {R.drawable.santosa, R.drawable.immanuel};
    public static HashMap<String, int[]> imagesKamar = new HashMap<>();
    public static boolean status;
    public static DocumentReference pesananId;

    public DBHelper(){
        db = FirebaseFirestore.getInstance();
        imagesKamar.put("Rumah Sakit Santosa Bandung",
                new int[]{R.drawable.santosa_vipps, R.drawable.santosa_vips, R.drawable.santosa_kelas1});
        imagesKamar.put("Rumah Sakit Immanuel Bandung",
                new int[]{R.drawable.imannuel_premiere_a, R.drawable.imannuel_suite, R.drawable.imannuel_kelas1});
        if (listPesanan == null){
            listPesanan = new ArrayList<>();
        }
    }
    public void getCurrentCustomerData(String email){
        taskQueryCustomer = db.collection("Customer").get();
        taskQueryCustomer.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> taskQueryCustomer) {
                if (taskQueryCustomer.isSuccessful()){
                    for (QueryDocumentSnapshot document: taskQueryCustomer.getResult()){
                        if (document.get("email").equals(email)){
                            String nama = (String) document.get("nama");
                            String id = document.getId();
                            currentUser = new Customer(nama, email, id);
                            Log.w(TAG, document.getId() + " ==> " + document.getData());
                        }else{
                            Log.w(TAG, document.get("email") + " != " + email);
                        }
                    }
                }else{
                    Log.w(TAG, "Error Getting Documents", taskQueryCustomer.getException());
                }
            }
        });
    }

    public RumahSakit docConvRS(QueryDocumentSnapshot document){
        String ID, nama, alamat, phone, website, imagePath;
        double latitude, longitude;

        ID = document.getId();
        nama = (String) document.get("nama");
        alamat = (String) document.get("alamat");
        phone = (String) document.get("phone");
        website = (String) document.get("website");
        latitude = (double) document.get("latitude");
        longitude = (double) document.get("longitude");
        imagePath = (String) document.get("imagePath");

        return new RumahSakit(ID, nama, latitude, longitude, phone, website, alamat, imagePath);
    }
    public void getQueryBookmarks(String collectionPath, String userId){
        listBookmarks = new ArrayList<>();
        status = false;
        taskQueryCustomer = db.collection(collectionPath).get();
        taskQueryCustomer.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> taskQueryCustomer) {
                if (taskQueryCustomer.isSuccessful()){
                    for (QueryDocumentSnapshot document : taskQueryCustomer.getResult()){
                        if (document.getDocumentReference("CustomerID").getId().equals(userId)){
                            String ID = document.getDocumentReference("RSID").getId();
                            taskQueryRS = db.collection("RumahSakit").get();
                            taskQueryRS.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> taskQueryRS) {
                                    for (QueryDocumentSnapshot document: taskQueryRS.getResult()){
                                        if (taskQueryRS.isSuccessful()) {
                                            if (document.getId().equals(ID)) {
                                                System.out.println("ini query rs by id");
                                                String nama, alamat, phone, website, imagePath;
                                                double latitude, longitude;

                                                nama = (String) document.get("nama");
                                                alamat = (String) document.get("alamat");
                                                phone = (String) document.get("phone");
                                                website = (String) document.get("website");
                                                latitude = (double) document.get("latitude");
                                                longitude = (double) document.get("longitude");
                                                imagePath = (String) document.get("imagePath");
                                                listBookmarks.add(new RumahSakit(ID, nama, latitude, longitude, phone, website, alamat, imagePath));
                                                Log.w(TAG, document.getId() + " ==>== " + document.getData());
                                                System.out.println("size list BOokmark " + listBookmarks.size());
                                            }else{
                                                Log.w(TAG, document.getId() + " != " + ID);
                                            }
                                        }else{
                                            Log.w(TAG, "ERROR Getting Documents", taskQueryCustomer.getException());
                                        }
                                    }
                                }
                            });
                        }else{
                            Log.w(TAG, document.getDocumentReference("CustomerID").getId() + " != " + userId);
                        }
                    }
                }else{
                    Log.w(TAG, "ERROR Getting Documents", taskQueryCustomer.getException());
                }
            }
        });
    }

    public void getQueryRumahSakit(String collectionPath){
        listImageRS = new ArrayList<>();
        listRS = new ArrayList<>();
        taskQueryRS = db.collection(collectionPath).get();
        taskQueryRS.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> taskQueryRS) {
                if (taskQueryRS.isSuccessful()){
                    for (QueryDocumentSnapshot document : taskQueryRS.getResult()){
                        documentToRS(document);
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                }else{
                    Log.w(TAG, "ERROR Getting Documents", taskQueryRS.getException());
                }
            }
        });
    }
    public void getQueryKamar(String collectionPath, int position) {
        RumahSakit rs = listRS.get(position);
        String rsid = rs.getRSID();
        taskQueryKamar = db.collection(collectionPath).get();
        taskQueryKamar.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> taskQueryKamar) {
                if (taskQueryKamar.isSuccessful()){
                    queriesToArrayKamar(taskQueryKamar, rsid);
                }else{
                    Log.w(TAG, "Error getting cocuments.", taskQueryKamar.getException());
                }
            }
        });
    }

    public void getAllQueryKamar(String collectionPath) {
        listKamar = new ArrayList<>();
        taskQueryKamar = db.collection(collectionPath).get();
        taskQueryKamar.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> taskQueryKamar) {
                if (taskQueryKamar.isSuccessful()){
                    for (QueryDocumentSnapshot document: taskQueryKamar.getResult()){
                        String RSID = document.getDocumentReference("RSID").getId();
                        int harga = ((Long) document.get("harga")).intValue();
                        String jenis = (String) document.get("jenis");
                        int jumlah = ((Long) document.get("jumlah")).intValue();
                        String nama = (String) document.get("nama");

                        listKamar.add(new Kamar(RSID, harga, jenis, jumlah, nama, document.getId()));
                    }
                }else{
                    Log.w(TAG, "Error getting cocuments.", taskQueryKamar.getException());
                }
            }
        });
    }

    public void queriesToArrayKamar(Task<QuerySnapshot> task, String FK_RSID){
        Kamar temp;
        listKamar = new ArrayList<>();
        for (QueryDocumentSnapshot document: task.getResult()) {
            if (document.getDocumentReference("RSID").getId().equals(FK_RSID)) {
                int harga = ((Long) document.get("harga")).intValue();
                String jenis = (String) document.get("jenis");
                int jumlah = ((Long) document.get("jumlah")).intValue();
                String nama = (String) document.get("nama");
                String kamarId = document.getId();

                temp = new Kamar(FK_RSID, harga, jenis, jumlah, nama, kamarId);
                listKamar.add(temp);
                Log.w(TAG, document.getId() + " => " + document.getData());
            }else{
                Log.w(TAG, "RSID tidak cocok dengan yang dicari");
            }
        }
    }


    public void documentToRS(QueryDocumentSnapshot document){
        RumahSakit temp;
        String RSID,nama, alamat, phone, website, imagePath;
        double latitude, longitude;

        RSID = document.getId();
        nama = (String) document.get("nama");
        alamat = (String) document.get("alamat");
        phone = (String) document.get("phone");
        website = (String) document.get("website");
        latitude = (double) document.get("latitude");
        longitude = (double) document.get("longitude");
        imagePath = (String) document.get("imagePath");

        temp = new RumahSakit(RSID, nama, latitude, longitude, phone, website, alamat, imagePath);
        listRS.add(temp);
    }

}
