package com.mad_lab.a1_loginpage.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mad_lab.a1_loginpage.R;
import com.mad_lab.a1_loginpage.inventory.Priority;

import java.util.List;

public class TaskPrioritySpinnerAdapter extends BaseAdapter {
    private Context context;
    private List<Priority> priorityList;
    private LayoutInflater mInflater = null;


    public TaskPrioritySpinnerAdapter(Context context, List<Priority> priorityList){
        this.context = context;
        this.priorityList = priorityList;

    }

//    public TaskPrioritySpinnerAdapter(@NonNull Context context, @NonNull List<Priority> priorityList) {
//        super(context, 0, priorityList);
//        this.context = context;
//        this.priorityList = priorityList;
//    }


    @Override
    public int getCount() {
        return priorityList != null ? priorityList.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean isEnabled(int position) {
        if (position == 0){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rootView;

        if(i==0){
            rootView = LayoutInflater.from(context)
                    .inflate(R.layout.priority_item, viewGroup, false);

            TextView txtName = rootView.findViewById(R.id.name);
            txtName.setText(priorityList.get(i).getName());
            txtName.setTextColor(ColorStateList.valueOf(context.getResources().getColor(R.color.dark_grey)));
        }

        else {
            rootView = LayoutInflater.from(context)
                    .inflate(R.layout.priority_item, viewGroup, false);

            TextView txtName = rootView.findViewById(R.id.name);
            ImageView image = rootView.findViewById(R.id.image);

            txtName.setText(priorityList.get(i).getName());
            txtName.setTextColor(Color.BLACK);
            image.setImageResource(priorityList.get(i).getImage());

            if (i == 1) {
                image.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.dark_red)));
            } else if (i == 2) {
                image.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.dark_orange)));
            } else if (i == 3) {
                image.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.dark_green)));
            }
        }

        return rootView;
    }
}
