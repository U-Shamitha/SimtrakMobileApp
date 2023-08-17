package com.mad_lab.a1_loginpage.activity.trainees;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mad_lab.a1_loginpage.R;
import com.mad_lab.a1_loginpage.activity.DashboardActivity;
import com.mad_lab.a1_loginpage.adapter.RecyclerJournalAdapter;
import com.mad_lab.a1_loginpage.adapter.RecyclerLeavesAdapter;
import com.mad_lab.a1_loginpage.model.JournalModel;
import com.mad_lab.a1_loginpage.model.LeavesModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class ViewLeavesActivity extends AppCompatActivity {

    ImageButton leaveFilter_btn;
    ImageButton addLeave_btn;
    LinearLayout search_ll;
    SearchView searchLeave;

    ImageView search_iv;

    RecyclerView leavesRecyclerView;
    RecyclerLeavesAdapter leaveArrAdapter ;
    ArrayList<LeavesModel> leaveArrayList;

    String TAG = "ViewLeavesFragment";
    String searchLeave_txt="";
    String searchLeave_ctxt="";
    boolean desSort = false;
    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_leaves);

        leaveFilter_btn = findViewById(R.id.leaveFilter_btn);
        addLeave_btn = findViewById(R.id.addLeave_btn);
        search_ll = findViewById(R.id.search_ll);
        searchLeave = findViewById(R.id.search_leave);
        search_iv = findViewById(R.id.search_iv);
        leavesRecyclerView = findViewById(R.id.leaves_recyclerView);

        leavesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        leaveArrayList = new ArrayList<>();

        fstore = FirebaseFirestore.getInstance();

        //Task filter start
        leaveFilter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenuForJournalFilter(view);
            }
        });

        //Task filter end


        //Search bar
        searchLeave.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                //                Toast.makeText(LoginActivity.this, "on click email", Toast.LENGTH_SHORT).show();
                if(b){
                    search_ll.setBackgroundResource(R.drawable.round_border_primary_color);
                    search_iv.setImageResource(R.drawable.ic_baseline_search_24_primary);
                }else{
                    search_ll.setBackgroundResource(R.drawable.round_border);
                    search_iv.setImageResource(R.drawable.ic_baseline_search_24);
                }
//                searchTask.setBackgroundTintList(ColorStateList.valueOf(color));
            }
        });

        searchLeave.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchLeave_txt = query;
                Toast.makeText(getApplicationContext() ,"query: "+query, Toast.LENGTH_SHORT).show();
                getSearchLeavesDataFromFireStore();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchLeave_ctxt = newText;
//                Toast.makeText(getContext(),"newText: "+newText, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        search_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchLeave_txt = searchLeave_ctxt;
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                getSearchLeavesDataFromFireStore();
            }
        });

        addLeave_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        getSearchLeavesDataFromFireStore();

    }


    private void getSearchLeavesDataFromFireStore() {

        leaveArrayList.clear();
        leaveArrAdapter = new RecyclerLeavesAdapter(getApplicationContext(), leaveArrayList);
        leavesRecyclerView.setAdapter(leaveArrAdapter);

        String userId = getDataFromSharedPrefernces("userId");
        if(userId!=""){

            CollectionReference usersRef = fstore.collection("users");

            DocumentReference docRef = fstore.collection("users").document(userId);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    ArrayList<Map<String, Object>> leaves = (ArrayList<Map<String, Object>>) documentSnapshot.get("leaves");
                    Log.d(TAG, "documentSnapshotJournalsData: "+documentSnapshot.get("leaves"));


                    if(searchLeave_txt.isEmpty() || searchLeave_txt.equalsIgnoreCase("all")){
                        for (Map<String, Object> leave : leaves) {
                            leaveArrayList.add(new LeavesModel("", leave.get("leaveId")+"", leave.get("status")+"", leave.get("startDate")+"", leave.get("joiningDate")+"", leave.get("adminRemarks")+"", leave.get("leaderRemarks")+"", leave.get("reasonForLeave")+"", leave.get("responsibilitiesPassedTo")+""   ));
                            Log.d(TAG, leave.toString());
                        }
                    }else {
                        for (Map<String, Object> leave : leaves) {
                            if ((leave.get("leaveId") + "").contains(searchLeave_txt) ||
                                    (leave.get("startDate") + "").contains(searchLeave_txt) ||
                                    (leave.get("joiningDate") + "").contains(searchLeave_txt) ||
                                    (leave.get("adminRemarks") + "").contains(searchLeave_txt) ||
                                    (leave.get("leaderRemarks") + "").contains(searchLeave_txt) ||
                                    (leave.get("reasonForLeave") + "").contains(searchLeave_txt) ||
                                    (leave.get("responsibilitiesPassedTo") + "").contains(searchLeave_txt) ||
                                    (leave.get("status") + "").contains(searchLeave_txt)
                            ) {
                                leaveArrayList.add(new LeavesModel("", leave.get("leaveId")+"", leave.get("status")+"", leave.get("startDate")+"", leave.get("joiningDate")+"", leave.get("adminRemarks")+"", leave.get("leaderRemarks")+"", leave.get("reasonForLeave")+"", leave.get("responsibilitiesPassedTo")+""   ));
                                Log.d(TAG, leave.toString());
                            }
                        }
                    }

                    leaveArrAdapter = new RecyclerLeavesAdapter(getApplicationContext(), leaveArrayList);
                    leavesRecyclerView.setAdapter(leaveArrAdapter);

                }
            });
        }

    }

    private void showPopupMenuForJournalFilter(View v) {
        PopupMenu filterLeavePopupMenu = new PopupMenu(getApplicationContext(), v);
        filterLeavePopupMenu.inflate(R.menu.leave_filter_menu);

        MenuItem checkboxItem = filterLeavePopupMenu.getMenu().findItem(R.id.revSort_cb);

        checkboxItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                checkboxItem.setChecked(!checkboxItem.isChecked());
                desSort = !desSort;

                // Keep the popup menu open
                checkboxItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
                checkboxItem.setActionView(new View(getApplicationContext()));
                MenuItemCompat.setOnActionExpandListener(checkboxItem, new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return false;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        return false;
                    }
                });

                return false;
            }

        });

        filterLeavePopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.leaveStatus_sort:
