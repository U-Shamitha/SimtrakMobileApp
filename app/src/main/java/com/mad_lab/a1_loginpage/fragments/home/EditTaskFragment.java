package com.mad_lab.a1_loginpage.fragments.home;

import static android.content.Context.MODE_PRIVATE;

import android.app.DatePickerDialog;
import android.content.Context;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.common.reflect.TypeToken;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.google.firestore.v1.WriteResult;
import com.google.gson.Gson;
import com.mad_lab.a1_loginpage.R;
import com.mad_lab.a1_loginpage.activity.DashboardActivity;
import com.mad_lab.a1_loginpage.adapter.TaskPrioritySpinnerAdapter;
import com.mad_lab.a1_loginpage.inventory.PriorityData;
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup;
import com.nex3z.togglebuttongroup.button.LabelToggle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class EditTaskFragment extends Fragment {

    String id, taskName, assignedDate, taskDescription, taskPriority, taskType, taskDeadline;

    TextInputLayout taskName_tl;
    TextInputLayout assignedDate_tl;
    TextInputLayout taskDes_tl;
    TextInputLayout taskPriority_tl;
    TextInputLayout deadline_tl;
    TextInputEditText taskName_et;
    TextInputEditText assignedDate_et;
    TextInputEditText taskDes_et;
    Spinner taskPriority_spinner;
    TaskPrioritySpinnerAdapter taskPrioritySpinner_adapter;
    TextInputEditText deadline_et;
    TextInputEditText taskType_et;

    LabelToggle continuous_label;
    LabelToggle recurring_label;
    SingleSelectToggleGroup taskType_tg;
    String selectedTaskType;

    TextView errMsg_tv;
    Button editTask_btn;

    FirebaseFirestore fstore;

    String TAG = "EditTaskFragment";
    String taskId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_task, container, false);

        taskName_tl = view.findViewById(R.id.taskName_tl);
        assignedDate_tl = view.findViewById(R.id.assignedDate_tl);
        taskDes_tl = view.findViewById(R.id.taskDes_tl);
        deadline_tl = view.findViewById(R.id.taskDeadline_tl);

        taskName_et = view.findViewById(R.id.taskName_et);
        assignedDate_et = view.findViewById(R.id.assignedDate_et);
        taskPriority_spinner = view.findViewById(R.id.taskPriorities_spinner);
        taskDes_et = view.findViewById(R.id.taskDes_et);
        deadline_et = view.findViewById(R.id.taskDeadline_et);
        continuous_label = view.findViewById(R.id.continuous_label);
        recurring_label = view.findViewById(R.id.recurring_label);
        taskType_tg = view.findViewById(R.id.tsakType_toogleGroup);

        errMsg_tv = view.findViewById(R.id.errorMsg_tv);
        editTask_btn = view.findViewById(R.id.editTask_btn);

        fstore = FirebaseFirestore.getInstance();


        changeIconColorOnFocus(taskName_tl, taskName_et, null, "start");
//        changeIconColorOnFocus(assignedDate_tl, assignedDate_et, null, "start");
        changeIconColorOnFocus(taskDes_tl, taskDes_et, null, "start");

        deadline_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                int color;
                if(b){
                    color = Color.parseColor("#0099ff");
                    showDatePickerDialog(view);
                }else{
                    color = Color.parseColor("#808080");
                }
                deadline_tl.setStartIconTintList(ColorStateList.valueOf(color));
            }
        });
        deadline_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });

        taskPrioritySpinner_adapter = new TaskPrioritySpinnerAdapter(getContext(), PriorityData.getPriorityList());
        taskPriority_spinner.setAdapter(taskPrioritySpinner_adapter);
        taskPriority_spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });


        taskType_tg.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {

                if(checkedId == recurring_label.getId()) {
                    selectedTaskType = "recurring";
                }else{
                    selectedTaskType = "continuous";
                }
            }
        });

        getDataFromSharedPrefernces("selectedTaskDetails");

        editTask_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String taskName = taskName_et.getText().toString().trim();
                Integer selectedItemIndex = Integer.parseInt(taskPriority_spinner.getSelectedItem().toString());
                String taskPriority = PriorityData.getPriorityList().get(selectedItemIndex).getName();
                String taskType = selectedTaskType;
                String deadline = deadline_et.getText().toString().trim();
                String description = taskDes_et.getText().toString().trim();
                String userId = getUserIdFromSharedPrefernces("userId");

                if(TextUtils.isEmpty(taskName)){
                    taskName_et.setError("Please enter task name");
                    errMsg_tv.setText("Please enter task name");
                }
                else if (TextUtils.isEmpty(taskPriority)) {
                    errMsg_tv.setText("Please enter task priority");
                }
                else if (TextUtils.isEmpty(taskType)) {
                    errMsg_tv.setText("Please enter task type");
                }
                else{

                    DocumentReference documentReference = fstore.collection("users").document(userId);
                    documentReference.get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if(documentSnapshot.exists()){
                                        ArrayList<Map<String, Object>> tasks= (ArrayList<Map<String, Object>>) documentSnapshot.get("tasks")!=null ? (ArrayList<Map<String, Object>>) documentSnapshot.get("tasks") : new ArrayList<>();
                                        String dateFormat = "dd-MM-yyyy";
                                        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

                                        if (tasks != null) {
                                            for (Map<String, Object> task : tasks) {
                                                Log.d("all tasks", task.toString());
                                                if (taskId.equals(task.get("taskId"))) {
//                                                    task.put("taskId", taskId);
                                                    task.put("taskName", taskName);
                                                    task.put("taskPriority", selectedItemIndex);
                                                    task.put("taskType", taskType);
                                                    task.put("deadline", deadline);
                                                    task.put("description", description);

//                                                        task.put("assignedDate", sdf.format(new Date()));
//                                                    task.put("assignedBy", "self");
                                                    Log.d(TAG, task.toString());

                                                    // Save the updated data back to the document
                                                    documentReference.update("tasks",tasks)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Toast.makeText(getContext(), "Task Updated Successfully", Toast.LENGTH_LONG).show();
                                                                startActivity(new Intent(getActivity(), DashboardActivity.class));
                                                                getActivity().finish();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(getContext(), "Update Failed: "+e.getMessage(), Toast.LENGTH_LONG).show();
                                                            }
                                                        });


                                                    break; // Assuming there is only one matching item
                                                }
                                            }
                                        }
