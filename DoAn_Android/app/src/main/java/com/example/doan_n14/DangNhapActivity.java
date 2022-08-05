package com.example.doan_n14;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doan_n14.ultils.Ultils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class DangNhapActivity extends AppCompatActivity {
    TextInputLayout edtEmail;
    TextInputLayout edtPassword;
    Button btnRegister, btnLogin,btnOK, btnCancel, btnLoginFinger;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_dang_nhap);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        edtEmail = findViewById(R.id.editText);
        edtPassword = findViewById(R.id.editText1);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnLoginFinger = findViewById(R.id.btnLoginFinger);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nametxt = edtEmail.getEditText().getText().toString();
                final String passwordtxt = edtPassword.getEditText().getText().toString();

                if(edtEmail.getEditText().getText().toString().isEmpty() ||
                        edtPassword.getEditText().getText().toString().isEmpty()){
                    final Dialog dialog = new Dialog(DangNhapActivity.this);
                    dialog.setContentView(R.layout.dialog_custom);
                    btnOK = dialog.findViewById(R.id.btnOK);
                    btnCancel = dialog.findViewById(R.id.btnCancel);
                    btnOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(DangNhapActivity.this,
                                    RegisterActivity.class);
                            startActivityForResult(intent, 100);

                            dialog.dismiss();

                        }
                    });
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                      @Override
                     public void onClick(View view) {
                       dialog.cancel();
                    }
                    });
                    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.WRAP_CONTENT);
                    dialog.show();
                }else if(edtPassword.getEditText().getText().toString().length() < 6){
                    edtPassword.setError("Minimum 6 number");
                }else {
                    mAuth.signInWithEmailAndPassword(nametxt, passwordtxt)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {
                                        Intent intent = new Intent(DangNhapActivity.this,TrangChinhActivity.class);
                                        Toast.makeText(DangNhapActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                        intent.putExtra("email",edtEmail.getEditText().getText().toString());
                                        Ultils.current_user = edtEmail.getEditText().getText().toString();
                                        startActivity(intent);
                                    }else
                                    {
                                        Toast.makeText(DangNhapActivity.this, "Đăng nhập thất bại! Hãy thử lại", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DangNhapActivity.this,
                        RegisterActivity.class);
                startActivityForResult(intent, 100);
            }
        });

        btnLoginFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DangNhapActivity.this, LoginFingerActivity.class);
                startActivity(intent);
            }
        });


    }


}