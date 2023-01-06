package com.example.clickmedical;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        DBHelper dbHelp = new DBHelper();
        ArrayList<String> namaHospitals = new ArrayList<>();
        ArrayList<History> histList = new ArrayList<>();
        firestoreDB = FirebaseFirestore.getInstance();
        histories = findViewById(R.id.histListView);

        dbHelp.getQueryRumahSakit("RumahSakit");
        while(!dbHelp.taskQueryRS.isComplete());
        dbHelp.getAllQueryKamar("Kamar");
        while(!dbHelp.taskQueryKamar.isComplete());
        String userID = dbHelp.currentUser.getId();

        firestoreDB.collection("Pesanan")
                .whereEqualTo("customerID", firestoreDB.document("Customer/"+userID))
                .whereEqualTo("accepted", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                String kamarID = document.getDocumentReference("kamarID").getId();
                                Kamar kamarTarget = searchKamarbyID(dbHelp.listKamar, kamarID);
                                RumahSakit rsTarget = searchRumahSakitbyID(dbHelp.listRS, kamarTarget.getRSID());
                                Date tanggalPesan = document.getTimestamp("tanggal").toDate();
                                namaHospitals.add(rsTarget.getNama());
                                histList.add(new History(dbHelp.currentUser.getNama(), kamarTarget.getNama(), rsTarget.getNama(),
                                        dbHelp.currentUser.getId(), kamarTarget.getKamarId(), rsTarget.getRSID(), tanggalPesan));
                            }
                            // lanjut kode disini
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            int[] imagesRS = getImageRS(dbHelp.listRS, namaHospitals);
                            CustomBaseAdapterHist adapter = new CustomBaseAdapterHist(getApplicationContext(), histList, imagesRS);
                            histories.setAdapter(adapter);
                        }else{
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    public RumahSakit searchRumahSakitbyID(ArrayList<RumahSakit> list, String id){
        for (RumahSakit rs: list){
            if (rs.getRSID().equals(id)){
                return rs;
            }
        }
        return null;
    }
    public Kamar searchKamarbyID(ArrayList<Kamar> list, String id){
        for (Kamar room: list){
            if (room.getKamarId().equals(id)){
                return room;
            }
        }
        return null;
    }
    public int[] getImageRS(ArrayList<RumahSakit> hospitals, ArrayList<String> namaHospitals){
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