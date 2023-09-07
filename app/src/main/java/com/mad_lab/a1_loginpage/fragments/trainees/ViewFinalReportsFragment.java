package com.mad_lab.a1_loginpage.fragments.trainees;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mad_lab.a1_loginpage.R;
import com.mad_lab.a1_loginpage.activity.DashboardActivity;
import com.mad_lab.a1_loginpage.adapter.RecyclerFinalReportsAdapter;
import com.mad_lab.a1_loginpage.adapter.RecyclerLeavesAdapter;
import com.mad_lab.a1_loginpage.model.FinalReportModel;
import com.mad_lab.a1_loginpage.model.LeavesModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class ViewFinalReportsFragment extends Fragment {

    ImageButton finalReportFilter_btn;
    ImageButton addFinalReport_btn;
    LinearLayout search_ll;
    SearchView searchFinalReport;

    ImageView search_iv;

    RecyclerView finalReportsRecyclerView;
    RecyclerFinalReportsAdapter finalReportArrAdapter ;
    ArrayList<FinalReportModel> finalReportArrayList;

    String TAG = "ViewFinalReportsFragment";
    String searchFinalReport_txt="";
    String searchFinalReport_ctxt="";
    boolean desSort = false;
    FirebaseFirestore fstore;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_finalreports, container, false);

        finalReportFilter_btn = view.findViewById(R.id.finalReportFilter_btn);
        addFinalReport_btn = view.findViewById(R.id.addFinalReport_btn);
        search_ll = view.findViewById(R.id.search_ll);
        searchFinalReport = view.findViewById(R.id.search_finalReport);
        search_iv = view.findViewById(R.id.search_iv);
        finalReportsRecyclerView = view.findViewById(R.id.finalReports_recyclerView);

        finalReportsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        finalReportArrayList = new ArrayList<>();

        fstore = FirebaseFirestore.getInstance();

        //Task filter start
        finalReportFilter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenuForJournalFilter(view);
            }
        });

        //Task filter end


        //Search bar
        searchFinalReport.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
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

        searchFinalReport.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchFinalReport_txt = query;
                Toast.makeText(getContext().getApplicationContext() ,"query: "+query, Toast.LENGTH_SHORT).show();
                getSearchFinalReportsDataFromFireStore();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchFinalReport_ctxt = newText;
