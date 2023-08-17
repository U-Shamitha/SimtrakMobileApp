package com.mad_lab.a1_loginpage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mad_lab.a1_loginpage.R;
import com.mad_lab.a1_loginpage.model.JournalModel;
import com.mad_lab.a1_loginpage.model.LeavesModel;

import java.util.ArrayList;

public class RecyclerLeavesAdapter extends RecyclerView.Adapter<RecyclerLeavesAdapter.ViewHolder> {

    Context context;
    ArrayList<LeavesModel> arrLeaves;

    public RecyclerLeavesAdapter(Context context, ArrayList<LeavesModel> arrLeaves){
        this.context = context;
        this.arrLeaves = arrLeaves;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.leave_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        LeavesModel leave = arrLeaves.get(position);

        holder.cardHeading_tv.setText("Leave");
        holder.leaveId_tv.setText(leave.id);
        holder.status_tv.setText(leave.status+"  ");
        holder.startDate_tv.setText(leave.startDate);
        holder.joiningDate_tv.setText(leave.joiningDate);
        holder.reason_tv.setText(leave.reasonForLeave);
        holder.adminRemarks_tv.setText(leave.adminRemarks);
        holder.leaderRemarks_tv.setText(leave.leaderRemarks);

        if(arrLeaves.get(position).status.equalsIgnoreCase("approved")){
            holder.status_iv.setImageResource(R.drawable.baseline_check_24);
            holder.status_tv.setTextColor(context.getResources().getColor(R.color.light_green));
        }

        if(!leave.reasonForLeave.equals("") && !leave.reasonForLeave.equals("null")){
            holder.reason_ll.setVisibility(View.VISIBLE);
        }else{
            holder.reason_ll.setVisibility(View.GONE);
        }

        if(!leave.adminRemarks.equals("") && !leave.adminRemarks.equals("null")){
            holder.adminRemarks_ll.setVisibility(View.VISIBLE);
        }else{
            holder.adminRemarks_ll.setVisibility(View.GONE);
        }

        if(!leave.leaderRemarks.equals("") && !leave.leaderRemarks.equals("null")){
            holder.leaderRemarks_ll.setVisibility(View.VISIBLE);
        }else{
            holder.leaderRemarks_ll.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        return arrLeaves.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout reason_ll, adminRemarks_ll, leaderRemarks_ll;
        TextView cardHeading_tv, leaveId_tv, status_tv, startDate_tv, joiningDate_tv, reason_tv, adminRemarks_tv, leaderRemarks_tv;
        ImageView status_iv;

        boolean reasonExpanded = false, adminRemarksExpanded = false, leaderRemarksExpanded = false;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            reason_ll = itemView.findViewById(R.id.reason_ll);
            adminRemarks_ll = itemView.findViewById(R.id.adminRemarks_ll);
            leaderRemarks_ll = itemView.findViewById(R.id.leaderRemarks_ll);
            cardHeading_tv = itemView.findViewById(R.id.cardHeading_tv);
            leaveId_tv = itemView.findViewById(R.id.leaveId_tv);
            status_tv = itemView.findViewById(R.id.status_tv);
            status_iv = itemView.findViewById(R.id.status_iv);
            startDate_tv = itemView.findViewById(R.id.startDate_tv);
            joiningDate_tv = itemView.findViewById(R.id.joiningDate_tv);
            reason_tv = itemView.findViewById(R.id.reason_tv);
            adminRemarks_tv = itemView.findViewById(R.id.adminRemarks_tv);
            leaderRemarks_tv = itemView.findViewById(R.id.leaderRemarks_tv);


            reason_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reasonExpanded = !reasonExpanded;
                    if(reasonExpanded){
//                        Toast.makeText(context, "expanding", Toast.LENGTH_LONG).show();
                        reason_ll.setOrientation(LinearLayout.VERTICAL);
                        ViewGroup.LayoutParams params = reason_ll.getLayoutParams();
                        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        reason_ll.setLayoutParams(params);
                    }else{
//                        Toast.makeText(context, "collapsing", Toast.LENGTH_LONG).show();
                        reason_ll.setOrientation(LinearLayout.HORIZONTAL);
                        ViewGroup.LayoutParams params = reason_ll.getLayoutParams();
                        params.height = 60;
                        reason_ll.setLayoutParams(params);
                    }
                }
            });

            adminRemarks_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adminRemarksExpanded = !adminRemarksExpanded;
                    if(adminRemarksExpanded){
//                        Toast.makeText(context, "expanding", Toast.LENGTH_LONG).show();
                        reason_ll.setOrientation(LinearLayout.VERTICAL);
                        ViewGroup.LayoutParams params = adminRemarks_ll.getLayoutParams();
                        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        adminRemarks_ll.setLayoutParams(params);
                    }else{
//                        Toast.makeText(context, "collapsing", Toast.LENGTH_LONG).show();
                        adminRemarks_ll.setOrientation(LinearLayout.HORIZONTAL);
                        ViewGroup.LayoutParams params = adminRemarks_ll.getLayoutParams();
                        params.height = 60;
                        adminRemarks_ll.setLayoutParams(params);
                    }
                }
            });

            leaderRemarks_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    leaderRemarksExpanded = !leaderRemarksExpanded;
                    if(leaderRemarksExpanded){
//                        Toast.makeText(context, "expanding", Toast.LENGTH_LONG).show();
                        leaderRemarks_ll.setOrientation(LinearLayout.VERTICAL);
                        ViewGroup.LayoutParams params = reason_ll.getLayoutParams();
                        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        leaderRemarks_ll.setLayoutParams(params);
                    }else{
//                        Toast.makeText(context, "collapsing", Toast.LENGTH_LONG).show();
                        leaderRemarks_ll.setOrientation(LinearLayout.HORIZONTAL);
                        ViewGroup.LayoutParams params = leaderRemarks_ll.getLayoutParams();
                        params.height = 60;
                        leaderRemarks_ll.setLayoutParams(params);
                    }
                }
            });


        }
    }
}
