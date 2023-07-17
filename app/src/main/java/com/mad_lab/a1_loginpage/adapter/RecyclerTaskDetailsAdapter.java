package com.mad_lab.a1_loginpage.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mad_lab.a1_loginpage.R;
import com.mad_lab.a1_loginpage.activity.DashboardActivity;
import com.mad_lab.a1_loginpage.model.TaskDetailsModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerTaskDetailsAdapter extends RecyclerView.Adapter<RecyclerTaskDetailsAdapter.ViewHolder> {

    Context context;
    ArrayList<TaskDetailsModel> arrTaskDetails;

    public RecyclerTaskDetailsAdapter(Context context, ArrayList<TaskDetailsModel> arrTaskDetails){
        this.context = context;
        this.arrTaskDetails = arrTaskDetails;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.task_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.id_tv.setText(arrTaskDetails.get(position).id);
        holder.assignedDate_tv.setText(arrTaskDetails.get(position).assignedDate);
        holder.name_tv.setText(arrTaskDetails.get(position).name);
        holder.taskPriority_tv.setText(arrTaskDetails.get(position).priority);
        holder.taskType_tv.setText(arrTaskDetails.get(position).type);
        holder.assignedBy_tv.setText(arrTaskDetails.get(position).assignedBy);
    }


    @Override
    public int getItemCount() {
        return arrTaskDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView id_tv, name_tv, assignedDate_tv, deadline_tv, assignedBy_tv, taskCardMenu_btn, taskType_tv, taskPriority_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id_tv = itemView.findViewById(R.id.id_tv);
            name_tv = itemView.findViewById(R.id.name_tv);
            assignedDate_tv = itemView.findViewById(R.id.assignedDate_tv);
            deadline_tv = itemView.findViewById(R.id.deadline_tv);
            taskPriority_tv = itemView.findViewById(R.id.taskPriority_tv);
            taskType_tv = itemView.findViewById(R.id.taskType_tv);
            assignedBy_tv = itemView.findViewById(R.id.assignedBy_tv);
            taskCardMenu_btn = itemView.findViewById(R.id.menu);

            taskCardMenu_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu taskCardOptionsPopupMenu = new PopupMenu(context, view);
                    taskCardOptionsPopupMenu.inflate(R.menu.task_card_options);

                    taskCardOptionsPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.taskNotes:
                                    return true;
                                case R.id.taskLinks:
                                    return true;
                                case R.id.taskStatus:
                                    return true;
                                case R.id.editTask:
                                    Map<String, Object> mapData = new HashMap<>();
                                    mapData.put("TaskName", name_tv.getText());
                                    mapData.put("AssignedDate", assignedDate_tv.getText());
                                    mapData.put("TaskPriority", taskPriority_tv.getText());
                                    mapData.put("TaskType", taskType_tv.getText());
                                    mapData.put("AssignedBy", assignedBy_tv.getText());
                                    storeDataInSharedPrefernces("selectedTaskDetails", mapData);
                                    Log.d("EditTaskData", mapData.toString());
                                    Intent intent= new Intent(context.getApplicationContext(), DashboardActivity.class);
                                    intent.putExtra("DesFragment","EditTask");
                                    context.startActivity(intent);
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });

                    taskCardOptionsPopupMenu.show();
                }
            });
        }
    }

    void storeDataInSharedPrefernces(String key, Map<String,Object> value) {
        String jsonString = new Gson().toJson(value);

        SharedPreferences sharedPreferences = context.getSharedPreferences("EditTaskDetails", MODE_PRIVATE);
        SharedPreferences.Editor  editor= sharedPreferences.edit();
        editor.putString(key, jsonString);
        editor.apply();
    }

}
