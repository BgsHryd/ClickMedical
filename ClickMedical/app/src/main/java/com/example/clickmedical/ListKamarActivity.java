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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        HelperSaver saver = new HelperSaver();
        DBHelper db = new DBHelper();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_kamar);
        title = (TextView) findViewById(R.id.titleRS);
        beds = (ListView) findViewById(R.id.kamarListView);
        title.setText(saver.getRumahSakit().getNama());
        CustomBaseAdapterKamar adapter = new CustomBaseAdapterKamar(getApplicationContext(),
                db.listKamar, db.imagesKamar.get(saver.getRumahSakit().getNama()));
        beds.setAdapter(adapter);
        beds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                saver.kamar = db.listKamar.get(position);
                saver.imageKamar = db.imagesKamar.get(saver.getRumahSakit().getNama())[position];
                startActivity(new Intent(getApplicationContext(), InfoKamarActivity.class));
            }
        });
    }
}