package com.mad_lab.a1_loginpage.fragments.home;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import com.mad_lab.a1_loginpage.R;

import java.util.Calendar;

public class EditTaskFragment extends Fragment {

    String taskName, assignedDate, taskDescription, taskPriority, taskType;

    public EditTaskFragment(String taskName, String assignedDate, String taskDescription, String taskPriority, String taskType){
        this.taskName = taskName;
        this.assignedDate = assignedDate;
        this.taskDescription = taskDescription;
        this.taskPriority = taskPriority;
        this.taskType = taskType;
    }

    EditText deadline_et;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_task, container, false);

        deadline_et = view.findViewById(R.id.taskDeadline_et);

        deadline_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });

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
}