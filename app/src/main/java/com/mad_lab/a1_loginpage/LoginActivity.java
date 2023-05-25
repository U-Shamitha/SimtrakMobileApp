package com.mad_lab.a1_loginpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText email_et;
    private TextInputEditText password_et;
    private TextInputLayout email_tl;
    private TextInputLayout password_tl;
    private Button login_bt;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_et = findViewById(R.id.email_et_login1);
        password_et = findViewById(R.id.password_et_login1);
        login_bt = findViewById(R.id.login_bt);
        auth = FirebaseAuth.getInstance();
        email_tl = findViewById(R.id.email_tl);
        password_tl = findViewById(R.id.password_tl);

        SharedPreferences sharedPref = LoginActivity.this.getSharedPreferences("login_details", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("Login",false);
        editor.apply();


        email_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
//                Toast.makeText(LoginActivity.this, "on click email", Toast.LENGTH_SHORT).show();
                int color;
                if(b){
                    color = Color.parseColor("#0099ff");
                }else{
                    color = Color.parseColor("#808080");
                }
                email_tl.setStartIconTintList(ColorStateList.valueOf(color));
                email_tl.setHintTextColor(ColorStateList.valueOf(color));

            }
        });
        password_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
//                Toast.makeText(LoginActivity.this, "on click email", Toast.LENGTH_SHORT).show();
                int color;
                if(b){
                    color = Color.parseColor("#0099ff");
                }else{
                    color = Color.parseColor("#808080");
                }
                password_tl.setEndIconTintList(ColorStateList.valueOf(color));
                password_tl.setHintTextColor(ColorStateList.valueOf(color));
            }
        });

        login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(LoginActivity.this, "login button clicked", Toast.LENGTH_SHORT).show();

                String email = email_et.getText().toString().trim();
                String password = password_et.getText().toString().trim();

                if (email.isEmpty()) {
                    email_et.setError("Email cannot be empty");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    email_et.setError("Please enter valid email");
                } else if (password.isEmpty()) {
                    password_et.setError("Password cannot be empty");
                } else if (password.length() < 6) {
                    password_et.setError("Password should be at least 6 characters long");
                } else {

                    auth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    editor.putBoolean("isLogin", true);
                                    editor.putString("userEmail",email);
                                    editor.apply();
                                    SignUpActivity.loggedInUserEmail= email;
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }
        });
    }

}