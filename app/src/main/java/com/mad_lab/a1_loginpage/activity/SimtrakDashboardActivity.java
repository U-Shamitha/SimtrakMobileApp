package com.mad_lab.a1_loginpage.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebViewClient;

import com.mad_lab.a1_loginpage.databinding.ActivitySimtrakDashboardBinding;

public class SimtrakDashboardActivity extends AppCompatActivity {

    private ActivitySimtrakDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySimtrakDashboardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("login_details", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLogin",false);
                editor.apply();
                startActivity(new Intent(SimtrakDashboardActivity.this, LoginActivity.class));
            }
        });

        simtrakDashboardWebViewSetup();
    }

    private void simtrakDashboardWebViewSetup(){

        binding.simtrakDashboardWebView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.simtrakDashboardWebView.getSettings().setSafeBrowsingEnabled(true);
        }
        binding.simtrakDashboardWebView.setWebViewClient(new WebViewClient());
        binding.simtrakDashboardWebView.loadUrl("https://adore.simtrak.in/dash.php");
    }

    @Override
    public void onBackPressed() {
        if(binding.simtrakDashboardWebView.canGoBack()){
            binding.simtrakDashboardWebView.goBack();
        }else{
            super.onBackPressed();
        }
    }
}