//                        Toast.makeText(getContext(), "leave status sel desSort: "+ desSort, Toast.LENGTH_SHORT).show();
                        return false;

                    case R.id.leaveStatusInProcess_sort:
                        getFilterLeavesDataFromFireStore("status", "in-process");
                        return true;

                    case R.id.leaveStatusApproved_sort:
                        getFilterLeavesDataFromFireStore("status", "approved");
                        return true;


                    case R.id.joiningDate_sort:
                        Collections.sort(leaveArrayList, new Comparator<LeavesModel>() {
                            @Override
                            public int compare(LeavesModel leave1, LeavesModel leave2) {
                                return leave1.joiningDate.compareTo(leave2.joiningDate);
                            }
                        });
                        if(desSort){
                            Collections.reverse(leaveArrayList);
                            desSort=false;
                        }
                        leaveArrAdapter.notifyDataSetChanged();
                        return true;


                    case R.id.leaveReason_sort:
                        Collections.sort(leaveArrayList, new Comparator<LeavesModel>() {
                            @Override
                            public int compare(LeavesModel leave1, LeavesModel leave2) {
                                return leave1.reasonForLeave.compareTo(leave2.reasonForLeave);
                            }
                        });
                        if(desSort){
                            Collections.reverse(leaveArrayList);
                            desSort=false;
                        }
                        leaveArrAdapter.notifyDataSetChanged();
                        return true;


                    case R.id.leaveAdminRemarks_sort:
                        Collections.sort(leaveArrayList, new Comparator<LeavesModel>() {
                            @Override
                            public int compare(LeavesModel leave1, LeavesModel leave2) {
                                return leave1.adminRemarks.compareTo(leave2.adminRemarks);
                            }
                        });
                        if(desSort){
                            Collections.reverse(leaveArrayList);
                            desSort=false;
                        }
                        leaveArrAdapter.notifyDataSetChanged();
                        return true;


                    case R.id.leaveLeaderRemarks_sort:
                        Collections.sort(leaveArrayList, new Comparator<LeavesModel>() {
                            @Override
                            public int compare(LeavesModel leave1, LeavesModel leave2) {
                                return leave1.leaderRemarks.compareTo(leave2.leaderRemarks);
                            }
                        });
                        if(desSort){
                            Collections.reverse(leaveArrayList);
                            desSort=false;
                        }
                        leaveArrAdapter.notifyDataSetChanged();
                        return true;

                    default:
                        return false;
                }
            }
        });

        filterLeavePopupMenu.show();
    }

    public String getDataFromSharedPrefernces(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

    private void getFilterLeavesDataFromFireStore(String filterType, String filterOption) {

        leaveArrayList.clear();
        leaveArrAdapter = new RecyclerLeavesAdapter(getApplicationContext(), leaveArrayList);
        leavesRecyclerView.setAdapter(leaveArrAdapter);

        String userId = getDataFromSharedPrefernces("userId");
        if(userId!=""){

            CollectionReference usersRef = fstore.collection("users");
            DocumentReference docRef = fstore.collection("users").document(userId);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    ArrayList<Map<String, Object>> leaves= (ArrayList<Map<String, Object>>) documentSnapshot.get("leaves");
                    Log.d(TAG, "documentSnapshotJournalsData: "+documentSnapshot.get("leaves"));
                    Log.d(TAG, "leaves: "+leaves);


                    for (Map<String, Object> leave : leaves) {
                        if ((leave.get(filterType) + "").contains(filterOption)) {
                            leaveArrayList.add(new LeavesModel("", leave.get("leaveId")+"", leave.get("status")+"", leave.get("startDate")+"", leave.get("joiningDate")+"", leave.get("adminRemarks")+"", leave.get("leaderRemarks")+"", leave.get("reasonForLeave")+"", leave.get("responsibilitiesPassedTo")+""   ));
                            Log.d(TAG, leave.toString());
                        }
                    }
                    if(desSort){
                        Collections.reverse(leaveArrayList);
                        desSort=false;
                    }
                    leaveArrAdapter = new RecyclerLeavesAdapter(getApplicationContext(), leaveArrayList);
                    leavesRecyclerView.setAdapter(leaveArrAdapter);
                    leaveArrAdapter.notifyDataSetChanged();

                }
            });

        }
    }
}