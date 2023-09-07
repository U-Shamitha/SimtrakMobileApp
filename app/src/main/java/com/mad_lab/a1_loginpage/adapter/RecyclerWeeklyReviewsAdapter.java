package com.mad_lab.a1_loginpage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mad_lab.a1_loginpage.R;
import com.mad_lab.a1_loginpage.model.FinalReportModel;
import com.mad_lab.a1_loginpage.model.WeeklyReviewModel;

import java.util.ArrayList;

public class RecyclerWeeklyReviewsAdapter extends RecyclerView.Adapter<RecyclerWeeklyReviewsAdapter.ViewHolder> {

    Context context;
    ArrayList<WeeklyReviewModel> arrWeeklyReviews;

    public RecyclerWeeklyReviewsAdapter(Context context, ArrayList<WeeklyReviewModel> arrWeeklyReviews){
        this.context = context;
        this.arrWeeklyReviews = arrWeeklyReviews;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.card_weekly_review, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        WeeklyReviewModel weeklyReview = arrWeeklyReviews.get(position);

        holder.cardHeading_tv.setText("Week "+(position+1));
        holder.weeklyReviewId_tv.setText(weeklyReview.weeklyReviewId);
        holder.status_tv.setText(weeklyReview.status+"  ");
        holder.week_tv.setText(weeklyReview.week);
        holder.remarks_tv.setText(weeklyReview.remarks);
        holder.stamp_tv.setText(weeklyReview.stamp);

        if(arrWeeklyReviews.get(position).status.equalsIgnoreCase("approved")){
            holder.status_iv.setImageResource(R.drawable.baseline_check_24);
            holder.status_tv.setTextColor(context.getResources().getColor(R.color.light_green));
        }
        else{
            holder.status_iv.setImageResource(R.drawable.process);
            holder.status_tv.setTextColor(context.getResources().getColor(R.color.in_process_color));
        }

    }


    @Override
    public int getItemCount() {
        return arrWeeklyReviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout week_ll, remarks_ll;
        TextView cardHeading_tv, weeklyReviewId_tv, status_tv, week_tv, remarks_tv, stamp_tv;
        ImageView status_iv;

        boolean weekExpanded = false, remarksExpanded = false;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            week_ll = itemView.findViewById(R.id.week_ll);
            remarks_ll = itemView.findViewById(R.id.remarks_ll);
            cardHeading_tv = itemView.findViewById(R.id.cardHeading_tv);
            weeklyReviewId_tv = itemView.findViewById(R.id.weeklyReviewId_tv);
            status_tv = itemView.findViewById(R.id.status_tv);
            status_iv = itemView.findViewById(R.id.status_iv);
            week_tv = itemView.findViewById(R.id.week_tv);
            remarks_tv = itemView.findViewById(R.id.remarks_tv);
            stamp_tv = itemView.findViewById(R.id.stamp_tv);


            week_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  weekExpanded = !weekExpanded;
                    if(weekExpanded){
//                        Toast.makeText(context, "expanding", Toast.LENGTH_LONG).show();
                        week_ll.setOrientation(LinearLayout.VERTICAL);
                        ViewGroup.LayoutParams params = week_ll.getLayoutParams();
                        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        week_ll.setLayoutParams(params);
                    }else{
//                        Toast.makeText(context, "collapsing", Toast.LENGTH_LONG).show();
                        week_ll.setOrientation(LinearLayout.HORIZONTAL);
                        ViewGroup.LayoutParams params = week_ll.getLayoutParams();
                        params.height = 60;
                        week_ll.setLayoutParams(params);
                    }
                }
            });

            remarks_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    remarksExpanded = !remarksExpanded;
                    if(remarksExpanded){
//                        Toast.makeText(context, "expanding", Toast.LENGTH_LONG).show();
                        remarks_ll.setOrientation(LinearLayout.VERTICAL);
                        ViewGroup.LayoutParams params = remarks_ll.getLayoutParams();
                        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        remarks_ll.setLayoutParams(params);
                    }else{
//                        Toast.makeText(context, "collapsing", Toast.LENGTH_LONG).show();
                        remarks_ll.setOrientation(LinearLayout.HORIZONTAL);
                        ViewGroup.LayoutParams params = remarks_ll.getLayoutParams();
                        params.height = 60;
                        remarks_ll.setLayoutParams(params);
                    }
                }
            });

        }
    }
}
