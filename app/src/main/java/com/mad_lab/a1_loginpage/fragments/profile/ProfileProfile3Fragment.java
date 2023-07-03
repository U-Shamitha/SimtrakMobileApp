package com.mad_lab.a1_loginpage.fragments.profile;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mad_lab.a1_loginpage.R;

import org.w3c.dom.Text;

public class ProfileProfile3Fragment extends Fragment {

    TextView name_tv;
    TextView status_tv;
    TextView domain_tv;
    TextView userGroup_tv;
    TextView email_tv;
    TextView phoneNumebr_tv;
    TextView dob_tv;
    TextView city_tv;
    TextView pincode_tv;
    TextView state_tv;
    TextView country_tv;
    TextView refPerson_tv;
    TextView refPersonPhone_tv;
    TextView org_tv;
    TextView education_tv;
    TextView commitments_tv;
    TextView roj_tv;
    TextView isAcaInternship_tv;
    TextView startDate_tv;
    TextView endDate_tv;
    TextView applyingFor_tv;
    TextView lastLogin_tv;
    TextView dataTimestamp_tv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_profile3, container, false);

        name_tv = view.findViewById(R.id.name_tv);
        status_tv = view.findViewById(R.id.status_tv);
        domain_tv = view.findViewById(R.id.domain_tv);
        userGroup_tv = view.findViewById(R.id.userGroup_tv);
        email_tv = view.findViewById(R.id.email_tv);
        phoneNumebr_tv = view.findViewById(R.id.phoneNumber_tv);
        dob_tv = view.findViewById(R.id.dob_tv);
        city_tv = view.findViewById(R.id.city_tv);
        pincode_tv = view.findViewById(R.id.pincode_tv);
        state_tv = view.findViewById(R.id.state_tv);
        country_tv = view.findViewById(R.id.country_tv);
        refPerson_tv = view.findViewById(R.id.refPerson_tv);
        refPersonPhone_tv = view.findViewById(R.id.refPersonPhone_tv);
        org_tv = view.findViewById(R.id.org_tv);
        education_tv = view.findViewById(R.id.education_tv);
        commitments_tv = view.findViewById(R.id.commitments_tv);
        roj_tv = view.findViewById(R.id.roj_tv);
        isAcaInternship_tv = view.findViewById(R.id.isAcaInternship_tv);
        startDate_tv = view.findViewById(R.id.startDate_tv);
        endDate_tv = view.findViewById(R.id.endDate_tv);
        applyingFor_tv = view.findViewById(R.id.appliedFor_tv);
        lastLogin_tv = view.findViewById(R.id.lastLogIn_tv);
        dataTimestamp_tv = view.findViewById(R.id.dataTimestamp_tv);


        name_tv.setText(getDataFromSharedPrefernces("firstName")+" "+getDataFromSharedPrefernces("lastName"));
        domain_tv.setText(getDataFromSharedPrefernces("applyingFor"));
        email_tv.setText(getDataFromSharedPrefernces("email"));
        phoneNumebr_tv.setText(getDataFromSharedPrefernces("phoneNumber"));
        dob_tv.setText(getDataFromSharedPrefernces("dob"));
        city_tv.setText(getDataFromSharedPrefernces("city"));
        pincode_tv.setText(getDataFromSharedPrefernces("pincode"));
        state_tv.setText(getDataFromSharedPrefernces("state"));
        country_tv.setText(getDataFromSharedPrefernces("countryName"));
        refPerson_tv.setText(getDataFromSharedPrefernces("refPerson"));
        refPersonPhone_tv.setText(getDataFromSharedPrefernces("refPersonPhone"));
        org_tv.setText(getDataFromSharedPrefernces("org"));
        education_tv.setText(getDataFromSharedPrefernces("education"));
        commitments_tv.setText(getDataFromSharedPrefernces("commitments"));
        roj_tv.setText(getDataFromSharedPrefernces("roj"));
        isAcaInternship_tv.setText(getDataFromSharedPrefernces("isAcaInternship"));
        startDate_tv.setText(getDataFromSharedPrefernces("startDate"));
        endDate_tv.setText(getDataFromSharedPrefernces("endDate"));
        applyingFor_tv.setText(getDataFromSharedPrefernces("applyingFor"));
        lastLogin_tv.setText(getDataFromSharedPrefernces("lastLogin"));
        dataTimestamp_tv.setText(getDataFromSharedPrefernces("dataTimestamp"));


        return view;
    }

    public String getDataFromSharedPrefernces(String key) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserDetails", MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }
}