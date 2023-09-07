package com.mad_lab.a1_loginpage.fragments.trainees;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mad_lab.a1_loginpage.R;
import com.mad_lab.a1_loginpage.activity.DashboardActivity;
import com.mad_lab.a1_loginpage.adapter.RecyclerJournalAdapter;
import com.mad_lab.a1_loginpage.model.JournalModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class ViewJournalsFragment extends Fragment {

    ImageButton journalFilter_btn;
    ImageButton addJournal_btn;
    LinearLayout search_ll;
    SearchView searchJournal;

    ImageView search_iv;

    RecyclerView journalRecyclerView;
    RecyclerJournalAdapter journalArrAdapter ;
    ArrayList<JournalModel> journalArrayList;

    String TAG = "JournalFragment";
    String searchJournal_txt="";
    String searchJournal_ctxt="";
    boolean desSort = false;
    FirebaseFirestore fstore;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_journal, container, false);

        journalFilter_btn = view.findViewById(R.id.journalFilter_btn);
        addJournal_btn = view.findViewById(R.id.add_journal_bt);
        search_ll = view.findViewById(R.id.search_ll);
        searchJournal = view.findViewById(R.id.search_journal);
        search_iv = view.findViewById(R.id.search_iv);
        journalRecyclerView = view.findViewById(R.id.dailyJournal_recyclerView);
        journalRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        journalArrayList = new ArrayList<>();

        fstore = FirebaseFirestore.getInstance();

        //Task filter start
        journalFilter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenuForJournalFilter(view);
            }
        });

        //Task filter end


        //Search bar
        searchJournal.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
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

        searchJournal.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchJournal_txt = query;
                Toast.makeText(getContext(),"query: "+query, Toast.LENGTH_SHORT).show();
                getSearchJournalsDataFromFireStore();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchJournal_ctxt = newText;
