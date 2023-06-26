package com.mad_lab.a1_loginpage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mad_lab.a1_loginpage.R;
import com.mad_lab.a1_loginpage.model.TaskDetailsModel;

import java.util.ArrayList;

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
        holder.assignedDate_tv.setText(arrTaskDetails.get(position).assignedDate);
    }


    @Override
    public int getItemCount() {
        return arrTaskDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView id_tv, name_tv, assignedDate_tv, deadline_tv, assignedBy_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id_tv = itemView.findViewById(R.id.id_tv);
            name_tv = itemView.findViewById(R.id.name_tv);
            assignedDate_tv = itemView.findViewById(R.id.assignedDate_tv);
            deadline_tv = itemView.findViewById(R.id.deadline_tv);
            assignedBy_tv = itemView.findViewById(R.id.assignedBy_tv);
        }
    }
}
