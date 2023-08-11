package com.mad_lab.a1_loginpage.activity.trainees;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mad_lab.a1_loginpage.R;

import java.util.Calendar;

public class LeaveApplyActivity extends AppCompatActivity {

    TextInputLayout leaveFrom_tl, joiningDate_tl, reasonForLeave_tl, responsibilitiesPassedTo_tl;
    TextInputEditText leaveFrom_et, joiningDate_et, reasonForLeave_et, responsibilitiesPassedTo_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_apply);

        leaveFrom_tl = findViewById(R.id.leaveFrom_tl);
        joiningDate_tl = findViewById(R.id.joiningDate_tl);
        reasonForLeave_tl = findViewById(R.id.reasonForLeave_tl);
        responsibilitiesPassedTo_tl = findViewById(R.id.responsibilitiesPassedTo_tl);

        leaveFrom_et = findViewById(R.id.leaveFrom_et);
        joiningDate_et = findViewById(R.id.joiningDate_et);
        reasonForLeave_et = findViewById(R.id.reasonForLeave_et);
        responsibilitiesPassedTo_et = findViewById(R.id.responsibilitiesPassedTo_et);

        changeIconColorOnFocus(leaveFrom_tl,leaveFrom_et, "start");
        changeIconColorOnFocus(joiningDate_tl,joiningDate_et, "start");
        changeIconColorOnFocus(reasonForLeave_tl,reasonForLeave_et, "");
        changeIconColorOnFocus(responsibilitiesPassedTo_tl,responsibilitiesPassedTo_et, "");

        setOnFocusListenerForDateInputEditText(leaveFrom_et, leaveFrom_tl);
        setOnFocusListenerForDateInputEditText(joiningDate_et, joiningDate_tl);

    }



    public void changeIconColorOnFocus(TextInputLayout tl, TextInputEditText et, String iconPosition) {
        if (et != null) {
            et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    int color;
                    if (b) {
                        color = Color.parseColor("#0099ff");
                    } else {
                        color = Color.parseColor("#808080");
                    }
                    if (iconPosition.equals("start")) {
                        tl.setStartIconTintList(ColorStateList.valueOf(color));
                    } else if (iconPosition.equals("end")) {
                        tl.setEndIconTintList(ColorStateList.valueOf(color));
                    }
                }
            });
        }
    }

    public void setOnFocusListenerForDateInputEditText(TextInputEditText et, TextInputLayout tl){
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                int color;
                if(b){
                    color = Color.parseColor("#0099ff");
                    showDatePickerDialog(view, et);
                }else{
                    color = Color.parseColor("#808080");
                }
                tl.setStartIconTintList(ColorStateList.valueOf(color));
            }
        });
        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view, et);
            }
        });
    }

    public void showDatePickerDialog(View v, TextInputEditText et) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                et.setText(selectedDate);
            }
        }, year, month, day);

        datePickerDialog.show();

    }


}

