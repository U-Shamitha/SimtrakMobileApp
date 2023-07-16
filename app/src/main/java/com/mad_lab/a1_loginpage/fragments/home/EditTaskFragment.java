package com.mad_lab.a1_loginpage.fragments.home;

import static android.content.Context.MODE_PRIVATE;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.mad_lab.a1_loginpage.R;

import java.util.Calendar;
import java.util.Map;

public class EditTaskFragment extends Fragment {

    String id, taskName, assignedDate, taskDescription, taskPriority, taskType, taskDeadline;

    TextInputLayout deadline_tl;
    EditText taskName_et;
    EditText assignedDate_et;
    EditText taskDescription_et;
    Spinner taskPriority_spinner;
    EditText deadline_et;
    EditText taskType_et;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_task, container, false);

        deadline_tl = view.findViewById(R.id.taskDeadline_tl);

        taskName_et = view.findViewById(R.id.taskName_et);
        assignedDate_et = view.findViewById(R.id.assignedDate_et);
        taskPriority_spinner = view.findViewById(R.id.taskPriorities_spinner);
//        taskType_et = view.findViewById(R.id.taskType_et);
        deadline_et = view.findViewById(R.id.taskDeadline_et);

        deadline_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });

        deadline_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
//                Toast.makeText(LoginActivity.this, "on click email", Toast.LENGTH_SHORT).show();
                int color;
                if(b){
                    color = Color.parseColor("#0099ff");
                }else{
                    color = Color.parseColor("#808080");
                }
                deadline_tl.setStartIconTintList(ColorStateList.valueOf(color));
                deadline_et.performClick();
            }
        });


        getDataFromSharedPrefernces("selectedTaskDetails");

        return view;
    }

    public void showDatePickerDialog(View v) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                deadline_et.setText(selectedDate);
            }
        }, year, month, day);

        datePickerDialog.show();

    }

    public void getDataFromSharedPrefernces(String key) {
        Log.d("DataRetInEditTask","in getData");
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("EditTaskDetails", MODE_PRIVATE);
        String jsonString = sharedPreferences.getString(key,"");
        Log.d("DataRetInEditTask",jsonString);
        if(jsonString!= null) {
            Map<String, Object> mapData = new Gson().fromJson(jsonString, new TypeToken<Map<String, Object>>() {
            }.getType());
            Log.d("DataRetInEditTask",mapData.toString());
            taskName_et.setText(mapData.get("TaskName") + "");
            assignedDate_et.setText(mapData.get("AssignedDate") + "");
//            taskPriority_et.setText(mapData.get("taskPriority") + "");
//            taskType_et.setText(mapData.get("taskType") + "");

        }

    }
}