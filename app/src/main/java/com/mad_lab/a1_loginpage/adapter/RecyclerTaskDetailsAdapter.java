package com.mad_lab.a1_loginpage.adapter;

import android.content.Context;
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

import com.mad_lab.a1_loginpage.R;
import com.mad_lab.a1_loginpage.model.TaskDetailsModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
        holder.id_tv.setText(arrTaskDetails.get(position).name);
        holder.assignedDate_tv.setText(arrTaskDetails.get(position).assignedDate);
        holder.name_tv.setText(arrTaskDetails.get(position).name);
        holder.deadline_tv.setText(arrTaskDetails.get(position).deadline);
        holder.assignedBy_tv.setText(arrTaskDetails.get(position).assignedBy);

        //set task card options pop up menu start
        holder.taskCardMenu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenuForTaskCardOptions(view);
            }
        });
        //set task card options pop up menu end
    }


    @Override
    public int getItemCount() {
        return arrTaskDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView id_tv, name_tv, assignedDate_tv, deadline_tv, assignedBy_tv, taskCardMenu_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id_tv = itemView.findViewById(R.id.id_tv);
            name_tv = itemView.findViewById(R.id.name_tv);
            assignedDate_tv = itemView.findViewById(R.id.assignedDate_tv);
            deadline_tv = itemView.findViewById(R.id.deadline_tv);
            assignedBy_tv = itemView.findViewById(R.id.assignedBy_tv);
            taskCardMenu_btn = itemView.findViewById(R.id.menu);
        }
    }

    private void showPopupMenuForTaskCardOptions(View v) {
        PopupMenu taskCardOptionsPopupMenu = new PopupMenu(context, v);
        taskCardOptionsPopupMenu.inflate(R.menu.task_card_options);

        taskCardOptionsPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.taskNotes:
                        return true;
                    case R.id.taskLinks:
                        return true;
                    case R.id.taskStatus:
                        return true;
                    case R.id.editTask:

                        return true;
                    default:
                        return false;
                }
            }
        });

        taskCardOptionsPopupMenu.show();
    }
}
