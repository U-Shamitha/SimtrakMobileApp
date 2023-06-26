package com.mad_lab.a1_loginpage.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mad_lab.a1_loginpage.R;
import com.mad_lab.a1_loginpage.fragments.home.DashboardAddTaskFragment;

public class ProfileFragment extends Fragment {

    AutoCompleteTextView taskFilter_actv;
    String taskFilterType="";
    ImageButton spinnerCloseButton;
    ImageButton spinnerSearchButton;
    Fragment fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        taskFilter_actv = view.findViewById(R.id.taskFilterActv);
        spinnerCloseButton = view.findViewById(R.id.spinner_close_button);
        spinnerSearchButton = view.findViewById(R.id.spinner_search_button);
        fragment = new AllTasksFragment();

        ArrayAdapter<String> taskFilterArrayAdapter = new ArrayAdapter<String>(getActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,getResources().getStringArray(R.array.taskFilters));
        taskFilter_actv.setAdapter(taskFilterArrayAdapter);
        taskFilter_actv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskFilter_actv.showDropDown();
            }
        });


        taskFilter_actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                taskFilterType = (String) taskFilter_actv.getAdapter().getItem(i).toString();
                Toast.makeText(getContext(), taskFilterType, Toast.LENGTH_SHORT).show();
//                spinnerCloseButton.setVisibility(View.VISIBLE);

            }
        });

        spinnerCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskFilter_actv.setText("");
                taskFilterType = "";
            }
        });

        spinnerSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                if(taskFilterType.equals("Add Task")) {
                    fragment = new DashboardAddTaskFragment();
                    loadFragment(fragment);
                }
                else if(taskFilterType.equals("WR Slot")) {
                    fragment = new AllTasksFragment();
                    loadFragment(fragment);
                }
                else if(taskFilterType.equals("Internship Extension")) {
                    fragment = new AllTasksFragment();
                    loadFragment(fragment);
                }
                else if(taskFilterType.equals("Upload Photo")) {
                    fragment = new AllTasksFragment();
                    loadFragment(fragment);
                }
                else if(taskFilterType.equals("All tasks")) {
                    fragment = new AllTasksFragment();
                    loadFragment(fragment);
                }
                else if(taskFilterType.equals("Ongoing tasks")) {
                    fragment = new AllTasksFragment();
                    loadFragment(fragment);
                }
                else if(taskFilterType.equals("Completed tasks")) {
                    fragment = new AllTasksFragment();
                    loadFragment(fragment);
                }
                else if(taskFilterType.equals("Paused tasks")) {
                    fragment = new AllTasksFragment();
                    loadFragment(fragment);
                }
                else if(taskFilterType.equals("Purged tasks")) {
                    fragment = new AllTasksFragment();
                    loadFragment(fragment);
                }
                else if(taskFilterType.equals("Recurring tasks")) {
                    fragment = new AllTasksFragment();
                    loadFragment(fragment);
                }
                else if(taskFilterType.equals("Leave")) {
                    fragment = new AllTasksFragment();
                    loadFragment(fragment);
                }
                else if(taskFilterType.equals("My learnings")) {
                    fragment = new AllTasksFragment();
                    loadFragment(fragment);
                }
                else if(taskFilterType.equals("Profile")) {
                    fragment = new AllTasksFragment();
                    loadFragment(fragment);
                }
                else if(taskFilterType.equals("Certificates")) {
                    fragment = new AllTasksFragment();
                    loadFragment(fragment);
                }
                else if(taskFilterType.equals("Journal")) {
                    fragment = new AllTasksFragment();
                    loadFragment(fragment);
                }
                else if(taskFilterType.equals("Reviews")) {
                    fragment = new AllTasksFragment();
                    loadFragment(fragment);
                }
                else if(taskFilterType.equals("Overall data")) {
                    fragment = new AllTasksFragment();
                    loadFragment(fragment);
                }
                else {
                    if (fragment.isAdded()){
                        removeFragment(fragment);
                    }
                }
            }
        });



        return view;
    }

    private void loadFragment() {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.filteredTasksContainer, new AllTasksFragment());
        ft.commit();
    }
    private void removeFragment(Fragment fragment) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }
    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.filteredTasksContainer, fragment);
        ft.commit();
    }
}