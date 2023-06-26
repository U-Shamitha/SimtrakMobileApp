package com.mad_lab.a1_loginpage.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.mad_lab.a1_loginpage.R;
import com.mad_lab.a1_loginpage.model.Users;

public class SignUpActivity extends AppCompatActivity {

    private TextInputLayout name_tl;
    private TextInputLayout email_tl;
    private TextInputLayout password_tl;
    private TextInputEditText name_et;
    private TextInputEditText email_et;
    private TextInputEditText password_et;
    private Button signup_bt;
    private TextView login_tv;
    public static String loggedInUserEmail;

    FirebaseAuth auth;
    public static FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);


        if(getApplicationContext().getSharedPreferences("login_details", Context.MODE_PRIVATE).getBoolean("isLogin", false)){
            loggedInUserEmail = getApplicationContext().getSharedPreferences("login_details", Context.MODE_PRIVATE).getString("userEmail","");
            if(!loggedInUserEmail.equals("")) {
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            }
        }

        name_tl = findViewById(R.id.userName_tl_signup);
        email_tl = findViewById(R.id.email_tl_signup);
        password_tl = findViewById(R.id.password_tl_signup);
        name_et = findViewById(R.id.userName_et_signup);
        email_et = findViewById(R.id.email_et_signup);
        password_et = findViewById(R.id.password_et_signup);
        signup_bt = findViewById(R.id.signup_bt);
        login_tv = findViewById(R.id.login_tv);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        name_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
//                Toast.makeText(LoginActivity.this, "on click email", Toast.LENGTH_SHORT).show();
                int color;
                if(b){
                    color = Color.parseColor("#0099ff");
                }else{
                    color = Color.parseColor("#808080");
                }
                name_tl.setStartIconTintList(ColorStateList.valueOf(color));
                name_tl.setHintTextColor(ColorStateList.valueOf(color));

            }
        });
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

        signup_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(SignUpActivity.this, "signup button clicked", Toast.LENGTH_SHORT).show();

                String name = name_et.getText().toString().trim();
                String email = email_et.getText().toString().trim();
                String password = password_et.getText().toString().trim();

                if(name.isEmpty()){
                    name_et.setError("Name cannot be empty");
                }
                else if(email.isEmpty()){
                    email_et.setError("Email cannot be empty");
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    email_et.setError("Please enter valid email");
                }
                else if(password.isEmpty()){
                    password_et.setError("Password cannot be empty");
                }
                else if(password.length()<6){
                    password_et.setError("Password should be at least 6 characters long");
                }
                else{

                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Toast.makeText(SignUpActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                    database.getReference("Users").child(email.split("@")[0]).setValue(new Users(name, email));
                                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                    finish();
                                }
                            });

                }

            }
        });


        login_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }
}