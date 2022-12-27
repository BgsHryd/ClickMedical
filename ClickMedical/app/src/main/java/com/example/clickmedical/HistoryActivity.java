package com.example.clickmedical;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private ListView histories;
    ArrayList<RumahSakit> RSList;
    int RSImages[] = {R.drawable.hospital_bed, R.drawable.hospital_bed, R.drawable.hospital_bed};

    ListView listview;
    ArrayList result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
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
                            histories = (ListView) findViewById(R.id.histListView);
                            CustomBaseAdapterRS adapter = new CustomBaseAdapterRS(getApplicationContext(), RSList, RSImages);
                            histories.setBackgroundResource(R.drawable.custom_shape);
                            histories.setAdapter(adapter);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    public void documentsToRS(ArrayList queryResult) {
        RSList = new ArrayList<>();
        RumahSakit temp;
        String RSID, nama, alamat, phone, website;
        double latitude, longitude;
        QueryDocumentSnapshot document;

        for (Object o : queryResult) {
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