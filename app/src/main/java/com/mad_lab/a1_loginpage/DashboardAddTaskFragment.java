package com.mad_lab.a1_loginpage;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mad_lab.a1_loginpage.inventory.Priority;
import com.mad_lab.a1_loginpage.inventory.PriorityData;
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup;
import com.nex3z.togglebuttongroup.ToggleButtonGroup;
import com.nex3z.togglebuttongroup.button.LabelToggle;
import com.nex3z.togglebuttongroup.button.OnCheckedChangeListener;


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
                        selectedTaskType = "continuous";
//                        continuous_label.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
//                        recurring_label.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.dark_grey)));
                    }else{
//                        Toast.makeText(getContext(), "2", Toast.LENGTH_SHORT).show();
                        selectedTaskType = "recurring";
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

        return view;
    }


}