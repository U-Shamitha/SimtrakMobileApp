package com.mad_lab.a1_loginpage.activity.trainees;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mad_lab.a1_loginpage.R;
import com.mad_lab.a1_loginpage.activity.DashboardActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LeaveApplyFragment extends Fragment {

    TextInputLayout leaveFrom_tl, joiningDate_tl, reasonForLeave_tl, responsibilitiesPassedTo_tl;
    TextInputEditText leaveFrom_et, joiningDate_et, reasonForLeave_et, responsibilitiesPassedTo_et;

    Button applyLeave_btn;
    TextView errMsg_tv;
    String TAG = "LeaveApplyActivity";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_leave_apply, container, false);

        leaveFrom_tl = view.findViewById(R.id.leaveFrom_tl);
        joiningDate_tl = view.findViewById(R.id.joiningDate_tl);
        reasonForLeave_tl = view.findViewById(R.id.reasonForLeave_tl);
        responsibilitiesPassedTo_tl = view.findViewById(R.id.responsibilitiesPassedTo_tl);

        leaveFrom_et = view.findViewById(R.id.leaveFrom_et);
        joiningDate_et = view.findViewById(R.id.joiningDate_et);
        reasonForLeave_et = view.findViewById(R.id.reasonForLeave_et);
        responsibilitiesPassedTo_et = view.findViewById(R.id.responsibilitiesPassedTo_et);

        applyLeave_btn = view.findViewById(R.id.leaveApply_btn);
        errMsg_tv = view.findViewById(R.id.errorMsg_tv);

        changeIconColorOnFocus(leaveFrom_tl,leaveFrom_et, "start");
        changeIconColorOnFocus(joiningDate_tl,joiningDate_et, "start");
        changeIconColorOnFocus(reasonForLeave_tl,reasonForLeave_et, "");
        changeIconColorOnFocus(responsibilitiesPassedTo_tl,responsibilitiesPassedTo_et, "");

        setOnFocusListenerForDateInputEditText(leaveFrom_et, leaveFrom_tl);
        setOnFocusListenerForDateInputEditText(joiningDate_et, joiningDate_tl);

        applyLeave_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitLeave();
            }
        });



        return view;
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

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                et.setText(selectedDate);
            }
        }, year, month, day);

        datePickerDialog.show();

    }

    void submitLeave() {

        errMsg_tv.setVisibility(View.GONE);
        errMsg_tv.setText("Error message appear here");

        String leaveFrom = leaveFrom_et.getText().toString().trim();
        String joiningDate = joiningDate_et.getText().toString().trim();
        String reasonForLeave = reasonForLeave_et.getText().toString().trim();
        String responsibilitiesPassedTo = responsibilitiesPassedTo_et.getText().toString().trim();

        if(TextUtils.isEmpty(leaveFrom)){
            leaveFrom_et.setError("Please select leave start date");
            errMsg_tv.setText("Please select leave start date");
        }
        else if(TextUtils.isEmpty(joiningDate)){
            leaveFrom_et.setError("Please Select joining date");
            errMsg_tv.setText("Please Select joining date");
        }
        else if(TextUtils.isEmpty(reasonForLeave)){
            leaveFrom_et.setError("Please enter reason for leave");
            errMsg_tv.setText("Please enter reason for leave");
        }
        else if(TextUtils.isEmpty(responsibilitiesPassedTo)){
            leaveFrom_et.setError("Please enter responsibilities passed to whom");
            errMsg_tv.setText("Please enter responsibilities passed to whom");
        }
        else{
            storeLeaveDataInFireStoreDatabase(leaveFrom, joiningDate, "", "", reasonForLeave, responsibilitiesPassedTo, "in-process");
        }

        if(!errMsg_tv.getText().toString().equals("Error message appear here")){
            errMsg_tv.setVisibility(View.VISIBLE);
        }

    }
    public void storeLeaveDataInFireStoreDatabase(String startDate, String joiningDate, String adminRemarks, String leaderRemarks, String reasonForLeave, String responsibilitiesPassedTo, String status){

        FirebaseFirestore fstore = FirebaseFirestore.getInstance();
        String userId = getUserDetailsFromSharedPrefernces("userId");
        DocumentReference documentReference = fstore.collection("users").document(userId);
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            ArrayList<Map<String, Object>> leaves= (ArrayList<Map<String, Object>>) documentSnapshot.get("leaves")!=null ? (ArrayList<Map<String, Object>>) documentSnapshot.get("leaves") : new ArrayList<>();
//                                Integer taskId = tasks.size();
                            String dateFormat = "dd-MM-yyyy";
                            String dateTimeFormat = "dd-MM-yyyy hh:mm:ss";
                            SimpleDateFormat sdtf = new SimpleDateFormat(dateTimeFormat);
                            String leaveId = sdtf.format(new Date());
                            Map<String, Object> leave = new HashMap<>();
                            leave.put("leaveId", leaveId);
                            leave.put("startDate", startDate);
                            leave.put("joiningDate", joiningDate);
                            leave.put("adminRemarks", adminRemarks);
                            leave.put("leaderRemarks", leaderRemarks);
                            leave.put("reasonForLeave", reasonForLeave);
                            leave.put("responsibilitiesPassedTo" +
                                    "", responsibilitiesPassedTo);
                            leave.put("status", status);
                            Log.d(TAG, leave.toString());
                            documentReference.update("leaves", FieldValue.arrayUnion(leave))
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d(TAG, "leave data uploaded to firestore");
                                            Intent intent = new Intent(getContext(), DashboardActivity.class);
                                            intent.putExtra("DesFragment", "ViewLeaves");
                                            startActivity(intent);
                                            getActivity().finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "leave data upload failed"+e.getMessage());
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "user doc not found"+e.getMessage());
                    }
                });


    }

    public String getUserDetailsFromSharedPrefernces(String key) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserDetails", MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }


}