//                Toast.makeText(getContext(),"newText: "+newText, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        search_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchJournal_txt = searchJournal_ctxt;
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                getSearchJournalsDataFromFireStore();
            }
        });

        addJournal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getContext().getApplicationContext(), DashboardActivity.class);
                intent.putExtra("DesFragment","AddJournal");
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });

        getSearchJournalsDataFromFireStore();

        return view;
    }




    private void getSearchJournalsDataFromFireStore() {

        journalArrayList.clear();
        journalArrAdapter = new RecyclerJournalAdapter(getContext(), journalArrayList);
        journalRecyclerView.setAdapter(journalArrAdapter);

        String userId = getDataFromSharedPrefernces("userId");
        if(userId!=""){

            CollectionReference usersRef = fstore.collection("users");

            DocumentReference docRef = fstore.collection("users").document(userId);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    ArrayList<Map<String, Object>> journals= (ArrayList<Map<String, Object>>) documentSnapshot.get("journals") !=null ? (ArrayList<Map<String, Object>>) documentSnapshot.get("journals") : new ArrayList<>();
                    Log.d(TAG, "documentSnapshotJournalsData: "+documentSnapshot.get("journals"));

                    String[] priorities = {"0-2 days", "3-7 days", "over 7 days"};


                    if(searchJournal_txt.isEmpty() || searchJournal_txt.equalsIgnoreCase("all")){
                        for (Map<String, Object> journal : journals) {
                            journalArrayList.add(new JournalModel( Float.parseFloat(journal.get("ratingOfDay")+""), journal.get("journalId")+"", journal.get("journalStatus")+"",journal.get("date")+"", journal.get("report")+"", journal.get("learnings")+"", journal.get("pendings")+"", journal.get("adminRemarks")+"", journal.get("leaderRemarks")+""));
                            Log.d(TAG, journal.toString());
                        }
                    }else {
                        for (Map<String, Object> journal : journals) {
                            if ((journal.get("journalId") + "").contains(searchJournal_txt) ||
                                    (journal.get("date") + "").contains(searchJournal_txt) ||
                                    (journal.get("journalStatus") + "").contains(searchJournal_txt) ||
                                    (journal.get("report") + "").contains(searchJournal_txt) ||
                                    (journal.get("learnings") + "").contains(searchJournal_txt) ||
                                    (journal.get("pendings") + "").contains(searchJournal_txt) ||
                                    (journal.get("adminRemarks") + "").contains(searchJournal_txt) ||
                                    (journal.get("leaderRemarks") + "").contains(searchJournal_txt) ||
                                    (journal.get("ratingOfDay") + "").contains(searchJournal_txt)
                            ) {
                                journalArrayList.add(new JournalModel(Float.parseFloat(journal.get("ratingOfDay")+""), journal.get("journalId")+"", journal.get("journalStatus")+"",journal.get("date")+"", journal.get("report")+"", journal.get("learnings")+"", journal.get("pendings")+"", journal.get("adminRemarks")+"", journal.get("leaderRemarks")+""));
                                Log.d(TAG, journal.toString());
                            }
                        }
                    }

                    journalArrAdapter = new RecyclerJournalAdapter(getContext(), journalArrayList);
                    journalRecyclerView.setAdapter(journalArrAdapter);

                }
            });
        }

    }

    private void showPopupMenuForJournalFilter(View v) {
        PopupMenu filterJournalPopupMenu = new PopupMenu(getContext(), v);
        filterJournalPopupMenu.inflate(R.menu.journal_filter_menu);

        MenuItem checkboxItem = filterJournalPopupMenu.getMenu().findItem(R.id.revSort_cb);

        checkboxItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                checkboxItem.setChecked(!checkboxItem.isChecked());
                desSort = !desSort;

                // Keep the popup menu open
                checkboxItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
                checkboxItem.setActionView(new View(getContext()));
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

        filterJournalPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.journalStatus_sort:
//                        Toast.makeText(getContext(), "task status sel desSort: "+ desSort, Toast.LENGTH_SHORT).show();
                        return false;

                    case R.id.journalStatusInProcess_sort:
                        getFilterJournalsDataFromFireStore("journalStatus", "in-process");
                        return true;

                    case R.id.journalStatusApproved_sort:
                        getFilterJournalsDataFromFireStore("journalStatus", "approved");
                        return true;

                    case R.id.journalReport_sort:
//                        Toast.makeText(getContext(), "taskName sort", Toast.LENGTH_SHORT).show();
                        Collections.sort(journalArrayList, new Comparator<JournalModel>() {
                            @Override
                            public int compare(JournalModel journal1, JournalModel journal2) {
                                return journal1.report.compareTo(journal2.report);
                            }
                        });
                        if(desSort){
                            Collections.reverse(journalArrayList);
                            desSort=false;
                        }
                        journalArrAdapter = new RecyclerJournalAdapter(getContext(), journalArrayList);
                        journalRecyclerView.setAdapter(journalArrAdapter);

                        return true;

                    case R.id.journalLearnings_sort:
//                        Toast.makeText(getContext(), "journalLearnings sort", Toast.LENGTH_SHORT).show();
                        Collections.sort(journalArrayList, new Comparator<JournalModel>() {
                            @Override
                            public int compare(JournalModel journal1, JournalModel journal2) {
                                return journal1.learnings.compareTo(journal2.learnings);
                            }
                        });
                        if(desSort){
                            Collections.reverse(journalArrayList);
                            desSort=false;
                        }
                        journalArrAdapter = new RecyclerJournalAdapter(getContext(), journalArrayList);
                        journalRecyclerView.setAdapter(journalArrAdapter);

                        return true;

                    case R.id.journalPendings_sort:
//                        Toast.makeText(getContext(), "journalPendings sort", Toast.LENGTH_SHORT).show();
                        Collections.sort(journalArrayList, new Comparator<JournalModel>() {
                            @Override
                            public int compare(JournalModel journal1, JournalModel journal2) {
                                return journal1.pendings.compareTo(journal2.pendings);
                            }
                        });
                        if(desSort){
                            Collections.reverse(journalArrayList);
                            desSort=false;
                        }
                        journalArrAdapter = new RecyclerJournalAdapter(getContext(), journalArrayList);
                        journalRecyclerView.setAdapter(journalArrAdapter);

                        return true;

                    case R.id.journalAdminRemarks_sort:
//                        Toast.makeText(getContext(), "journalAdminRemarks sort", Toast.LENGTH_SHORT).show();
                        Collections.sort(journalArrayList, new Comparator<JournalModel>() {
                            @Override
                            public int compare(JournalModel journal1, JournalModel journal2) {
                                return journal1.adminRemarks.compareTo(journal2.adminRemarks);
                            }
                        });
                        if(desSort){
                            Collections.reverse(journalArrayList);
                            desSort=false;
                        }
                        journalArrAdapter = new RecyclerJournalAdapter(getContext(), journalArrayList);
                        journalRecyclerView.setAdapter(journalArrAdapter);

                        return true;

                    case R.id.journalLeaderRemarks_sort:
//                        Toast.makeText(getContext(), "journalLeaderRemarks sort", Toast.LENGTH_SHORT).show();
                        Collections.sort(journalArrayList, new Comparator<JournalModel>() {
                            @Override
                            public int compare(JournalModel journal1, JournalModel journal2) {
                                return journal1.leaderRemarks.compareTo(journal2.leaderRemarks);
                            }
                        });
                        if(desSort){
                            Collections.reverse(journalArrayList);
                            desSort=false;
                        }
                        journalArrAdapter = new RecyclerJournalAdapter(getContext(), journalArrayList);
                        journalRecyclerView.setAdapter(journalArrAdapter);

                        return true;

                    default:
                        return false;
                }
            }
        });

        filterJournalPopupMenu.show();
    }

    public String getDataFromSharedPrefernces(String key) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserDetails", MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

    private void getFilterJournalsDataFromFireStore(String filterType, String filterOption) {

        journalArrayList.clear();
        journalArrAdapter = new RecyclerJournalAdapter(getContext(), journalArrayList);
        journalRecyclerView.setAdapter(journalArrAdapter);

        String userId = getDataFromSharedPrefernces("userId");
        if(userId!=""){

            CollectionReference usersRef = fstore.collection("users");
            DocumentReference docRef = fstore.collection("users").document(userId);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    ArrayList<Map<String, Object>> journals= (ArrayList<Map<String, Object>>) documentSnapshot.get("journals") != null ? (ArrayList<Map<String, Object>>) documentSnapshot.get("journals") : new ArrayList<>();
                    Log.d(TAG, "documentSnapshotJournalsData: "+documentSnapshot.get("journals"));
                    Log.d(TAG, "journals: "+journals);


                    for (Map<String, Object> journal : journals) {
                        if ((journal.get(filterType) + "").contains(filterOption)) {
                            journalArrayList.add(new JournalModel( Float.parseFloat(journal.get("ratingOfDay")+""), journal.get("journalId")+"", journal.get("journalStatus")+"",journal.get("date")+"", journal.get("report")+"", journal.get("learnings")+"", journal.get("pendings")+"", journal.get("adminRemarks")+"", journal.get("leaderRemarks")+""));
                            Log.d(TAG, journal.toString());
                        }
                    }
                    if(desSort){
                        Collections.reverse(journalArrayList);
                        desSort=false;
                    }
                    journalArrAdapter = new RecyclerJournalAdapter(getContext(), journalArrayList);
                    journalRecyclerView.setAdapter(journalArrAdapter);
                    journalArrAdapter.notifyDataSetChanged();

                }
            });

        }
    }
}