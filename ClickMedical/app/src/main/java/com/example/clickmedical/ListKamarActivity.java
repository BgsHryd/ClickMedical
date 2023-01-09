package com.example.clickmedical;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class ListKamarActivity extends AppCompatActivity {
    private TextView title;
    public static RumahSakit RSTarget;
    int RSImages[] = {R.drawable.hospital_bed, R.drawable.hospital_bed, R.drawable.hospital_bed};
    ListView beds;
    HelperSaver saver;
    DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // inisialisasi variabel
        saver = new HelperSaver();
        db = new DBHelper();

        // setting agar layout activity_list_kamar muncul
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_kamar);

        // nampilin data ke layar
        title = (TextView) findViewById(R.id.titleRS);
        beds = (ListView) findViewById(R.id.kamarListView);
        title.setText(saver.getRumahSakit().getNama());
        // masukkin data ke dalam ListView
        CustomBaseAdapterKamar adapter = new CustomBaseAdapterKamar(getApplicationContext(),
                db.listKamar, db.imagesKamar.get(saver.getRumahSakit().getNama()));
        beds.setAdapter(adapter);

        // case kalo item ListView nya dipencet
        beds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // simpen data kamar yang ditekan ke dalam HelperSaver
                saver.kamar = db.listKamar.get(position);
                saver.imageKamar = db.imagesKamar.get(saver.getRumahSakit().getNama())[position];

                // pindah layout ke InfoKamarActivity
                startActivity(new Intent(getApplicationContext(), InfoKamarActivity.class));
            }
        });
    }
}