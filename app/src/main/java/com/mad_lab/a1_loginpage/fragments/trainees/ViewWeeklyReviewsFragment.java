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
import com.mad_lab.a1_loginpage.adapter.RecyclerWeeklyReviewsAdapter;
import com.mad_lab.a1_loginpage.model.FinalReportModel;
import com.mad_lab.a1_loginpage.model.WeeklyReviewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class ViewWeeklyReviewsFragment extends Fragment {

    ImageButton weeklyReviewFilter_btn;
    ImageButton addWeeklyReview_btn;
    LinearLayout search_ll;
    SearchView searchWeeklyReview;

    ImageView search_iv;

    RecyclerView weeklyReviewsRecyclerView;
    RecyclerWeeklyReviewsAdapter weeklyReviewArrAdapter ;
    ArrayList<WeeklyReviewModel> weeklyReviewArrayList;

    String TAG = "ViewWeeklyReviewsFragment";
    String searchFinalReport_txt="";
    String searchFinalReport_ctxt="";
    boolean desSort = false;
    FirebaseFirestore fstore;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_weeklyreviews, container, false);

        weeklyReviewFilter_btn = view.findViewById(R.id.weeklyReviewFilter_btn);
        addWeeklyReview_btn = view.findViewById(R.id.addWeeklyReview_btn);
        search_ll = view.findViewById(R.id.search_ll);
        searchWeeklyReview = view.findViewById(R.id.search_weeklyReview);
        search_iv = view.findViewById(R.id.search_iv);
        weeklyReviewsRecyclerView = view.findViewById(R.id.weeklyReviews_recyclerView);

        weeklyReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        weeklyReviewArrayList = new ArrayList<>();

        fstore = FirebaseFirestore.getInstance();

        //Task filter start
        weeklyReviewFilter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenuForWeeklyReviewFilter(view);
            }
        });

        //Task filter end


        //Search bar
        searchWeeklyReview.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
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

        searchWeeklyReview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchFinalReport_txt = query;
                Toast.makeText(getContext().getApplicationContext() ,"query: "+query, Toast.LENGTH_SHORT).show();
                getSearchWeeklyReviewsDataFromFireStore();
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
                getSearchWeeklyReviewsDataFromFireStore();
            }
        });

        addWeeklyReview_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getContext().getApplicationContext(), DashboardActivity.class);
                intent.putExtra("DesFragment","UploadWeeklyReview");
                startActivity(intent);
                getActivity().finish();
            }
        });

        getSearchWeeklyReviewsDataFromFireStore();


        return view;
    }


    private void getSearchWeeklyReviewsDataFromFireStore() {

        weeklyReviewArrayList.clear();
        weeklyReviewArrAdapter= new RecyclerWeeklyReviewsAdapter(getContext().getApplicationContext(), weeklyReviewArrayList);
        weeklyReviewsRecyclerView.setAdapter(weeklyReviewArrAdapter);

        String userId = getDataFromSharedPrefernces("userId");
        if(userId!=""){

            CollectionReference usersRef = fstore.collection("users");

            DocumentReference docRef = fstore.collection("users").document(userId);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    ArrayList<Map<String, Object>> weeklyReviews = (ArrayList<Map<String, Object>>) documentSnapshot.get("weeklyReviews") != null ? (ArrayList<Map<String, Object>>) documentSnapshot.get("weeklyReviews") : new ArrayList<>();
                    Log.d(TAG, "documentSnapshotFinalReportsData: "+documentSnapshot.get("weeklyReviews"));


                    if(searchFinalReport_txt.isEmpty() || searchFinalReport_txt.equalsIgnoreCase("all")){
                        for (Map<String, Object> weeklyReview : weeklyReviews) {
                            weeklyReviewArrayList.add(new WeeklyReviewModel("", weeklyReview.get("weeklyReviewId")+"", weeklyReview.get("status")+"", weeklyReview.get("week")+"", weeklyReview.get("remarks")+"",weeklyReview.get("stamp")+""));
                            Log.d(TAG, weeklyReview.toString());
                        }
                    }else {
                        for (Map<String, Object> weeklyReview : weeklyReviews) {
                            if ((weeklyReview.get("weeklyReviewId") + "").contains(searchFinalReport_txt) ||
                                    (weeklyReview.get("week") + "").contains(searchFinalReport_txt) ||
                                    (weeklyReview.get("remarks")+ "").contains(searchFinalReport_txt) ||
                                    (weeklyReview.get("stamp")+ "").contains(searchFinalReport_txt) ||
                                    (weeklyReview.get("status") + "").contains(searchFinalReport_txt)
                            ) {
                                weeklyReviewArrayList.add(new WeeklyReviewModel("", weeklyReview.get("weeklyReviewId")+"", weeklyReview.get("status")+"", weeklyReview.get("week")+"", weeklyReview.get("remarks")+"",weeklyReview.get("stamp")+""));
                                Log.d(TAG, weeklyReview.toString());
                            }
                        }
                    }

                    weeklyReviewArrAdapter = new RecyclerWeeklyReviewsAdapter(getContext().getApplicationContext(), weeklyReviewArrayList);
                    weeklyReviewsRecyclerView.setAdapter(weeklyReviewArrAdapter);

                }
            });
        }

    }

    private void showPopupMenuForWeeklyReviewFilter(View v) {
        PopupMenu filterWeeklyReviewPopupMenu = new PopupMenu(getContext().getApplicationContext(), v);
        filterWeeklyReviewPopupMenu.inflate(R.menu.weeklyreview_filter_menu);

        MenuItem checkboxItem = filterWeeklyReviewPopupMenu.getMenu().findItem(R.id.revSort_cb);

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

        filterWeeklyReviewPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.weeklyReviewStatus_sort:
//                        Toast.makeText(getContext(), "leave status sel desSort: "+ desSort, Toast.LENGTH_SHORT).show();
                        return false;

                    case R.id.weeklyReviewStatusInProcess_sort:
                        getFilterWeeklyReviewsDataFromFireStore("status", "in-process");
                        return true;

                    case R.id.weeklyReviewStatusApproved_sort:
                        getFilterWeeklyReviewsDataFromFireStore("status", "approved");
                        return true;

                    case R.id.week_sort:
                        Collections.sort(weeklyReviewArrayList, new Comparator<WeeklyReviewModel>() {
                            @Override
                            public int compare(WeeklyReviewModel wr1, WeeklyReviewModel wr2) {
                                return wr1.week.compareTo(wr2.week);
                            }
                        });
                        if(desSort){
                            Collections.reverse(weeklyReviewArrayList);
                            desSort=false;
                        }
                        weeklyReviewArrAdapter = new RecyclerWeeklyReviewsAdapter(getContext().getApplicationContext(), weeklyReviewArrayList);
                        weeklyReviewsRecyclerView.setAdapter(weeklyReviewArrAdapter);
                        return true;

                    case R.id.remarks_sort:
                        Collections.sort(weeklyReviewArrayList, new Comparator<WeeklyReviewModel>() {
                            @Override
                            public int compare(WeeklyReviewModel wr1, WeeklyReviewModel wr2) {
                                return wr1.remarks.compareTo(wr2.remarks);
                            }
                        });
                        if(desSort){
                            Collections.reverse(weeklyReviewArrayList);
                            desSort=false;
                        }
                        weeklyReviewArrAdapter = new RecyclerWeeklyReviewsAdapter(getContext().getApplicationContext(), weeklyReviewArrayList);
                        weeklyReviewsRecyclerView.setAdapter(weeklyReviewArrAdapter);
                        return true;

                    case R.id.stamp_sort:
                        Collections.sort(weeklyReviewArrayList, new Comparator<WeeklyReviewModel>() {
                            @Override
                            public int compare(WeeklyReviewModel wr1, WeeklyReviewModel wr2) {
                                return wr1.stamp.compareTo(wr2.stamp);
                            }
                        });
                        if(desSort){
                            Collections.reverse(weeklyReviewArrayList);
                            desSort=false;
                        }
                        weeklyReviewArrAdapter = new RecyclerWeeklyReviewsAdapter(getContext().getApplicationContext(), weeklyReviewArrayList);
                        weeklyReviewsRecyclerView.setAdapter(weeklyReviewArrAdapter);
                        return true;

                    default:
                        return false;
                }
            }
        });

        filterWeeklyReviewPopupMenu.show();
    }

    public String getDataFromSharedPrefernces(String key) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserDetails", MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

    private void getFilterWeeklyReviewsDataFromFireStore(String filterType, String filterOption) {

        weeklyReviewArrayList.clear();
        weeklyReviewArrAdapter = new RecyclerWeeklyReviewsAdapter(getContext().getApplicationContext(), weeklyReviewArrayList);
        weeklyReviewsRecyclerView.setAdapter(weeklyReviewArrAdapter);

        String userId = getDataFromSharedPrefernces("userId");
        if(userId!=""){

            CollectionReference usersRef = fstore.collection("users");
            DocumentReference docRef = fstore.collection("users").document(userId);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    ArrayList<Map<String, Object>> weeklyReviews = (ArrayList<Map<String, Object>>) documentSnapshot.get("weeklyReviews") != null ? (ArrayList<Map<String, Object>>) documentSnapshot.get("weeklyReviews") : new ArrayList<>();
                    Log.d(TAG, "documentSnapshotFinalReportsData: "+documentSnapshot.get("weeklyReviews"));
                    Log.d(TAG, "weeklyReviews: "+ weeklyReviews);


                    for (Map<String, Object> weeklyReview : weeklyReviews ) {
                        if ((weeklyReview.get(filterType) + "").contains(filterOption)) {
                            weeklyReviewArrayList.add(new WeeklyReviewModel("", weeklyReview.get("weeklyReviewId")+"", weeklyReview.get("status")+"", weeklyReview.get("week")+"", weeklyReview.get("remarks")+"",weeklyReview.get("stamp")+""));
                            Log.d(TAG, weeklyReview.toString());
                        }
                    }
                    if(desSort){
                        Collections.reverse(weeklyReviewArrayList);
                        desSort=false;
                    }
                    weeklyReviewArrAdapter = new RecyclerWeeklyReviewsAdapter(getContext().getApplicationContext(), weeklyReviewArrayList);
                    weeklyReviewsRecyclerView.setAdapter(weeklyReviewArrAdapter);
                    weeklyReviewArrAdapter.notifyDataSetChanged();

                }
            });

        }
    }


}