package com.mad_lab.a1_loginpage.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.play.core.integrity.p;
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
        holder.status_tv.setText(arrTaskDetails.get(position).status);
        holder.assignedDate_tv.setText(arrTaskDetails.get(position).assignedDate);
        holder.name_tv.setText(arrTaskDetails.get(position).name);
        holder.deadline_tv.setText(arrTaskDetails.get(position).deadline);
        holder.taskPriority_tv.setText(arrTaskDetails.get(position).priority);
        holder.taskType_tv.setText(arrTaskDetails.get(position).type);
        holder.assignedBy_tv.setText(arrTaskDetails.get(position).assignedBy);
        holder.description_tv.setText(arrTaskDetails.get(position).description);

        if(!arrTaskDetails.get(position).deadline.equals("") && !arrTaskDetails.get(position).deadline.equals("null")){
            holder.deadline_ll.setVisibility(View.VISIBLE);
        }else{
            holder.deadline_ll.setVisibility(View.GONE);
        }

        if(!arrTaskDetails.get(position).description.equals("") && !arrTaskDetails.get(position).description.equals("null")){
            holder.description_ll.setVisibility(View.VISIBLE);
        }else{
            holder.description_ll.setVisibility(View.GONE);
        }


    }


    @Override
    public int getItemCount() {
        return arrTaskDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView id_tv, status_tv, name_tv, assignedDate_tv, deadline_tv, assignedBy_tv, taskCardMenu_btn, taskType_tv, taskPriority_tv, description_tv;
        LinearLayout deadline_ll, description_ll;

        boolean desExpanded = false;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id_tv = itemView.findViewById(R.id.id_tv);
            name_tv = itemView.findViewById(R.id.name_tv);
            status_tv = itemView.findViewById(R.id.status_tv);
            assignedDate_tv = itemView.findViewById(R.id.assignedDate_tv);
            deadline_tv = itemView.findViewById(R.id.deadline_tv);
            taskPriority_tv = itemView.findViewById(R.id.taskPriority_tv);
            taskType_tv = itemView.findViewById(R.id.taskType_tv);
            assignedBy_tv = itemView.findViewById(R.id.assignedBy_tv);
            taskCardMenu_btn = itemView.findViewById(R.id.menu);
            description_tv = itemView.findViewById(R.id.description_tv);

            deadline_ll = itemView.findViewById(R.id.deadline_ll);
            description_ll = itemView.findViewById(R.id.description_ll);


            taskCardMenu_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu taskCardOptionsPopupMenu = new PopupMenu(context, view);
                    taskCardOptionsPopupMenu.inflate(R.menu.task_card_options);

                    taskCardOptionsPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        Intent intent;
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Map<String, Object> mapData = new HashMap<>();
                            mapData.put("TaskId", id_tv.getText());
                            mapData.put("TaskName", name_tv.getText());
                            mapData.put("AssignedDate", assignedDate_tv.getText());
                            mapData.put("Deadline", deadline_tv.getText());
                            mapData.put("TaskPriority", taskPriority_tv.getText());
                            mapData.put("TaskType", taskType_tv.getText());
                            mapData.put("AssignedBy", assignedBy_tv.getText());
                            mapData.put("Description", description_tv.getText());
                            mapData.put("TaskStatus", status_tv.getText());
                            storeDataInSharedPrefernces("selectedTaskDetails", mapData);
                            Log.d("EditTaskData", mapData.toString());
                            switch (item.getItemId()) {
                                case R.id.taskNotes:
                                    intent= new Intent(context.getApplicationContext(), DashboardActivity.class);
                                    intent.putExtra("DesFragment","ViewNotes");
                                    context.startActivity(intent);
                                    return true;
                                case R.id.taskLinks:
                                    intent= new Intent(context.getApplicationContext(), DashboardActivity.class);
                                    intent.putExtra("DesFragment","ViewNoteLinks");
                                    context.startActivity(intent);
                                    return true;
                                case R.id.taskStatus:
                                    intent= new Intent(context.getApplicationContext(), DashboardActivity.class);
                                    intent.putExtra("DesFragment","EditTaskStatus");
                                    context.startActivity(intent);
                                    return true;
                                case R.id.editTask:
                                    intent= new Intent(context.getApplicationContext(), DashboardActivity.class);
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

            description_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    desExpanded = !desExpanded;
                    if(desExpanded){
//                        Toast.makeText(context, "expanding", Toast.LENGTH_LONG).show();
                        description_ll.setOrientation(LinearLayout.VERTICAL);
                        ViewGroup.LayoutParams params = description_ll.getLayoutParams();
                        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        description_ll.setLayoutParams(params);
                    }else{
//                        Toast.makeText(context, "collapsing", Toast.LENGTH_LONG).show();
                        description_ll.setOrientation(LinearLayout.HORIZONTAL);
                        ViewGroup.LayoutParams params = description_ll.getLayoutParams();
                        params.height = 60;
                        description_ll.setLayoutParams(params);
                    }
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
