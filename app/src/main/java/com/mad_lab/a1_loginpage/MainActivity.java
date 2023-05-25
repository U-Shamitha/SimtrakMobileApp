package com.mad_lab.a1_loginpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    private TextView welcome_tv;
    private Button logout;
    public static String loggedInUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("login_details", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  sharedPref.edit();

        welcome_tv = findViewById(R.id.welcome_tv);
        logout = findViewById(R.id.logout);

        SignUpActivity.database.getReference("Users").child(SignUpActivity.loggedInUserEmail.split("@")[0])
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            Users loggedInUser = snapshot.getValue(Users.class);
                            loggedInUserName = loggedInUser.userName;
                            welcome_tv.setText("Welcome " + loggedInUserName);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainActivity.this, "Unable to fetch user data", Toast.LENGTH_SHORT).show();
                    }
                });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("isLogin",false);
                editor.apply();
                Toast.makeText(MainActivity.this, "logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });

    }
}