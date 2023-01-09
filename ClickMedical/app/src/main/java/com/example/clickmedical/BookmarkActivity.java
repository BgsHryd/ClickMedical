package com.example.clickmedical;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import java.util.concurrent.TimeUnit;

public class BookmarkActivity extends AppCompatActivity {
    private ListView bookmarks;
    private FirebaseFirestore store;
    private String userId;
    private DBHelper db;
    private HelperSaver saver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // setting layout with activity_bookmark.xml
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        // initialize variable
        db = new DBHelper();
        db.listBookmarks = new ArrayList<>();
        saver = new HelperSaver();
        userId = db.currentUser.getId();
        store = FirebaseFirestore.getInstance();
        int RSImages[] = {R.drawable.santosa, R.drawable.immanuel};
        bookmarks = findViewById(R.id.bookmarkListView);

        // query bookmark dengan syarat query CustomerID pada query harus sama dengan id user yang sedang login saat ini
        store.collection("Bookmark").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        if (document.getDocumentReference("CustomerID").getId().equals(userId)){
                            Log.w(TAG, document.getDocumentReference("CustomerID").getId() + " === " + userId);
                            String RSID = document.getDocumentReference("RSID").getId();

                            // query data rumah sakit berdasarkan data RSID yang didapat dari query sebelumnya
                            store.collection("RumahSakit").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    for (QueryDocumentSnapshot docRS : task.getResult()){
                                        if (docRS.getId().equals(RSID)) {
                                            RumahSakit temp = db.docConvRS(docRS);
                                            db.listBookmarks.add(temp);
                                            Log.w(TAG, docRS.getId() + " =>==<= " + docRS.getData());
                                        }else{
                                            Log.w(TAG, docRS.getId() + " =!=!= " + RSID);
                                        }
                                    }
                                }
                            });
                        }else{
                            Log.w(TAG, document.getId() + " != " + userId);
                        }
                    }
                }else{
                    Log.w(TAG, "ERROR GETTING DOCUMENTS", task.getException());
                }
                // tunggu bentar biar datanya keload
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // nempel data kedalem ListView (lebih detail lihat file layout xml nya juga)
                CustomBaseAdapterRS adapter = new CustomBaseAdapterRS(getApplicationContext(), db.listBookmarks, RSImages);
                bookmarks.setBackgroundResource(R.drawable.custom_shape);
                bookmarks.setAdapter(adapter);

                // case item listview nya dipencet
                bookmarks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // simpan rumahSakit yang dipencet kedalam HelperSaver
                        RumahSakit rs = db.listRS.get(position);
                        saver.RS = rs;
                        db.getQueryKamar("Kamar", position);

                        // langsung query kamar biar cepet ke load pas pindah screen
                        while (!db.taskQueryKamar.isComplete()) ;

                        // pindah screen ke ListKamarActivity.java
                        startActivity(new Intent(getApplicationContext(), ListKamarActivity.class));
                    }
                });
            }
        });
    }
}