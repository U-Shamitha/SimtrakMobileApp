package com.mad_lab.a1_loginpage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mad_lab.a1_loginpage.R;
import com.mad_lab.a1_loginpage.model.JournalModel;

import java.util.ArrayList;

public class RecyclerJournalAdapter extends RecyclerView.Adapter<RecyclerJournalAdapter.ViewHolder> {

    Context context;
    ArrayList<JournalModel> arrJournals;

    public RecyclerJournalAdapter(Context context, ArrayList<JournalModel> arrJournals){
        this.context = context;
        this.arrJournals = arrJournals;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.journal_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ratingofDay_rb.setRating(arrJournals.get(position).rating);
        holder.journalId_tv.setText(arrJournals.get(position).id);
        holder.date_tv.setText(arrJournals.get(position).date);
        holder.report_tv.setText(arrJournals.get(position).report);
        holder.learnings_tv.setText(arrJournals.get(position).learnings);
        holder.pendings_tv.setText(arrJournals.get(position).pendings);
        holder.adminRemarks.setText(arrJournals.get(position).adminRemarks);
        holder.leaderRemaerks.setText(arrJournals.get(position).leaderRemarks);

    }


    @Override
    public int getItemCount() {
        return arrJournals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        RatingBar ratingofDay_rb;
        TextView journalId_tv, date_tv, report_tv, learnings_tv, pendings_tv, adminRemarks, leaderRemaerks;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            ratingofDay_rb = itemView.findViewById(R.id.ratingOfDay);
            journalId_tv = itemView.findViewById(R.id.journalId_tv);
            date_tv = itemView.findViewById(R.id.date_tv);
            report_tv = itemView.findViewById(R.id.report_tv);
            learnings_tv = itemView.findViewById(R.id.learnings_tv);
            pendings_tv = itemView.findViewById(R.id.pendings_tv);
            adminRemarks = itemView.findViewById(R.id.adminRemarks_tv);
            leaderRemaerks = itemView.findViewById(R.id.learnings_tv);

        }
    }
}
