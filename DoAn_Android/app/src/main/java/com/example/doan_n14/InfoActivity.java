package com.example.doan_n14;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InfoActivity extends AppCompatActivity {

    TextInputLayout edtName, edtEmail, edtPass, edtUsername, edtPhoneN;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("user");
        userID = user.getUid();

        edtName = findViewById(R.id.editText4);
        edtEmail = findViewById(R.id.editText);
        edtUsername = findViewById(R.id.editText2);
        edtPhoneN = findViewById(R.id.editTextP);
        edtPass = findViewById(R.id.editText3);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user1 = snapshot.getValue(User.class);
                if(user1 != null)
                {
                    String Name = user1.name;
                    String Email = user1.email;
                    String Username = user1.username;
                    String Phone = user1.phone;
                    String Pass = user1.password;

                    edtName.getEditText().setText(Name);
                    edtEmail.getEditText().setText(Email);
                    edtUsername.getEditText().setText(Username);
                    edtPhoneN.getEditText().setText(Phone);
                    edtPass.getEditText().setText(Pass);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void detail() {
        Intent intent = getIntent();
        String user_name = intent.getStringExtra("name");
        String user_email = intent.getStringExtra("email");
        String user_uname = intent.getStringExtra("username");
        String user_phone = intent.getStringExtra("phone");
        String user_pass = intent.getStringExtra("pass");

        edtName.getEditText().setText(user_name);
        edtEmail.getEditText().setText(user_email);
        edtUsername.getEditText().setText(user_uname);
        edtPhoneN.getEditText().setText(user_phone);
        edtPass.getEditText().setText(user_pass);
    }
}