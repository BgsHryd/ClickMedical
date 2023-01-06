package com.example.clickmedical;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends AppCompatActivity {
    private FirebaseUser firebaseUser;
    private TextView name;
    private ImageButton pesanBtn, recBtn, profilebtn;
    private ImageButton histBtn, bookmarkBtn;
    DBHelper db;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        db = new DBHelper();
        name = findViewById(R.id.name);
        profilebtn = findViewById(R.id.profileButton);
        pesanBtn = findViewById(R.id.pesanButton);
        recBtn = findViewById(R.id.rekomenButton);
        histBtn = findViewById(R.id.historyButton);
        bookmarkBtn = findViewById(R.id.bookmarkButton);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db.getCurrentCustomerData(firebaseUser.getEmail());
        while(!db.taskQueryCustomer.isSuccessful());
        System.out.println(db.currentUser);

        if (firebaseUser != null) {
            name.setText(firebaseUser.getDisplayName());
        }else{
            name.setText("Login gagal");
        }

        profilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });
        pesanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PesanActivity.class));
            }
        });
        recBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RecommendActivity.class));
            }
        });
        histBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
            }
        });
        bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BookmarkActivity.class));
            }
        });
    }
}