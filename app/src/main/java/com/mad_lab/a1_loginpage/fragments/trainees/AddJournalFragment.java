package com.mad_lab.a1_loginpage.fragments.trainees;

import static android.content.Context.MODE_PRIVATE;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.mad_lab.a1_loginpage.inventory.PriorityData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddJournalFragment extends Fragment {

    AutoCompleteTextView taskTopic_atv;
    AutoCompleteTextView taskStatus_atv;
    AutoCompleteTextView ratingOfDay_atv;
    TextInputEditText taskTime_et;

    TextInputLayout taskTopic_tl;
    TextInputLayout taskStatus_tl;
    TextInputLayout ratingOfDay_tl;
    TextInputLayout taskTime_tl;

    TextInputLayout taskNote_tl, learnings_tl, otherDevelopments_tl, meetingsAttended_tl;

    EditText taskNote_et, learnings_et, otherDevelopments_et, meetingsAttended_et;

    String taskTopic;
    String taskStatus;
    Float ratingOfDay;
    TextView errMsg_tv;
    Button addJournal_btn;

    String TAG;


    FirebaseFirestore fstore;


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

        taskNote_tl = view.findViewById(R.id.taskNote_tl);
        learnings_tl = view.findViewById(R.id.learnings_tl);
        otherDevelopments_tl = view.findViewById(R.id.otherDevelopments_tl);
        meetingsAttended_tl = view.findViewById(R.id.meetingsAttended_tl);

        taskNote_et = view.findViewById(R.id.taskNote_et);
        learnings_et = view.findViewById(R.id.learnings_et);
        otherDevelopments_et = view.findViewById(R.id.otherDevelopments_et);
        meetingsAttended_et = view.findViewById(R.id.meetingsAttended_et);

        errMsg_tv = view.findViewById(R.id.errorMsg_tv);
        addJournal_btn = view.findViewById(R.id.addJournal_btn);

        fstore = FirebaseFirestore.getInstance();


        //set task topic spinner
        ArrayAdapter<String> taskTopicArrayAdapter = new ArrayAdapter<String>(getActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,getResources().getStringArray(R.array.taskTopicSpinnerList));
        taskTopic_atv.setAdapter(taskTopicArrayAdapter);
        taskTopic_atv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(taskTopic_atv.isPopupShowing()){
                    taskTopic_atv.dismissDropDown();
                }else{
                    taskTopic_atv.showDropDown();
                }
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
                if(taskStatus_atv.isPopupShowing()){
                    taskStatus_atv.dismissDropDown();
                }else{
                    taskStatus_atv.showDropDown();
                }
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


        ratingOfDay_atv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ratingOfDay = Float.valueOf(ratingOfDay_atv.getAdapter().getItem(i).toString());
                Toast.makeText(getContext(), ratingOfDay.toString(), Toast.LENGTH_SHORT).show();
            }
        });


        //setting on click color change to icons in text input layout
        taskTopic_atv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
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
                int color;
                if(b){
                    color = Color.parseColor("#0099ff");
                }else{
                    color = Color.parseColor("#808080");
                }
                taskTime_tl.setStartIconTintList(ColorStateList.valueOf(color));
            }
        });

        addJournal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                errMsg_tv.setVisibility(View.GONE);
                errMsg_tv.setText("Error message appear here");

                String userId = getDataFromSharedPrefernces("userId");
                String taskTopic = taskTopic_atv.getText().toString().trim();
                String taskNote = taskNote_et.getText().toString().trim();
                String learnings = learnings_et.getText().toString().trim();
                String taskStatus = taskStatus_atv.getText().toString().trim();
                String timeTaken = taskTime_et.getText().toString().trim();
                String otherDevelopments = otherDevelopments_et.getText().toString().trim();
                String meetingsAttended = meetingsAttended_et.getText().toString().trim();
                ratingOfDay = Float.valueOf(0);
                try{
                    ratingOfDay = Float.parseFloat(ratingOfDay_atv.getText().toString().trim());
                }catch (Exception e){
                    Log.d("AddJournalFragment", "error in processing rating: "+e.getMessage());
                }

                if(TextUtils.isEmpty(taskTopic)){
                    errMsg_tv.setText("Please select task topic");
                }
                else if (TextUtils.isEmpty(taskNote)) {
                    taskNote_et.setError("Please enter task note");
                    errMsg_tv.setText("Please enter task note");
                }
                else if (TextUtils.isEmpty(learnings)) {
                    learnings_et.setError("Please enter learnings");
                    errMsg_tv.setText("Please enter learnings");
                }
                else if(TextUtils.isEmpty(taskStatus)){
                    errMsg_tv.setText("Please select task status");
                }
                else if (TextUtils.isEmpty(timeTaken)) {
                    taskTime_et.setError("Please enter time taken");
                    errMsg_tv.setText("Please enter time taken");
                }
                else if (TextUtils.isEmpty(otherDevelopments)) {
                    otherDevelopments_et.setError("Please enter other developments");
                    errMsg_tv.setText("Please enter other developments");
                }
                else if (TextUtils.isEmpty(meetingsAttended)) {
                    meetingsAttended_et.setError("Please enter meetings attended");
                    errMsg_tv.setText("Please enter meetings attended");
                }
                else if(ratingOfDay == null){
                    errMsg_tv.setText("Please select rating of day");
                }
                else{

                    DocumentReference documentReference = fstore.collection("users").document(userId);
                    documentReference.get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if(documentSnapshot.exists()){
                                        ArrayList<Map<String, Object>> tasks= (ArrayList<Map<String, Object>>) documentSnapshot.get("journals")!=null ? (ArrayList<Map<String, Object>>) documentSnapshot.get("journals") : new ArrayList<>();
//                                Integer taskId = tasks.size();
                                        String dateFormat = "dd-MM-yyyy";
                                        String dateTimeFormat = "dd-MM-yyyy hh:mm:ss";
                                        SimpleDateFormat sdtf = new SimpleDateFormat(dateTimeFormat);
                                        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
                                        String journalId = sdtf.format(new Date());
                                        Map<String, Object> journal = new HashMap<>();
                                        journal.put("journalId", journalId);
                                        journal.put("date", sdf.format(new Date()));
                                        journal.put("report", "report");
                                        journal.put("learnings", learnings);
                                        journal.put("taskTopic", taskTopic);
                                        journal.put("taskNote", taskNote);
                                        journal.put("taskStatus", taskStatus);
                                        journal.put("timeTaken", timeTaken);
                                        journal.put("otherDevelopments", otherDevelopments);
                                        journal.put("meetingsAttended", meetingsAttended);
                                        journal.put("pendings", "pendings");
                                        journal.put("adminRemarks", "adminRemarks");
                                        journal.put("leaderRemarks", "leaderRemarks");
                                        journal.put("ratingOfDay", ratingOfDay);
                                        journal.put("journalStatus", "in-process");
                                        Log.d(TAG, journal.toString());
                                        documentReference.update("journals", FieldValue.arrayUnion(journal))
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Log.d(TAG, "Journal data uploaded yo firestore successfully");
                                                        Toast.makeText(getContext(), "Journal added Successfully", Toast.LENGTH_LONG).show();
                                                        Intent intent= new Intent(getActivity(), DashboardActivity.class);
                                                        intent.putExtra("DesFragment","ViewJournals");
                                                        getActivity().startActivity(intent);
//                                                                    startActivity(new Intent(getActivity(), DashboardActivity.class));
                                                        getActivity().finish();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d(TAG, "Journal data upload failed"+e.getMessage());
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

                if(!errMsg_tv.getText().toString().equals("Error message appear here")){
                    errMsg_tv.setVisibility(View.VISIBLE);
                }
            }
        });


        return view;
    }

    public String getDataFromSharedPrefernces(String key) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserDetails", MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }
}