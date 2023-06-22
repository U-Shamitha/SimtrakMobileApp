package com.mad_lab.a1_loginpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;


public class DashboardActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        toolbar = findViewById(R.id.dasboard_toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.OpenDrawer, R.string.CloseDrawer);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        loadFragment();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if(itemId==R.id.trainees){
                    Toast.makeText(DashboardActivity.this, "Trainees", Toast.LENGTH_SHORT).show();
                }
                else if(itemId==R.id.task_management){
                    Toast.makeText(DashboardActivity.this, "Task Management", Toast.LENGTH_SHORT).show();
                }
                else if(itemId==R.id.wfh){
                    Toast.makeText(DashboardActivity.this, "Work From Home", Toast.LENGTH_SHORT).show();
                }
                else if(itemId==R.id.profile){
                    Toast.makeText(DashboardActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                    loadFragment(new ProfileFragment());
                }
                else if(itemId==R.id.logout){
//                    Toast.makeText(DashboardActivity.this, "Logout", Toast.LENGTH_SHORT).show();

                    SharedPreferences sharedPreferences = getSharedPreferences("login_details", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLogin",false);
                    editor.apply();
                    startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                }
                else{
//                    Toast.makeText(DashboardActivity.this, "Dashboard", Toast.LENGTH_SHORT).show();
                    loadFragment(new DashboardHomeFragment());
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        new MenuInflater(this).inflate(R.menu.dasboard_menu_items, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        int itemId = item.getItemId();
//
//
//        if(itemId==R.id.trainees){
//            Toast.makeText(this, "Trainees", Toast.LENGTH_SHORT).show();
//        }
//        else if(itemId==R.id.task_management){
//            Toast.makeText(this, "Task Management", Toast.LENGTH_SHORT).show();
//        }
//        else if(itemId==R.id.wfh){
//            Toast.makeText(this, "Work From Home", Toast.LENGTH_SHORT).show();
//        }
//        else if(itemId==R.id.profile){
//            Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
//        }
//        else if(itemId==R.id.logout){
//            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
//        }
//        else{
//            Toast.makeText(this, "Dashboard", Toast.LENGTH_SHORT).show();
//        }
//        return super.onOptionsItemSelected(item);
//    }

    }

    public void loadFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
//        ft.replace(R.id.dashboardFragment_container, new DashboardHomeFragment());
        ft.replace(R.id.dashboardFragment_container, new ProfileProfile3Fragment());
        ft.commit();
    }

    public void loadFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.dashboardFragment_container, fragment);
        ft.commit();
    }
}