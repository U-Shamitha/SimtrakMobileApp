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
import com.mad_lab.a1_loginpage.model.LeavesModel;

import java.util.ArrayList;

public class RecyclerFinalReportsAdapter extends RecyclerView.Adapter<RecyclerFinalReportsAdapter.ViewHolder> {

    Context context;
    ArrayList<FinalReportModel> arrFinalReports;

    public RecyclerFinalReportsAdapter(Context context, ArrayList<FinalReportModel> arrFinalReports){
        this.context = context;
        this.arrFinalReports = arrFinalReports;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.finalreport_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        FinalReportModel finalReport = arrFinalReports.get(position);

        holder.cardHeading_tv.setText("Final Report");
        holder.reportId_tv.setText(finalReport.reportId);
        holder.status_tv.setText(finalReport.status+"  ");
        holder.fileName_tv.setText(finalReport.fileName);
        holder.uploadedOn_tv.setText(finalReport.uploadedOn);

        if(arrFinalReports.get(position).status.equalsIgnoreCase("approved")){
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
        return arrFinalReports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout finalName_ll;
        TextView cardHeading_tv, reportId_tv, status_tv, fileName_tv, uploadedOn_tv;
        ImageView status_iv;

        boolean finalNameExpanded = false;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            finalName_ll = itemView.findViewById(R.id.fileName_ll);
            cardHeading_tv = itemView.findViewById(R.id.cardHeading_tv);
            reportId_tv = itemView.findViewById(R.id.reportId_tv);
            status_tv = itemView.findViewById(R.id.status_tv);
            status_iv = itemView.findViewById(R.id.status_iv);
            fileName_tv = itemView.findViewById(R.id.fileName_tv);
            uploadedOn_tv = itemView.findViewById(R.id.uploadedOn_tv);


            finalName_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  finalNameExpanded = !finalNameExpanded;
                    if(finalNameExpanded){
//                        Toast.makeText(context, "expanding", Toast.LENGTH_LONG).show();
                        finalName_ll.setOrientation(LinearLayout.VERTICAL);
                        ViewGroup.LayoutParams params = finalName_ll.getLayoutParams();
                        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        finalName_ll.setLayoutParams(params);
                    }else{
//                        Toast.makeText(context, "collapsing", Toast.LENGTH_LONG).show();
                        finalName_ll.setOrientation(LinearLayout.HORIZONTAL);
                        ViewGroup.LayoutParams params = finalName_ll.getLayoutParams();
                        params.height = 60;
                        finalName_ll.setLayoutParams(params);
                    }
                }
            });

        }
    }
}
