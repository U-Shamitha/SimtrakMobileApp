package com.mad_lab.a1_loginpage.fragments.home;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.common.reflect.TypeToken;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.mad_lab.a1_loginpage.R;
import com.mad_lab.a1_loginpage.activity.DashboardActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AddNoteLinkFragment extends Fragment {

    TextInputLayout noteLink_tl;
    TextInputEditText noteLink_et;

    private  TextInputLayout noteLinkType_tl;

    private AutoCompleteTextView noteLinkType_atv;

    TextView errMsg_tv;
    Button addNoteLink_btn;

    FirebaseFirestore fstore;

    String TAG= "AddNoteLinkFragment";

    String taskId ;
    String noteLinkId, noteLink, noteLinkType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_note_link, container, false);

        noteLink_tl = view.findViewById(R.id.noteLink_tl);
        noteLink_et = view.findViewById(R.id.noteLink_et);
        noteLinkType_tl = view.findViewById(R.id.noteLinkType_tl);
        noteLinkType_atv = view.findViewById(R.id.noteLinkType_atv);
        addNoteLink_btn = view.findViewById(R.id.editNoteLink_btn);
        errMsg_tv = view.findViewById(R.id.errorMsg_tv);

        fstore = FirebaseFirestore.getInstance();


        noteLink_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                int color;
                if(b){
                    color = Color.parseColor("#0099ff");
                }else{
                    color = Color.parseColor("#808080");
                }
                noteLink_tl.setStartIconTintList(ColorStateList.valueOf(color));
            }
        });

        noteLinkType_atv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                int color;
                if(b){
                    color = Color.parseColor("#0099ff");
                }else{
                    color = Color.parseColor("#808080");
                }
                noteLinkType_tl.setStartIconTintList(ColorStateList.valueOf(color));

                noteLinkType_atv.showDropDown();
            }
        });


        ArrayAdapter<String> noteLinkTypeArrayAdapter = new ArrayAdapter<String>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.noteLinkTypeSpinnerList));
        noteLinkType_atv.setAdapter(noteLinkTypeArrayAdapter);
        noteLinkType_atv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteLinkType_atv.showDropDown();
            }
        });


        noteLinkType_atv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = noteLinkType_atv.getAdapter().getItem(i).toString();
//                Toast.makeText(getApplicationContext(), selectedItem, Toast.LENGTH_SHORT).show();
            }
        });



        addNoteLink_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errMsg_tv.setVisibility(View.GONE);
                errMsg_tv.setText("Error message appear here");
                noteLink = noteLink_et.getText().toString().trim();
                noteLinkType = noteLinkType_atv.getText().toString().trim();


                if(TextUtils.isEmpty(noteLink)){
                    noteLink_et.setError("Please enter note");
                    errMsg_tv.setText("Please enter note");
                }
                else if (TextUtils.isEmpty(noteLinkType)) {
                    errMsg_tv.setText("Please select link type");
                }
                else {
                    performSubmission(view);
                }

                if(!errMsg_tv.getText().toString().equals("Error message appear here")){
                    errMsg_tv.setVisibility(View.VISIBLE);
                }
            }
        });


        getTaskDataFromSharedPrefernces("selectedTaskDetails");


        return view;
    }

    public String getUserIdFromSharedPrefernces(String key) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserDetails", MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

    public void getTaskDataFromSharedPrefernces(String key) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("EditTaskDetails", MODE_PRIVATE);
        String jsonString = sharedPreferences.getString(key,"");
        if(jsonString!= null) {
            Map<String, Object> mapData = new Gson().fromJson(jsonString, new TypeToken<Map<String, Object>>() {
            }.getType());
            Toast.makeText(getContext(), "taskId: "+mapData.get("TaskId").toString(), Toast.LENGTH_LONG).show();
            taskId = mapData.get("TaskId").toString();
        }

    }





    public void performSubmission(View view){
        String userId = getUserIdFromSharedPrefernces("userId");
        DocumentReference documentReference = fstore.collection("users").document(userId);
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            ArrayList<Map<String, Object>> tasks= (ArrayList<Map<String, Object>>) documentSnapshot.get("tasks")!=null ? (ArrayList<Map<String, Object>>) documentSnapshot.get("tasks") : new ArrayList<>();
//                                        String dateTimeFormat = "dd-MM-yyyy hh:mm:ss";
//                                        SimpleDateFormat sdtf = new SimpleDateFormat(dateTimeFormat);
//                                        noteId = sdtf.format(new Date());

                            if (tasks != null) {
                                for (Map<String, Object> task : tasks) {
                                    Log.d("all tasks", task.toString());
                                    if (taskId.equals(task.get("taskId"))) {
//
                                        ArrayList<Map<String, Object>> noteLinksinTask = (ArrayList<Map<String, Object>>) task.get("noteLinks")!=null ? (ArrayList<Map<String, Object>>) task.get("noteLinks") : new ArrayList<>();
                                        Log.d("noteLinksInTask", noteLinksinTask.toString());

                                        String dateTimeFormat = "dd-MM-yyyy hh:mm:ss";
                                        SimpleDateFormat sdtf = new SimpleDateFormat(dateTimeFormat);
                                        noteLinkId = sdtf.format(new Date());

                                        Map<String, Object> noteLinkMap = new HashMap<>();
                                        noteLinkMap.put("noteLinkId", noteLinkId);
                                        noteLinkMap.put("noteLink", noteLink);
                                        noteLinkMap.put("noteLinkType", noteLinkType);

                                        noteLinksinTask.add(noteLinkMap);

                                        task.put("noteLinks",noteLinksinTask);
                                        Log.d("noteLinksInTask", noteLinksinTask.toString());
                                        Log.d(TAG, task.toString());

                                        // Save the updated data back to the document
                                        documentReference.update("tasks",tasks)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(getContext(), "Note Link added Successfully", Toast.LENGTH_LONG).show();
                                                        Intent intent= new Intent(getActivity(), DashboardActivity.class);
                                                        intent.putExtra("DesFragment","ViewNoteLinks");
                                                        getActivity().startActivity(intent);
//                                                                    startActivity(new Intent(getActivity(), DashboardActivity.class));
                                                                    getActivity().finish();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getContext(), "Note Link didn't get added: "+e.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                });


                                        break; // Assuming there is only one matching item
                                    }
                                }
                            }
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


}