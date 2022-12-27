package com.example.clickmedical;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BookmarkActivity extends AppCompatActivity {
    private ListView bookmarks;
    ArrayList<RumahSakit> RSList;
    ArrayList<Kamar> listKamar;
    Kamar temp;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        db = FirebaseFirestore.getInstance();
        int RSImages[] = {R.drawable.hospital_bed, R.drawable.hospital_bed};

        ArrayList res = new ArrayList<>();
        db.collection("RumahSakit")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                res.add(document);
                            }
                            documentsToRS(res);
                            bookmarks = (ListView) findViewById(R.id.bookmarkListView);
                            CustomBaseAdapterRS adapter = new CustomBaseAdapterRS(getApplicationContext(), RSList, RSImages);
                            bookmarks.setBackgroundResource(R.drawable.custom_shape);
                            bookmarks.setAdapter(adapter);
                            bookmarks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    fetchKamarData(position);
                                }
                            });
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    public void fetchKamarData(int position){
        RumahSakit rs = RSList.get(position);
        String id = rs.getRSID();
        db.collection("Kamar")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            queriesToArrayKamar(task, id);
                        }else{
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    public void queriesToArrayKamar(Task<QuerySnapshot> task, String FK_RSID){
        listKamar = new ArrayList<>();
        for (QueryDocumentSnapshot document: task.getResult()) {
            if (document.getDocumentReference("RSID").getId().equals(FK_RSID)) {
                int harga = ((Long) document.get("harga")).intValue();
                String jenis = (String) document.get("jenis");
                int jumlah = ((Long) document.get("jumlah")).intValue();
                String nama = (String) document.get("nama");
                listKamar.add(new Kamar(FK_RSID, harga, jenis, jumlah, nama));
                Log.w(TAG, document.getId() + " => " + document.getData());
            }else{
                Log.w(TAG, "RSID tidak cocok dengan yang dicari");
            }
        }
    }
    public void documentsToRS(ArrayList queryResult){
        RSList = new ArrayList<>();
        RumahSakit temp;
        String RSID,nama, alamat, phone, website;
        double latitude, longitude;
        QueryDocumentSnapshot document;

        for (Object o : queryResult){
            document = (QueryDocumentSnapshot) o;
            RSID = document.getId();
            nama = (String) document.get("nama");
            alamat = (String) document.get("alamat");
            phone = (String) document.get("phone");
            website = (String) document.get("website");
            latitude = (double) document.get("latitude");
            longitude = (double) document.get("longitude");

            temp = new RumahSakit(RSID, nama, latitude, longitude, phone, website, alamat);
            RSList.add(temp);
        }

    }
}