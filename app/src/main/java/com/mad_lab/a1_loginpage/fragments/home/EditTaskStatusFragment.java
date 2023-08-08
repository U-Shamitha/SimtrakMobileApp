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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class EditTaskStatusFragment extends Fragment {

    private  TextInputLayout taskStatus_tl;

    private AutoCompleteTextView taskStatus_atv;

    TextView errMsg_tv;
    Button editTaskStatus_btn;

    FirebaseFirestore fstore;

    String TAG= "EditTaskStatusFragment";

    String taskId ;
    String taskStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_task_status, container, false);

        taskStatus_tl = view.findViewById(R.id.taskStatus_tl);
        taskStatus_atv = view.findViewById(R.id.taskStatus_atv);
        editTaskStatus_btn = view.findViewById(R.id.editTaskStatus_btn);
        errMsg_tv = view.findViewById(R.id.errorMsg_tv);

        fstore = FirebaseFirestore.getInstance();


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


        ArrayAdapter<String> noteLinkTypeArrayAdapter = new ArrayAdapter<String>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.taskStatusSpinnerList));
        taskStatus_atv.setAdapter(noteLinkTypeArrayAdapter);
        taskStatus_atv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskStatus_atv.showDropDown();
            }
        });


        taskStatus_atv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = taskStatus_atv.getAdapter().getItem(i).toString();
//                Toast.makeText(getApplicationContext(), selectedItem, Toast.LENGTH_SHORT).show();
            }
        });



        editTaskStatus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errMsg_tv.setVisibility(View.GONE);
                errMsg_tv.setText("Error message appear here");
                taskStatus = taskStatus_atv.getText().toString().trim();


                if (TextUtils.isEmpty(taskStatus)) {
                    errMsg_tv.setText("Please select Task Type");
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
        String jsonString = sharedPreferences.getString(key, "");
        if (jsonString != null) {
            Map<String, Object> mapData = new Gson().fromJson(jsonString, new TypeToken<Map<String, Object>>() {
            }.getType());
            Toast.makeText(getContext(), "taskId: " + mapData.get("TaskId").toString(), Toast.LENGTH_LONG).show();
            taskId = mapData.get("TaskId").toString();
            if((mapData.get("TaskStatus") + "").equals("Completed")){
                taskStatus_atv.setText(mapData.get("TaskStatus") + "");
                errMsg_tv.setText("Status of completed task can't be changed");
                errMsg_tv.setVisibility(View.VISIBLE);
                editTaskStatus_btn.setEnabled(false);
            }else{
                taskStatus_atv.setText(mapData.get("TaskStatus") + "", false);
            }

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

                                        task.put("taskStatus", taskStatus);

                                        // Save the updated data back to the document
                                        documentReference.update("tasks",tasks)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(getContext(), "Task Status edited Successfully", Toast.LENGTH_LONG).show();
                                                        Intent intent= new Intent(getActivity(), DashboardActivity.class);
                                                        getActivity().startActivity(intent);
//                                                                    startActivity(new Intent(getActivity(), DashboardActivity.class));
                                                                    getActivity().finish();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getContext(), "Task Status didn't get added: "+e.getMessage(), Toast.LENGTH_LONG).show();
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