package com.mad_lab.a1_loginpage;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;


public class DashboardHomeFragment extends Fragment {

    SearchView searchTask;

    RecyclerView homeTasksRecyclerView;
    RecyclerTaskDetailsAdapter homeTasksArrAdapter ;
    ArrayList<TaskDetailsModel> homeTasksArrayList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard_home, container, false);

        searchTask = view.findViewById(R.id.search_task);
        homeTasksRecyclerView = view.findViewById(R.id.homeTasks_recyclerView);
        homeTasksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        homeTasksArrayList = new ArrayList<>();

        //Search bar
        searchTask.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                //                Toast.makeText(LoginActivity.this, "on click email", Toast.LENGTH_SHORT).show();
                if(b){
                    searchTask.setBackgroundResource(R.drawable.round_border_primary_color);
                }else{
                    searchTask.setBackgroundResource(R.drawable.round_border);
                }
//                searchTask.setBackgroundTintList(ColorStateList.valueOf(color));
            }
        });

        homeTasksArrayList.add(new TaskDetailsModel("id1","26-05-23","task1","27-05-23","self"));
        homeTasksArrayList.add(new TaskDetailsModel("id2","27-05-23","task2","28-05-23","self"));
        homeTasksArrayList.add(new TaskDetailsModel("id3","28-05-23","task3","29-05-23","self"));
        homeTasksArrayList.add(new TaskDetailsModel("id4","29-05-23","task4","30-05-23","self"));
        homeTasksArrayList.add(new TaskDetailsModel("id5","30-05-23","task5","31-05-23","self"));

        homeTasksArrAdapter = new RecyclerTaskDetailsAdapter(getContext(), homeTasksArrayList);
        homeTasksRecyclerView.setAdapter(homeTasksArrAdapter);

        return view;
    }
}