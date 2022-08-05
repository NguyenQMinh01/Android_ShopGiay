package com.example.doan_n14;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doan_n14.service.UserService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    //FirebaseDatabase rootNode;
    //DatabaseReference reference;
    FirebaseFirestore db;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        final TextInputLayout edtName = findViewById(R.id.editTextName);
        final TextInputLayout edtEmail = findViewById(R.id.editText);
        final TextInputLayout edtUsername = findViewById(R.id.editText2);
        final TextInputLayout edtPassword = findViewById(R.id.editText3);
        final TextInputLayout edtConfirm = findViewById(R.id.editText4);
        final TextInputLayout edtPhoneN = findViewById(R.id.editTextP);
        final Button btnSignIn = findViewById(R.id.btnDangKy);
        final Button btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getEditText().getText().toString();
                String username = edtUsername.getEditText().getText().toString();
                String email = edtEmail.getEditText().getText().toString();
                String phone = edtPhoneN.getEditText().getText().toString();
                String pass = edtPassword.getEditText().getText().toString();
                String confirm = edtConfirm.getEditText().getText().toString();
                if(username.isEmpty()){
                    edtUsername.setError("Username cannot be null ");
                    return;
                }
                if(pass.isEmpty()){
                    edtPassword.setError("Password required");
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    edtEmail.setError("pleas provide is required");
                    edtEmail.requestFocus();
                    return;
                }
                if(confirm.isEmpty()){
                    edtPassword.setError("confirm required");
                    return;
                }

                if(!pass.equals(confirm))
                {
                   edtConfirm.setError("not confirm password true");
                   return;
                }
                if (edtPassword.getEditText().getText().toString().equals(edtConfirm.getEditText().getText().toString()))
                {

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://chettrongvovong.herokuapp.com/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    final UserService service = retrofit.create(UserService.class);

                    User user = new User(edtName.getEditText().getText().toString(),edtUsername.getEditText().getText().toString()
                    ,edtEmail.getEditText().getText().toString(),edtPhoneN.getEditText().getText().toString()
                            ,edtPassword.getEditText().getText().toString());
                    Call<User> createCall = service.create(user);
                    createCall.enqueue(new Callback<User>() {
                                           @Override
                                           public void onResponse(Call<User> _, Response<User> resp) {
                                           }

                                           @Override
                                           public void onFailure(Call<User> _, Throwable t) {
                                               t.printStackTrace();
                                           }
                                       });


                    mAuth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {
                                        User user = new User(name,username,email,phone,pass);
                                        userID = mAuth.getCurrentUser().getUid();
                                        DocumentReference documentReference = db.collection("user").document(userID);
                                        Map<String ,Object> items = new HashMap<>();
                                        items.put("name",name);
                                        items.put("username",username);
                                        items.put("email",email);
                                        items.put("phone",phone);
                                        items.put("password",pass);
                                        documentReference.set(items).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d(TAG,"onsuccessfully"+ userID);
                                            }
                                        });
                                        documentReference.set(items).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d(TAG,"onfailue"+ e.toString());
                                            }
                                        });
                                        FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    Toast.makeText(RegisterActivity.this, "User has been register succefully", Toast.LENGTH_SHORT).show();

                                                }
                                                else
                                                {
                                                    Toast.makeText(RegisterActivity.this, "Failed to register! try again", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }
                                    else
                                    {
                                        Toast.makeText(RegisterActivity.this, "Failed to register", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    }
                else
                {
                    Toast.makeText(RegisterActivity.this,"Fail",Toast.LENGTH_SHORT).show();
                }


            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  =new Intent(RegisterActivity.this,DangNhapActivity.class);
                startActivity(intent);
            }
        });
    }



    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

}