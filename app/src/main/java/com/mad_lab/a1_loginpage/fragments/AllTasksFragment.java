package com.mad_lab.a1_loginpage.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mad_lab.a1_loginpage.R;
import com.mad_lab.a1_loginpage.adapter.RecyclerTaskDetailsAdapter;
import com.mad_lab.a1_loginpage.model.TaskDetailsModel;

import java.util.ArrayList;

public class AllTasksFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<TaskDetailsModel> arrTaskDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_tasks, container, false);

        recyclerView = view.findViewById(R.id.tasksRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        arrTaskDetails = new ArrayList<>();

        arrTaskDetails.add(new TaskDetailsModel("id1","26-05-23","task1","27-05-23","self"));
        arrTaskDetails.add(new TaskDetailsModel("id2","27-05-23","task2","28-05-23","self"));
        arrTaskDetails.add(new TaskDetailsModel("id3","28-05-23","task3","29-05-23","self"));
        arrTaskDetails.add(new TaskDetailsModel("id4","29-05-23","task4","30-05-23","self"));
        arrTaskDetails.add(new TaskDetailsModel("id5","30-05-23","task5","31-05-23","self"));

        RecyclerTaskDetailsAdapter adapter = new RecyclerTaskDetailsAdapter(getContext(), arrTaskDetails);
        recyclerView.setAdapter(adapter);

        return view;
    }
}