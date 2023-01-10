package com.example.clickmedical;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.clickmedical.R.id;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText email, password, confPassword, fullName;
    Button regBtn, loginBtn;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // inisialisasi variabel yang akan digunakan
        email = (EditText) findViewById(R.id.inputEmail);
        password = (EditText) findViewById(R.id.inputPassword);
        confPassword = (EditText) findViewById(R.id.inputConfirm);
        fullName = (EditText) findViewById(R.id.inputName);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(Register.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Silahkan tunggu dulu sebentar");
        progressDialog.setCancelable(true);

        regBtn = (Button) findViewById(R.id.registerButton);
        loginBtn = (Button) findViewById(R.id.LoginButton);

        // case kalo tombol login ditekan
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // pindah activity ke login
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
            }
        });
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ambil data yang udah user isiin
                String mail = email.getText().toString();
                String name = fullName.getText().toString();
                String pass = password.getText().toString();
                String repass = confPassword.getText().toString();

                if (mail.equals("") || pass.equals("") || name.equals("") || repass.equals("")){ // case kalo ada form yang masi kosong
                    Toast.makeText(Register.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                }else{
                    // check kalo form password sama confirm password udah sama atau belum
                    if (pass.equals(repass)){
                        // lebih detailnya rujuk ke method register dibawah
                        register(name, mail, pass);
                    }else{
                        Toast.makeText(Register.this, "Password input is not match", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
    private void register(String fullName, String email, String password){
        // tampilkan progress dialog
        progressDialog.show();

        // bantuan firebase buat bikin user dengan email
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){ // kalo udah berhasil buat user
                    FirebaseUser user = task.getResult().getUser();
                    if (user != null) {
                        // ubah data profile
                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                .setDisplayName(fullName)
                                .build();
                        user.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // kalo dah beres buat user dan setting data pindah ke Ho
                                addCustomerToDatabase(fullName, email, "Customer");
                                reload();
                            }
                        });
                    }else{
                        Toast.makeText(getApplicationContext(), "Register Gagal", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }
    private void addCustomerToDatabase(String nama, String email, String dbName){
        // nambahin customer ked alam database
        Map<String, Object> data = new HashMap<>();
        data.put("nama", nama);
        data.put("email", email);

        // syntax untuk menambahkan user ke dalam firebase
        db.collection(dbName)
        .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
    private void reload(){
        // buat pindah ke Home
        Intent i;
        i = new Intent(getApplicationContext(), Home.class);
        startActivity(i);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

            reload();
        }
    }
}