//                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                    @Override
//                                                    public void onSuccess(Void unused) {
//                                                        Log.d(TAG, "task data uploaded to firestore");
//                                                        startActivity(new Intent(getActivity(), DashboardActivity.class));
//                                                        getActivity().finish();
//                                                    }
//                                                })
//                                                .addOnFailureListener(new OnFailureListener() {
//                                                    @Override
//                                                    public void onFailure(@NonNull Exception e) {
//                                                        Log.d(TAG, "task data upload failed"+e.getMessage());
//                                                    }
//                                                });
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "user doc not found"+e.getMessage());
                                }
                            });
                    ;



//                    Map<String, Object> docData = new HashMap<>();
//                    docData.put("favFoods", Arrays.asList("Hamburger", "Vegetables"));

//                    documentReference.set(task)
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void unused) {
////                                    Toast.makeText(getContext(), "task data uploaded to firestore", Toast.LENGTH_SHORT).show();
//                                    storeDataInSharedPrefernces("userId", userId);
//                                    Log.d(TAG, "task data uploaded to firestore");
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Log.e(TAG, "unable to upload user data to firestore");
//                                }
//                            });

                }

                if(!errMsg_tv.getText().toString().equals("Error message appear here")){
                    errMsg_tv.setVisibility(View.VISIBLE);
                }
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

    public String getUserIdFromSharedPrefernces(String key) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserDetails", MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

    public void getDataFromSharedPrefernces(String key) {
        Log.d("DataRetInEditTask","in getData");
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("EditTaskDetails", MODE_PRIVATE);
        String jsonString = sharedPreferences.getString(key,"");
        Log.d("DataRetInEditTask",jsonString);
        if(jsonString!= null) {
            Map<String, Object> mapData = new Gson().fromJson(jsonString, new TypeToken<Map<String, Object>>() {
            }.getType());

            taskName_et.setText(mapData.get("TaskName") + "");
            assignedDate_et.setText(mapData.get("AssignedDate") + "");
            deadline_et.setText(mapData.get("Deadline").toString());
            taskDes_et.setText(mapData.get("Description").toString());
            taskId = mapData.get("TaskId").toString();

            //set task type
            if(mapData.get("TaskType").equals("continuous")){
                continuous_label.performClick();
            } else if (mapData.get("TaskType").equals("recurring")) {
                recurring_label.performClick();
            }

            //set spinner
            Log.d("TaskPriority",mapData.get("TaskPriority").toString());
            String[] priorities = {"0-2 days", "3-7 days", "over 7 days"};
            for (int i=0; i<priorities.length; i++) {
                if(priorities[i].equals(mapData.get("TaskPriority"))){
                    Log.d("TaskPriority_sel",mapData.get("TaskPriority").toString());
                    taskPriority_spinner.setSelection(i+1);
                }
            }
        }

    }


    public void changeIconColorOnFocus(TextInputLayout tl, TextInputEditText et, AutoCompleteTextView atv, String iconPositon) {
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
                    if (iconPositon.equals("start")) {
                        tl.setStartIconTintList(ColorStateList.valueOf(color));
                    } else if (iconPositon.equals("end")) {
                        tl.setEndIconTintList(ColorStateList.valueOf(color));
                    }
                }
            });
        }
        if(atv != null){
            atv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    int color;
                    if (b) {
                        color = Color.parseColor("#0099ff");
                    } else {
                        color = Color.parseColor("#808080");
                    }
                    if (iconPositon.equals("start")) {
                        tl.setStartIconTintList(ColorStateList.valueOf(color));
                    } else if (iconPositon.equals("end")) {
                        tl.setEndIconTintList(ColorStateList.valueOf(color));
                    }
                    atv.showDropDown();
                }
            });
        }
    }
}