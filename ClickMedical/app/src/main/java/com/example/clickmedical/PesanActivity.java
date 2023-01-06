package com.example.clickmedical;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PesanActivity extends AppCompatActivity {
    private ListView listViewRS;
    private SearchView searchView;
    private DBHelper db = new DBHelper();
    private HelperSaver saver = new HelperSaver();
    private CustomBaseAdapterRS adapter;
    private AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan);

        db.getQueryRumahSakit("RumahSakit");
        while (!db.taskQueryRS.isSuccessful());
        db.getQueryBookmarks("Bookmark", db.currentUser.getId());
        while(!db.taskQueryCustomer.isSuccessful());

        adapter = new CustomBaseAdapterRS(getApplicationContext(), db.listRS, db.imagesRS);
        listViewRS = findViewById(R.id.RSListView);
        listViewRS.setAdapter(adapter);
        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        builder = new AlertDialog.Builder(this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                PesanActivity.this.adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });

        listViewRS.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // pop up dialog asking wheter the user want this
                // hospital to becoma a bookmark or not
                RumahSakit target = db.listRS.get(position);
                builder.setTitle("Add Bookmark")
                .setMessage("Menambahkan " + target.getNama() + " ke bookmark?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addRSToListBookmark(target);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
                return true;
            }
        });

        listViewRS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RumahSakit rs = db.listRS.get(position);
                saver.RS = rs;
                db.getQueryKamar("Kamar", position);
                while (!db.taskQueryKamar.isComplete()) ;
                startActivity(new Intent(getApplicationContext(), ListKamarActivity.class));
            }
        });
    }

    private void filterList(String newText) {
        ArrayList<RumahSakit> filteredList = new ArrayList<>();
        for (RumahSakit rs : db.listRS){
            if (rs.getNama().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(rs);
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }else{
            adapter.setFilteredList(filteredList);
        }
    }
    private boolean checkBookmarkExist(RumahSakit rs){
        for (int i = 0; i < db.listBookmarks.size(); i++){
            if (rs.getRSID().equals(db.listBookmarks.get(i).getRSID())){
                return true;
            }
        }
        return false;
    }
    private void addRSToListBookmark(RumahSakit rs){
        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
        Map<String, Object> mapBookmark = new HashMap<>();
        mapBookmark.put("CustomerID", firestoreDB.document("Customer/" + db.currentUser.getId()));
        mapBookmark.put("RSID", firestoreDB.document("RumahSakit/" + rs.getRSID()));
        if (!checkBookmarkExist(rs)) {
            firestoreDB.collection("Bookmark")
                    .document()
                    .set(mapBookmark)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(PesanActivity.this, "Bookmark berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });
            db.listBookmarks.add(rs);
        }else{
            Toast.makeText(this, "Bookmark already exists", Toast.LENGTH_SHORT).show();
        }
    }
}