//                Toast.makeText(getContext(),"newText: "+newText, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        search_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchFinalReport_txt = searchFinalReport_ctxt;
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                getSearchFinalReportsDataFromFireStore();
            }
        });

        addFinalReport_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getContext().getApplicationContext(), DashboardActivity.class);
                intent.putExtra("DesFragment","UploadFinalReport");
                startActivity(intent);
                getActivity().finish();
            }
        });

        getSearchFinalReportsDataFromFireStore();


        return view;
    }


    private void getSearchFinalReportsDataFromFireStore() {

        finalReportArrayList.clear();
        finalReportArrAdapter = new RecyclerFinalReportsAdapter(getContext().getApplicationContext(), finalReportArrayList);
        finalReportsRecyclerView.setAdapter(finalReportArrAdapter);

        String userId = getDataFromSharedPrefernces("userId");
        if(userId!=""){

            CollectionReference usersRef = fstore.collection("users");

            DocumentReference docRef = fstore.collection("users").document(userId);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    ArrayList<Map<String, Object>> finalReports = (ArrayList<Map<String, Object>>) documentSnapshot.get("finalReports") != null ? (ArrayList<Map<String, Object>>) documentSnapshot.get("finalReports") : new ArrayList<>();
                    Log.d(TAG, "documentSnapshotFinalReportsData: "+documentSnapshot.get("finalReports"));


                    if(searchFinalReport_txt.isEmpty() || searchFinalReport_txt.equalsIgnoreCase("all")){
                        for (Map<String, Object> finalReport : finalReports) {
                            finalReportArrayList.add(new FinalReportModel("", finalReport.get("reportId")+"", finalReport.get("status")+"", finalReport.get("fileName")+"", finalReport.get("uploadedOn")+""));
                            Log.d(TAG, finalReport.toString());
                        }
                    }else {
                        for (Map<String, Object> finalReport : finalReports) {
                            if ((finalReport.get("reportId") + "").contains(searchFinalReport_txt) ||
                                    (finalReport.get("fileName") + "").contains(searchFinalReport_txt) ||
                                    (finalReport.get("uploadedOn") + "").contains(searchFinalReport_txt) ||
                                    (finalReport.get("status") + "").contains(searchFinalReport_txt)
                            ) {
                                finalReportArrayList.add(new FinalReportModel("", finalReport.get("reportId")+"", finalReport.get("status")+"", finalReport.get("fileName")+"", finalReport.get("uploadedOn")+""));
                                Log.d(TAG, finalReport.toString());
                            }
                        }
                    }

                    finalReportArrAdapter = new RecyclerFinalReportsAdapter(getContext().getApplicationContext(), finalReportArrayList);
                    finalReportsRecyclerView.setAdapter(finalReportArrAdapter);

                }
            });
        }

    }

    private void showPopupMenuForJournalFilter(View v) {
        PopupMenu filterLeavePopupMenu = new PopupMenu(getContext().getApplicationContext(), v);
        filterLeavePopupMenu.inflate(R.menu.finalreport_filter_menu);

        MenuItem checkboxItem = filterLeavePopupMenu.getMenu().findItem(R.id.revSort_cb);

        checkboxItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                checkboxItem.setChecked(!checkboxItem.isChecked());
                desSort = !desSort;

                // Keep the popup menu open
                checkboxItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
                checkboxItem.setActionView(new View(getContext().getApplicationContext()));
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

                    case R.id.finalReportStatus_sort:
//                        Toast.makeText(getContext(), "leave status sel desSort: "+ desSort, Toast.LENGTH_SHORT).show();
                        return false;

                    case R.id.finalReportStatusInProcess_sort:
                        getFilterFinalReportsDataFromFireStore("status", "in-process");
                        return true;

                    case R.id.finalReportStatusApproved_sort:
                        getFilterFinalReportsDataFromFireStore("status", "approved");
                        return true;

                    case R.id.fileName_sort:
                        Collections.sort(finalReportArrayList, new Comparator<FinalReportModel>() {
                            @Override
                            public int compare(FinalReportModel report1, FinalReportModel report2) {
                                return report1.fileName.compareTo(report2.fileName);
                            }
                        });
                        if(desSort){
                            Collections.reverse(finalReportArrayList);
                            desSort=false;
                        }
                        finalReportArrAdapter = new RecyclerFinalReportsAdapter(getContext().getApplicationContext(), finalReportArrayList);
                        finalReportsRecyclerView.setAdapter(finalReportArrAdapter);
                        return true;

                    case R.id.uploadedOn_sort:
                        Collections.sort(finalReportArrayList, new Comparator<FinalReportModel>() {
                            @Override
                            public int compare(FinalReportModel report1, FinalReportModel report2) {
                                return report1.uploadedOn.compareTo(report2.uploadedOn);
                            }
                        });
                        if(desSort){
                            Collections.reverse(finalReportArrayList);
                            desSort=false;
                        }
                        finalReportArrAdapter = new RecyclerFinalReportsAdapter(getContext().getApplicationContext(), finalReportArrayList);
                        finalReportsRecyclerView.setAdapter(finalReportArrAdapter);
                        return true;

                    default:
                        return false;
                }
            }
        });

        filterLeavePopupMenu.show();
    }

    public String getDataFromSharedPrefernces(String key) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserDetails", MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

    private void getFilterFinalReportsDataFromFireStore(String filterType, String filterOption) {

        finalReportArrayList.clear();
        finalReportArrAdapter = new RecyclerFinalReportsAdapter(getContext().getApplicationContext(), finalReportArrayList);
        finalReportsRecyclerView.setAdapter(finalReportArrAdapter);

        String userId = getDataFromSharedPrefernces("userId");
        if(userId!=""){

            CollectionReference usersRef = fstore.collection("users");
            DocumentReference docRef = fstore.collection("users").document(userId);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    ArrayList<Map<String, Object>> finalReports= (ArrayList<Map<String, Object>>) documentSnapshot.get("finalReports") != null ? (ArrayList<Map<String, Object>>) documentSnapshot.get("finalReports") : new ArrayList<>();
                    Log.d(TAG, "documentSnapshotFinalReportsData: "+documentSnapshot.get("finalReports"));
                    Log.d(TAG, "finalReports: "+ finalReports);


                    for (Map<String, Object> finalReport : finalReports ) {
                        if ((finalReport.get(filterType) + "").contains(filterOption)) {
                            finalReportArrayList.add(new FinalReportModel("", finalReport.get("reportId")+"", finalReport.get("status")+"", finalReport.get("fileName")+"", finalReport.get("uploadedOn")+""));
                            Log.d(TAG, finalReport.toString());
                        }
                    }
                    if(desSort){
                        Collections.reverse(finalReportArrayList);
                        desSort=false;
                    }
                    finalReportArrAdapter = new RecyclerFinalReportsAdapter(getContext().getApplicationContext(), finalReportArrayList);
                    finalReportsRecyclerView.setAdapter(finalReportArrAdapter);
                    finalReportArrAdapter.notifyDataSetChanged();

                }
            });

        }
    }


}