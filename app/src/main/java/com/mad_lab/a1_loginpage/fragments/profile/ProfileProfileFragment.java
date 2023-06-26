package com.mad_lab.a1_loginpage.fragments.profile;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mad_lab.a1_loginpage.adapter.ProfileDetailsListViewAdapter;
import com.mad_lab.a1_loginpage.model.ProfileDetailsModel;
import com.mad_lab.a1_loginpage.R;

import java.util.ArrayList;

public class ProfileProfileFragment extends Fragment {

    ListView profileDetails_listview;
    ArrayList<ProfileDetailsModel> profileDetailsArr = new ArrayList<>();
    CardView status_card;
    LinearLayout status_layout;
    TextView status_main;
    TextView status_des;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_profile, container, false);

        profileDetails_listview = view.findViewById(R.id.profileDetails_listview);
//        status_main = view.findViewById(R.id.status_main);
//        status_layout = view.findViewById(R.id.status_layout);
//        status_main = view.findViewById(R.id.status_main);
//        status_des = view.findViewById(R.id.status_des);

//        status_layout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

//        status_main.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                expand(view, status_des, status_layout);
//            }
//        });

        profileDetailsArr.add(new ProfileDetailsModel("Status","Active"));
        profileDetailsArr.add(new ProfileDetailsModel("Name","Active"));
        profileDetailsArr.add(new ProfileDetailsModel("City","Active"));
        profileDetailsArr.add(new ProfileDetailsModel("Email ID","Active"));
        profileDetailsArr.add(new ProfileDetailsModel("State","Active"));
        profileDetailsArr.add(new ProfileDetailsModel("PIN","Active"));
        profileDetailsArr.add(new ProfileDetailsModel("Reason of Joining","Active"));

        ProfileDetailsListViewAdapter profileDetailsArrAdapter = new ProfileDetailsListViewAdapter(getContext(), profileDetailsArr);
        profileDetails_listview.setAdapter(profileDetailsArrAdapter);



        return view;
    }

//    private void expand(View view, TextView des, LinearLayout layout) {
//
//        int v = (des.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
//
//        TransitionManager.beginDelayedTransition(layout, new AutoTransition());
//        des.setVisibility(v);
//    }
}