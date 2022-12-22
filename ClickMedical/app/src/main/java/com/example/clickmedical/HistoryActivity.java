package com.example.clickmedical;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

public class HistoryActivity extends AppCompatActivity {
    String RSList[] = {"Hasan Sadikin", "Immanuel", "Santosa"};
    int RSImages[] = {R.drawable.hospital_bed, R.drawable.hospital_bed, R.drawable.hospital_bed};

    ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        listview = (ListView) findViewById(R.id.histListView);
        CustomBaseAdapter adapter = new CustomBaseAdapter(getApplicationContext(), RSList, RSImages);
        listview.setBackgroundResource(R.drawable.custom_shape);
        listview.setAdapter(adapter);
    }
}