package com.example.clickmedical;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HistoryActivity extends AppCompatActivity {
    ListView histories;
    FirebaseFirestore firestoreDB;
    SearchView searchBar;
    CustomBaseAdapterHist adapter;
    private DBHelper dbHelp;
    private ArrayList<History> histList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        dbHelp = new DBHelper();
        ArrayList<String> namaHospitals = new ArrayList<>();
        histList = new ArrayList<>();
        firestoreDB = FirebaseFirestore.getInstance();
        histories = findViewById(R.id.histListView);
        searchBar = findViewById(R.id.searchBar);
        searchBar.clearFocus();
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                HistoryActivity.this.adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });

        dbHelp.getQueryRumahSakit("RumahSakit");
        while(!dbHelp.taskQueryRS.isComplete());
        dbHelp.getAllQueryKamar("Kamar");
        while(!dbHelp.taskQueryKamar.isComplete());
        String userID = dbHelp.currentUser.getId();

        // query rumah sakit
        firestoreDB.collection("Pesanan")
                .whereEqualTo("customerID", firestoreDB.document("Customer/"+userID))//syarat query customerID harus sama dengan id user yang lagi login
                .whereEqualTo("accepted", true)// syarat query harus pesanan yang sudah di acc
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                // ambil data dari hasil query
                                String kamarID = document.getDocumentReference("kamarID").getId();
                                Kamar kamarTarget = searchKamarbyID(dbHelp.listKamar, kamarID);
                                RumahSakit rsTarget = searchRumahSakitbyID(dbHelp.listRS, kamarTarget.getRSID());
                                Date tanggalPesan = document.getTimestamp("tanggal").toDate();

                                // tambahkan data history ke dalam ArrayList
                                namaHospitals.add(rsTarget.getNama());
                                histList.add(new History(dbHelp.currentUser.getNama(), kamarTarget.getNama(), rsTarget.getNama(),
                                        dbHelp.currentUser.getId(), kamarTarget.getKamarId(), rsTarget.getRSID(), tanggalPesan));
                            }
                            // kode yang akan dieksekusi setelah selesai query
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            // mengambil data image
                            int[] imagesRS = getImageRS(dbHelp.listRS, namaHospitals);

                            // nempel data history ke dalam ListView
                            adapter = new CustomBaseAdapterHist(getApplicationContext(), histList, imagesRS);
                            histories.setAdapter(adapter);
                        }else{
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
    private void filterList(String newText) {
        // filter berdasarkan text yang diinput user di tempat search
        ArrayList<History> filteredList = new ArrayList<>();
        for (History hist : histList){
            if (hist.getNamaRS().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(hist);
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }else{
            adapter.setFilteredList(filteredList);
        }
    }

    public RumahSakit searchRumahSakitbyID(ArrayList<RumahSakit> list, String id){
        /*
        *  mencari rumah sakit berdasarkan id rumah sakit
        * */
        for (RumahSakit rs: list){
            if (rs.getRSID().equals(id)){
                return rs;
            }
        }
        return null;
    }
    public Kamar searchKamarbyID(ArrayList<Kamar> list, String id){
        // mencari kamar berdasarkan id kamar
        for (Kamar room: list){
            if (room.getKamarId().equals(id)){
                return room;
            }
        }
        return null;
    }
    public int[] getImageRS(ArrayList<RumahSakit> hospitals, ArrayList<String> namaHospitals){
        // mengambil image rumah sakit berdasarkan nama yang ada pada list namaHospitals

        int[] result = new int[namaHospitals.size()];
        DBHelper dbHelp = new DBHelper();

        for (int i = 0; i<namaHospitals.size();i++){
            int idx_target = 0;
            for (int j = 1; j< hospitals.size();j++){
                if (namaHospitals.get(i).equals(hospitals.get(j).getNama())){
                    idx_target = j;
                }
            }
            result[i] = dbHelp.imagesRS[idx_target];
        }
        return result;
    }
}