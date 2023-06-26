package com.mad_lab.a1_loginpage.fragments.trainees;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mad_lab.a1_loginpage.R;

public class AddJournalFragment extends Fragment {

    AutoCompleteTextView taskTopic_atv;
    AutoCompleteTextView taskStatus_atv;
    AutoCompleteTextView ratingOfDay_atv;
    TextInputEditText taskTime_et;

    TextInputLayout taskTopic_tl;
    TextInputLayout taskStatus_tl;
    TextInputLayout ratingOfDay_tl;
    TextInputLayout taskTime_tl;

    String taskTopic;
    String taskStatus;
    Integer ratingOfDay;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_journal, container, false);

        taskTopic_atv = view.findViewById(R.id.taskTopic_atv);
        taskStatus_atv = view.findViewById(R.id.taskStatus_atv);
        ratingOfDay_atv = view.findViewById(R.id.ratingOfDay_atv);
        taskTime_et = view.findViewById(R.id.taskTime_et);

        taskTopic_tl = view.findViewById(R.id.taskTopic_tl);
        taskStatus_tl = view.findViewById(R.id.taskStatus_tl);
        ratingOfDay_tl = view.findViewById(R.id.ratingOfDay_tl);
        taskTime_tl = view.findViewById(R.id.taskTime_tl);


        //set task topic spinner
        ArrayAdapter<String> taskTopicArrayAdapter = new ArrayAdapter<String>(getActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,getResources().getStringArray(R.array.taskTopicSpinnerList));
        taskTopic_atv.setAdapter(taskTopicArrayAdapter);
        taskTopic_atv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskTopic_atv.showDropDown();
            }
        });


        taskTopic_atv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                taskTopic = taskTopic_atv.getAdapter().getItem(i).toString();
                Toast.makeText(getContext(), taskTopic, Toast.LENGTH_SHORT).show();
            }
        });



        //set task status spinner
        ArrayAdapter<String> taskStatusArrayAdapter = new ArrayAdapter<String>(getActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,getResources().getStringArray(R.array.taskStatusSpinnerList));
        taskStatus_atv.setAdapter(taskStatusArrayAdapter);
        taskStatus_atv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskStatus_atv.showDropDown();
            }
        });


        taskStatus_atv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                taskStatus = taskStatus_atv.getAdapter().getItem(i).toString();
                Toast.makeText(getContext(), taskStatus, Toast.LENGTH_SHORT).show();
            }
        });



        //set rating spinner
        ArrayAdapter<String> ratingOfDayArrayAdapter = new ArrayAdapter<String>(getActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,getResources().getStringArray(R.array.ratingSpinnerLsit));
        ratingOfDay_atv.setAdapter(ratingOfDayArrayAdapter);
        ratingOfDay_atv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ratingOfDay_atv.showDropDown();
            }
        });


        taskStatus_atv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ratingOfDay = Integer.parseInt(ratingOfDay_atv.getAdapter().getItem(i).toString());
                Toast.makeText(getContext(), ratingOfDay.toString(), Toast.LENGTH_SHORT).show();
            }
        });


        //setting on click color change to icons in text input layout
        taskTopic_atv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
//                Toast.makeText(LoginActivity.this, "on click email", Toast.LENGTH_SHORT).show();
                int color;
                if(b){
                    color = Color.parseColor("#0099ff");
                }else{
                    color = Color.parseColor("#808080");
                }
                taskTopic_tl.setStartIconTintList(ColorStateList.valueOf(color));
                taskTopic_atv.showDropDown();
            }
        });
        taskStatus_atv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
//                Toast.makeText(LoginActivity.this, "on click email", Toast.LENGTH_SHORT).show();
                int color;
                if(b){
                    color = Color.parseColor("#0099ff");
                }else{
                    color = Color.parseColor("#808080");
                }
                taskStatus_tl.setStartIconTintList(ColorStateList.valueOf(color));
                taskStatus_atv.showDropDown();
            }
        });
        ratingOfDay_atv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
//                Toast.makeText(LoginActivity.this, "on click email", Toast.LENGTH_SHORT).show();
                int color;
                if(b){
                    color = Color.parseColor("#0099ff");
                }else{
                    color = Color.parseColor("#808080");
                }
                ratingOfDay_tl.setStartIconTintList(ColorStateList.valueOf(color));
                ratingOfDay_atv.showDropDown();
            }
        });
        taskTime_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
//                Toast.makeText(LoginActivity.this, "on click email", Toast.LENGTH_SHORT).show();
                int color;
                if(b){
                    color = Color.parseColor("#0099ff");
                }else{
                    color = Color.parseColor("#808080");
                }
                taskTime_tl.setStartIconTintList(ColorStateList.valueOf(color));
            }
        });




        return view;
    }
}