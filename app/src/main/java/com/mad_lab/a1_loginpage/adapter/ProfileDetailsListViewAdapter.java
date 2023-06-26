package com.mad_lab.a1_loginpage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.mad_lab.a1_loginpage.model.ProfileDetailsModel;
import com.mad_lab.a1_loginpage.R;

import java.util.ArrayList;

public class ProfileDetailsListViewAdapter extends ArrayAdapter<ProfileDetailsModel> {

    ArrayList<ProfileDetailsModel> profileDetailsArr = new ArrayList<>();

    public ProfileDetailsListViewAdapter(Context context, ArrayList<ProfileDetailsModel> profileDetailsArr){
        super(context, R.layout.profile_details_list_item, profileDetailsArr);
        this.profileDetailsArr = profileDetailsArr;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {

        ProfileDetailsModel profileDetails = getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.profile_details_list_item, parent,false);
        }

        CardView det_card = view.findViewById(R.id.det_card);
        LinearLayout det_layout = view.findViewById(R.id.det_layout);
        HorizontalScrollView det_main_sv = view.findViewById(R.id.det_main_sv);
        TextView det_main = view.findViewById(R.id.det_main);
        TextView det_des = view.findViewById(R.id.det_des);
        TextView det_side_des = view.findViewById(R.id.det_side_des);
        ImageButton expandCollapse_btn = view.findViewById(R.id.expandCollapseBtn);

//        det_layout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        det_main.setText(profileDetailsArr.get(position).list_item_main);
        det_des.setText(profileDetailsArr.get(position).list_item_des);
        det_side_des.setText(profileDetailsArr.get(position).list_item_des);

        expandCollapse_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup.LayoutParams params = det_main_sv.getLayoutParams();

                if((det_des.getVisibility() == View.GONE)){
                    params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    det_side_des.setVisibility(View.GONE);
                    det_des.setVisibility(View.VISIBLE);
                    expandCollapse_btn.setImageResource(R.drawable.baseline_remove_24);
                }else{
                    params.width = 0;
                    det_des.setVisibility(View.GONE);
                    det_side_des.setVisibility(View.VISIBLE);
                    expandCollapse_btn.setImageResource(R.drawable.baseline_add_24);
                }

//                params.width = (det_des.getVisibility() == View.GONE) ? ViewGroup.LayoutParams.WRAP_CONTENT: 0;
//
//                int des_vis = (det_des.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
//                det_des.setVisibility(des_vis);
//                int side_des_vis = (det_side_des.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
////                TransitionManager.beginDelayedTransition(det_layout, new AutoTransition());
//                det_side_des.setVisibility(side_des_vis);
            }
        });

        det_card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                ViewGroup.LayoutParams params = det_main_sv.getLayoutParams();
                params.width = (det_des.getVisibility() == View.GONE) ? ViewGroup.LayoutParams.WRAP_CONTENT: 0;

                int des_vis = (det_des.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
                det_des.setVisibility(des_vis);
                int side_des_vis = (det_side_des.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
//                TransitionManager.beginDelayedTransition(det_layout, new AutoTransition());
                det_side_des.setVisibility(side_des_vis);

                return false;
            }
        });



        return view;
    }
}
