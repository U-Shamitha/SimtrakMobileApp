package com.mad_lab.a1_loginpage.fragments.home;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mad_lab.a1_loginpage.R;
import com.mad_lab.a1_loginpage.adapter.RecyclerTaskDetailsAdapter;
import com.mad_lab.a1_loginpage.model.TaskDetailsModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class DashboardHomeFragment extends Fragment {

    TextView userName_tv;
    ImageButton sort_ib;
    LinearLayout search_ll;
    SearchView searchTask;
    ImageView search_iv;
    RecyclerView homeTasksRecyclerView;
    RecyclerTaskDetailsAdapter homeTasksArrAdapter ;
    ArrayList<TaskDetailsModel> homeTasksArrayList;

    String TAG = "HomeFragment";
    boolean desSort = false;

    FirebaseFirestore fstore;


    String searchTask_txt="";
    String searchTask_ctxt="";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard_home, container, false);

        userName_tv = view.findViewById(R.id.userName_tv);
        sort_ib = view.findViewById(R.id.sort_ib);
        search_ll = view.findViewById(R.id.search_ll);
        searchTask = view.findViewById(R.id.search_task);
        search_iv = view.findViewById(R.id.search_iv);
        homeTasksRecyclerView = view.findViewById(R.id.homeTasks_recyclerView);
        homeTasksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        homeTasksArrayList = new ArrayList<>();
        fstore = FirebaseFirestore.getInstance();



        //Task sort start
        sort_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenuForSortTask(view);
            }
        });

        //Task sort end



        //Search bar
        searchTask.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
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

        searchTask.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchTask_txt = query;
                Toast.makeText(getContext(),"query: "+query, Toast.LENGTH_SHORT).show();
                getSearchTasksDataFromFireStore();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchTask_ctxt = newText;
//                Toast.makeText(getContext(),"newText: "+newText, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        search_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchTask_txt = searchTask_ctxt;
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                getSearchTasksDataFromFireStore();
            }
        });

        setUserName();
        getTasksDataFromFireStore();



        return view;
    }

    private void setUserName() {
        userName_tv.setText(getDataFromSharedPrefernces("firstName")+" "+getDataFromSharedPrefernces("lastName"));
    }

    private void getTasksDataFromFireStore() {

        String userId = getDataFromSharedPrefernces("userId");
        if(userId!=""){

            CollectionReference usersRef = fstore.collection("users");

            DocumentReference docRef = fstore.collection("users").document(userId);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    ArrayList<Map<String, Object>> tasks= (ArrayList<Map<String, Object>>) documentSnapshot.get("tasks");
                    Log.d(TAG, "documentSnapshotTasksData: "+documentSnapshot.get("tasks"));
                    Log.d(TAG, "tasks: "+tasks);

                    String[] deadlines = {"0-2 days", "3-7 days", "over 7 days"};

                    if(tasks!=null){
                        for (Map<String, Object> task : tasks) {

                            homeTasksArrayList.add(new TaskDetailsModel("id", task.get("assignedDate")+"", task.get("taskName")+"", deadlines[(int)(long) task.get("taskPriority")-1], task.get("taskType")+""));
                            Log.d(TAG, task.toString());
                        }
                    }

                    homeTasksArrAdapter = new RecyclerTaskDetailsAdapter(getContext(), homeTasksArrayList);
                    homeTasksRecyclerView.setAdapter(homeTasksArrAdapter);

                }
            });


//            usersRef.document(userId).addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
//                @Override
//                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
//                    Map<String, Object> tasks= (Map<String, Object>) documentSnapshot.getBlob("tasks");
//                    Log.d(TAG, "documentSnapshotTasksData: "+documentSnapshot.getString("tasks"));
//                    Log.d(TAG, "tasks: "+tasks);
//
//                }
//            });
        }

    }

    private void getSearchTasksDataFromFireStore() {

        homeTasksArrayList.clear();
        homeTasksArrAdapter = new RecyclerTaskDetailsAdapter(getContext(), homeTasksArrayList);
        homeTasksRecyclerView.setAdapter(homeTasksArrAdapter);

        String userId = getDataFromSharedPrefernces("userId");
        if(userId!=""){
            // Fetch a single document
//            DocumentSnapshot documentSnapshot = fstore.collection("users").document(userId).get().getResult();
//            if (documentSnapshot.exists()) {
//                Log.d(TAG, documentSnapshot.getData()+"");
//            } else {
//                Log.d(TAG, "User details not found");
//            }

            CollectionReference usersRef = fstore.collection("users");

            DocumentReference docRef = fstore.collection("users").document(userId);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    ArrayList<Map<String, Object>> tasks= (ArrayList<Map<String, Object>>) documentSnapshot.get("tasks");
                    Log.d(TAG, "documentSnapshotTasksData: "+documentSnapshot.get("tasks"));
                    Log.d(TAG, "tasks: "+tasks);

                    String[] deadlines = {"0-2 days", "3-7 days", "over 7 days"};


                    if(searchTask_txt.isEmpty() || searchTask_txt.equalsIgnoreCase("all")){
                        for (Map<String, Object> task : tasks) {
                            homeTasksArrayList.add(new TaskDetailsModel("id", task.get("assignedDate") + "", task.get("taskName") + "", deadlines[(int) (long) task.get("taskPriority") - 1], task.get("taskType") + ""));
                            Log.d(TAG, task.toString());
                        }
                    }else {
                        for (Map<String, Object> task : tasks) {
                            if ((task.get("taskName") + "").contains(searchTask_txt)) {
                                homeTasksArrayList.add(new TaskDetailsModel("id", task.get("assignedDate") + "", task.get("taskName") + "", deadlines[(int) (long) task.get("taskPriority") - 1], task.get("taskType") + ""));
                                Log.d(TAG, task.toString());
                            }
                        }
                    }

                    homeTasksArrAdapter = new RecyclerTaskDetailsAdapter(getContext(), homeTasksArrayList);
                    homeTasksRecyclerView.setAdapter(homeTasksArrAdapter);

                }
            });


