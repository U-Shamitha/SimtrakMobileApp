package com.mad_lab.a1_loginpage.fragments.home;

import static android.content.Context.MODE_PRIVATE;

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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mad_lab.a1_loginpage.R;
import com.mad_lab.a1_loginpage.activity.DashboardActivity;
import com.mad_lab.a1_loginpage.activity.LoginActivity;
import com.mad_lab.a1_loginpage.activity.SignUpActivity;
import com.mad_lab.a1_loginpage.adapter.TaskPrioritySpinnerAdapter;
import com.mad_lab.a1_loginpage.inventory.PriorityData;
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup;
import com.nex3z.togglebuttongroup.button.LabelToggle;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class DashboardAddTaskFragment extends Fragment {

    TextInputLayout taskName_tl;
    TextInputEditText taskName_et;
    Spinner taskPriority_spinner;
    TaskPrioritySpinnerAdapter taskPrioritySpinner_adapter;
    LabelToggle continuous_label;
    LabelToggle recurring_label;
    SingleSelectToggleGroup taskType_tg;
    String selectedTaskType;
    Boolean isFirstCheck = true;

    TextView errMsg_tv;
    Button addTask_btn;

    String TAG= "AddTaskFragment";

    FirebaseFirestore fstore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard_add_task, container, false);

        taskName_tl = view.findViewById(R.id.taskName_tl);
        taskName_et = view.findViewById(R.id.taskName_et);
        taskPriority_spinner = view.findViewById(R.id.taskPriorities_spinner);
        continuous_label = view.findViewById(R.id.continuous_label);
        recurring_label = view.findViewById(R.id.recurring_label);
        taskType_tg = view.findViewById(R.id.tsakType_toogleGroup);
        errMsg_tv = view.findViewById(R.id.errorMsg_tv);
        addTask_btn = view.findViewById(R.id.addTask_btn);

        fstore = FirebaseFirestore.getInstance();

        taskName_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
//                Toast.makeText(LoginActivity.this, "on click email", Toast.LENGTH_SHORT).show();
                int color;
                if(b){
                    color = Color.parseColor("#0099ff");
                }else{
                    color = Color.parseColor("#808080");
                }
                taskName_tl.setStartIconTintList(ColorStateList.valueOf(color));
//                taskName_tl.setHintTextColor(ColorStateList.valueOf(color));

            }
        });
        taskType_tg.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {

                    if(checkedId == recurring_label.getId()) {
//                        Toast.makeText(getContext(), "1", Toast.LENGTH_SHORT).show();
                        selectedTaskType = "recurring";
//                        continuous_label.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
//                        recurring_label.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.dark_grey)));
                    }else{
//                        Toast.makeText(getContext(), "2", Toast.LENGTH_SHORT).show();
                        selectedTaskType = "continuous";
//                        continuous_label.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.dark_grey)));
//                        recurring_label.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    }
            }
        });

//        ArrayAdapter<String> taskPriorityArrAdapter = new ArrayAdapter<String>(getActivity(),androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.taskPrioritiesList)){
//            @Override
//            public boolean isEnabled(int position) {
//                if(position == 0 || position == 1 || position == 3){
//                    return false;
//                }
//                else{
//                    return true;
//                }
//            }
//
//            @Override
//            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//
//                View view1 = super.getDropDownView(position, convertView, parent);
//                TextView tv = (TextView) view1;
//                if(position == 0){
//                    tv.setTextColor(Color.GRAY);
//                }
//                else if(position == 1 || position == 3){
//                    tv.setTextColor(getResources().getColor(R.color.my_primary_color));
//                    tv.setTypeface(Typeface.MONOSPACE);
//                }else{
//                    tv.setTextColor(Color.BLACK);
//                }
//                return view1;
//            }
//        };
//        taskPriority_spinner.setAdapter(taskPriorityArrAdapter);

//        taskPriority_spinner.setSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener() {
//            @Override
//            public void onPopupWindowOpened(Spinner spinner) {
//                taskPriority_spinner.setBackground(getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24));
//            }
//
//            @Override
//            public void onPopupWindowClosed(Spinner spinner) {
//                taskPriority_spinner.setBackground(getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24));
//            }
//        });

        taskPrioritySpinner_adapter = new TaskPrioritySpinnerAdapter(getContext(), PriorityData.getPriorityList());
//        Toast.makeText(getContext(), "no "+  taskPrioritySpinner_adapter.getCount(), Toast.LENGTH_SHORT).show();
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

        taskPriority_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getContext(), ""+PriorityData.getPriorityList().get(i).getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addTask_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String taskName = taskName_et.getText().toString().trim();
                Integer selectedItemIndex = Integer.parseInt(taskPriority_spinner.getSelectedItem().toString());
                String taskPriority = PriorityData.getPriorityList().get(selectedItemIndex).getName();
                String taskType = selectedTaskType;
                String userId = getDataFromSharedPrefernces("userId");

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
                    String dateFormat = "dd-MM-yyyy";
                    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
                    Map<String, Object> task = new HashMap<>();
                    task.put("taskName", taskName);
                    task.put("taskPriority", selectedItemIndex);
                    task.put("taskType", taskType);
                    task.put("assignedDate", sdf.format(new Date()));
                    Log.d(TAG, task.toString());
                    documentReference.update("tasks", FieldValue.arrayUnion(task))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "task data uploaded to firestore");
                                    startActivity(new Intent(getActivity(), DashboardActivity.class));
                                    getActivity().finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "task data upload failed"+e.getMessage());
                                }
                            });



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

    public String getDataFromSharedPrefernces(String key) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserDetails", MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

    void storeDataInSharedPrefernces(String key, String value) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserDetails", MODE_PRIVATE);
        SharedPreferences.Editor  editor= sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }


}