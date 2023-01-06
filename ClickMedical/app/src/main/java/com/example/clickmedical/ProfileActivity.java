package com.example.clickmedical;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseUser firebaseUser;
    private TextView name;
    private Button logOutbtn, historyBtn;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // inisialisasi variabel yang bakal dipake
        db = new DBHelper();
        name = findViewById(R.id.nameOfUser);

        // ambil data user yang lagi login
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            // tampilin nama user
            name.setText(firebaseUser.getDisplayName());
        }else{
            // ini harusnya engga mungkin mengingat logic di login dan register
            name.setText("Tidak mungkin");
        }

        // setting logout button
        logOutbtn = findViewById(R.id.logOutButton);
        logOutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });

        // setting buat pindah ke layar history
        historyBtn = findViewById(R.id.historyButton);
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
            }
        });
    }
}