//            usersRef.document(userId).addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
//                @Override
//                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
//                    Map<String, Object> tasks= (Map<String, Object>) documentSnapshot.getBlob("tasks");
//                    Log.d(TAG, "documentSnapshotTasksData: "+documentSnapshot.getString("tasks"));
//                    Log.d(TAG, "tasks: "+tasks);
//
//                }
//            });
        }

    }

    public String getDataFromSharedPrefernces(String key) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserDetails", MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

    private void showPopupMenuForSortTask(View v) {
        PopupMenu sortTaskPopupMenu = new PopupMenu(getContext(), v);
        sortTaskPopupMenu.inflate(R.menu.task_sort_menu);

        MenuItem checkboxItem = sortTaskPopupMenu.getMenu().findItem(R.id.revSort_cb);

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


//        if (checkboxItem != null) {
//            CheckBox checkBox = (CheckBox) checkboxItem.getActionView().findViewById(R.id.menu_item_checkbox);
//            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                    checkBox.setChecked(!checkBox.isChecked());
//                    desSort = !desSort;
//                }
//            });


//        }

        sortTaskPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.taskId_sort:
                        Toast.makeText(getContext(), "taskId sort", Toast.LENGTH_SHORT).show();
                        Collections.sort(homeTasksArrayList, new Comparator<TaskDetailsModel>() {
                            @Override
                            public int compare(TaskDetailsModel task1, TaskDetailsModel task2) {
                                return task1.name.compareTo(task2.name);
                            }
                        });
                        if(desSort){
                            Collections.reverse(homeTasksArrayList);
                            desSort=false;
                        }
                        homeTasksArrAdapter.notifyDataSetChanged();
                        return true;
                    case R.id.assignedDate_sort:
                        Toast.makeText(getContext(), "assigned date sort", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(), "deadline sort", Toast.LENGTH_SHORT).show();
                        Collections.sort(homeTasksArrayList, new Comparator<TaskDetailsModel>() {
                            @Override
                            public int compare(TaskDetailsModel task1, TaskDetailsModel task2) {
                                return task1.assignedDate.compareTo(task2.assignedDate);
                            }
                        });
                        if(desSort){
                            Collections.reverse(homeTasksArrayList);
                            desSort=false;
                        }
                        homeTasksArrAdapter.notifyDataSetChanged();
                        return true;
                    case R.id.deadline_sort:
                        Toast.makeText(getContext(), "deadline sort", Toast.LENGTH_SHORT).show();
                        Collections.sort(homeTasksArrayList, new Comparator<TaskDetailsModel>() {
                            @Override
                            public int compare(TaskDetailsModel task1, TaskDetailsModel task2) {
                                return task1.deadline.compareTo(task2.deadline);
                            }
                        });
                        if(desSort){
                            Collections.reverse(homeTasksArrayList);
                            desSort=false;
                        }
                        homeTasksArrAdapter.notifyDataSetChanged();
                        return true;
                    case R.id.taskType_sort:
                        Toast.makeText(getContext(), "task type sort", Toast.LENGTH_SHORT).show();
                        Collections.sort(homeTasksArrayList, new Comparator<TaskDetailsModel>() {
                            @Override
                            public int compare(TaskDetailsModel task1, TaskDetailsModel task2) {
                                return task1.assignedBy.compareTo(task2.assignedBy);
                            }
                        });
                        if(desSort){
                            Collections.reverse(homeTasksArrayList);
                            desSort=false;
                        }
                        homeTasksArrAdapter.notifyDataSetChanged();
                        return true;
                    default:
                        return false;
                }
            }
        });

        sortTaskPopupMenu.show();
    }

}