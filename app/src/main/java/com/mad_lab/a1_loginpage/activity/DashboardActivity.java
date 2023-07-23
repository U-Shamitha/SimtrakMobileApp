package com.mad_lab.a1_loginpage.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.mad_lab.a1_loginpage.R;
import com.mad_lab.a1_loginpage.fragments.home.AddNoteFragment;
import com.mad_lab.a1_loginpage.fragments.home.DashboardAddTaskFragment;
import com.mad_lab.a1_loginpage.fragments.home.DashboardHomeFragment;
import com.mad_lab.a1_loginpage.fragments.home.EditNoteFragment;
import com.mad_lab.a1_loginpage.fragments.home.EditTaskFragment;
import com.mad_lab.a1_loginpage.fragments.home.ViewNotesFragment;
import com.mad_lab.a1_loginpage.fragments.profile.ProfileProfile3Fragment;
import com.mad_lab.a1_loginpage.fragments.trainees.AddJournalFragment;
import com.mad_lab.a1_loginpage.fragments.trainees.JournalFragment;


public class DashboardActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;



    Boolean isTraineesSelected= false;
    Boolean isHomeSelected= false;

    private TextView selected_page_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        toolbar = findViewById(R.id.dasboard_toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        selected_page_tv = findViewById(R.id.selected_page_tv);



        // toolbar start

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.OpenDrawer, R.string.CloseDrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.userName_nav_tv);
        navUsername.setText(getDataFromSharedPrefernces("firstName")+" "+getDataFromSharedPrefernces("lastName"));

        // toolbar end


        //Loading Fragemnt

        // Get the Intent that started this activity
        Intent intent = getIntent();

        // Retrieve the extras from the intent
        Bundle extras = intent.getExtras();

        if (extras != null) {
            // Extract the value associated with the specific key
            String fragmentType = extras.getString("DesFragment");

            // Load the corresponding fragment based on the extracted value
            if ("EditTask".equals(fragmentType)) {
                loadFragment(new EditTaskFragment());
            } else if ("ViewNotes".equals(fragmentType)) {
                loadFragment(new ViewNotesFragment());
            } else if ("AddNote".equals(fragmentType)) {
                loadFragment(new AddNoteFragment());
            }else if ("EditNote".equals(fragmentType)) {
                loadFragment(new EditNoteFragment());
            }
        }else{
            loadFragment();
        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

              int itemId = item.getItemId();

//              selected_page_tv.setText("");

              if(itemId==R.id.home){
//                    Toast.makeText(DashboardActivity.this, "Home", Toast.LENGTH_SHORT).show();
                    if (!isHomeSelected) {
                        navigationView.getMenu().clear();
                        navigationView.inflateMenu(R.menu.dasboard_menu_home_sel);
                        if(navigationView.getMenu().getItem(0).getActionView()!= null) {
                            navigationView.getMenu().getItem(0).getActionView().setRotation(90f);
                        }
                        isHomeSelected= true;
                        isTraineesSelected = false;
                    } else {
                        navigationView.getMenu().clear();
                        navigationView.inflateMenu(R.menu.dasboard_menu_items);
                        if(navigationView.getMenu().getItem(0).getActionView()!= null) {
                            navigationView.getMenu().getItem(0).getActionView().setRotation(0f);
                        }
                        isHomeSelected= false;
                    }
              }
              else if (itemId == R.id.dashboard) {
                  selected_page_tv.setText("Dashboard");
                  loadFragment();
              }
              else if (itemId == R.id.add_task) {
                  selected_page_tv.setText("Add Task");
                  loadFragment(new DashboardAddTaskFragment());
              }
              else if(itemId==R.id.trainees_main){
//                    Toast.makeText(DashboardActivity.this, "Trainees", Toast.LENGTH_SHORT).show();
                    if (!isTraineesSelected) {
                        navigationView.getMenu().clear();
                        navigationView.inflateMenu(R.menu.dasboard_menu_trainees_sel);
                        if(navigationView.getMenu().getItem(3).getActionView()!= null) {
                            navigationView.getMenu().getItem(3).getActionView().setRotation(90f);
                        }
//                        item.getActionView().setRotation(90f);
//                        navigationView.getMenu().setGroupVisible(R.id.trainees_grp2, true);
//                        navigationView.getMenu().getItem(2).setVisible(true);
                        isTraineesSelected= true;
                        isHomeSelected = false;
                    } else {
                        navigationView.getMenu().clear();
                        navigationView.inflateMenu(R.menu.dasboard_menu_items);
                        if(navigationView.getMenu().getItem(3).getActionView()!= null) {
                            navigationView.getMenu().getItem(3).getActionView().setRotation(0f);
                        }
//                        item.getActionView().setRotation(0f);
//                        navigationView.getMenu().setGroupVisible(R.id.trainees_grp2, false);
//                        navigationView.getMenu().getItem(2).setVisible(false);
                        isTraineesSelected= false;
                    }
                }
                else if(itemId==R.id.daily_journal){
                  selected_page_tv.setText("Daily Journal");
                  loadFragment(new JournalFragment());
                }else if(itemId==R.id.add_journal){
                  selected_page_tv.setText("Add Journal");
                  loadFragment(new AddJournalFragment());
              }
                else if(itemId==R.id.task_management){
                    Toast.makeText(DashboardActivity.this, "Management", Toast.LENGTH_SHORT).show();
                }
//                else if(itemId==R.id.wfh){
//                    Toast.makeText(DashboardActivity.this, "Work From Home", Toast.LENGTH_SHORT).show();
//                }
                else if(itemId==R.id.profile){
//                    Toast.makeText(DashboardActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                    selected_page_tv.setText("Your Profile");
                    loadFragment(new ProfileProfile3Fragment());
                }
                else if(itemId==R.id.logout){
//                    Toast.makeText(DashboardActivity.this, "Logout", Toast.LENGTH_SHORT).show();

                    SharedPreferences sharedPreferences = getSharedPreferences("login_details", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLogin",false);
                    editor.putString("userId","");
                    editor.apply();
                    startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                    finish();
                }
                else {
//                    Toast.makeText(DashboardActivity.this, "Dashboard", Toast.LENGTH_SHORT).show();
//                    loadFragment();
                }

                if(itemId!=R.id.home && itemId!=R.id.trainees_main){

                    isHomeSelected = false;
                    isTraineesSelected = false;
                    drawerLayout.closeDrawer(GravityCompat.START);
                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.dasboard_menu_items);

                }

                return true;
            }
        });

    }


    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{

            int fragments = getSupportFragmentManager().getBackStackEntryCount();
            if (fragments == 1) {
                finish();
            }else {
                super.onBackPressed();
            }

        }

    }

    public String getDataFromSharedPrefernces(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

    public void loadFragment(){
        selected_page_tv.setText("Dashboard");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
//        ft.replace(R.id.dashboardFragment_container, new DashboardHomeFragment());
        ft.add(R.id.dashboardFragment_container, new DashboardHomeFragment());
        fm.popBackStack("root_fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        ft.addToBackStack("root_fragment");
        ft.commit();
    }

    public void loadFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.dashboardFragment_container, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void loadFragment(Fragment fragment, String fragmentName){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.dashboardFragment_container, fragment);
//        fm.popBackStack(fragmentName, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        ft.addToBackStack(fragmentName);
        ft.commit();
    }
}