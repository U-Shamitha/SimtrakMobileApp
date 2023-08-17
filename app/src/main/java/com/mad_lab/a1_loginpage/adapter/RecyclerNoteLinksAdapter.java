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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mad_lab.a1_loginpage.R;
import com.mad_lab.a1_loginpage.activity.DashboardActivity;
import com.mad_lab.a1_loginpage.model.NoteLinksModel;
import com.mad_lab.a1_loginpage.model.NotesModel;
import com.mad_lab.a1_loginpage.model.TaskDetailsModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecyclerNoteLinksAdapter extends RecyclerView.Adapter<RecyclerNoteLinksAdapter.ViewHolder> {

    Context context;
    ArrayList<NoteLinksModel> arrNoteLinks;

    public RecyclerNoteLinksAdapter(Context context, ArrayList<NoteLinksModel> arrNoteLinks){
        this.context = context;
        this.arrNoteLinks = arrNoteLinks;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.note_link_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.taskName_tv.setText(arrNoteLinks.get(position).taskName);
        holder.id_tv.setText(arrNoteLinks.get(position).id);
        holder.givenOn_tv.setText(arrNoteLinks.get(position).givenOn);
        holder.givenBy_tv.setText(arrNoteLinks.get(position).givenBy);
        holder.noteLink_tv.setText(arrNoteLinks.get(position).noteLink);
        holder.linkType_tv.setText(arrNoteLinks.get(position).linkType);


        if(!arrNoteLinks.get(position).noteLink.equals("") && !arrNoteLinks.get(position).noteLink.equals("null")){
            holder.noteLink_ll.setVisibility(View.VISIBLE);
        }else{
            holder.noteLink_ll.setVisibility(View.GONE);
        }


    }


    @Override
    public int getItemCount() {
        return arrNoteLinks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView taskName_tv, id_tv, givenOn_tv, givenBy_tv, noteLink_tv, linkType_tv;

        Button noteLinkMenu_btn;
        LinearLayout noteLink_ll;

        boolean noteLinkExpanded = false;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName_tv = itemView.findViewById(R.id.name_tv);
            id_tv = itemView.findViewById(R.id.id_tv);
            givenOn_tv = itemView.findViewById(R.id.givenOn_tv);
            givenBy_tv = itemView.findViewById(R.id.givenBy_tv);
            noteLink_tv = itemView.findViewById(R.id.noteLink_tv);
            linkType_tv = itemView.findViewById(R.id.linkType_tv);
            noteLink_ll = itemView.findViewById(R.id.noteLink_ll);
            noteLinkMenu_btn = itemView.findViewById(R.id.menu);

            noteLinkMenu_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu noteCardOptionsPopupMenu = new PopupMenu(context, view);
                    noteCardOptionsPopupMenu.inflate(R.menu.note_link_card_options);

                    noteCardOptionsPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.editNoteLink:
                                    Map<String, Object> mapData = new HashMap<>();
                                    mapData.put("NoteLinkId", id_tv.getText());
                                    mapData.put("GivenOn", givenOn_tv.getText());
                                    mapData.put("GivenBY", givenBy_tv.getText());
                                    mapData.put("NoteLink", noteLink_tv.getText());
                                    mapData.put("NoteLinkType", linkType_tv.getText());
                                    storeDataInSharedPrefernces("selectedNoteLinkDetails", mapData);
                                    Log.d("EditNoteLinkData", mapData.toString());
                                    Intent intent= new Intent(context.getApplicationContext(), DashboardActivity.class);
                                    intent.putExtra("DesFragment","EditNoteLink");
                                    context.startActivity(intent);
                                    ((Activity)context).finish();
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });

                    noteCardOptionsPopupMenu.show();
                }
            });

            noteLink_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    noteLinkExpanded = !noteLinkExpanded;
                    if(noteLinkExpanded){
//                        Toast.makeText(context, "expanding", Toast.LENGTH_LONG).show();
                        noteLink_ll.setOrientation(LinearLayout.VERTICAL);
                        ViewGroup.LayoutParams params = noteLink_ll.getLayoutParams();
                        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        noteLink_ll.setLayoutParams(params);
                    }else{
//                        Toast.makeText(context, "collapsing", Toast.LENGTH_LONG).show();
                        noteLink_ll.setOrientation(LinearLayout.HORIZONTAL);
                        ViewGroup.LayoutParams params = noteLink_ll.getLayoutParams();
                        params.height = 60;
                        noteLink_ll.setLayoutParams(params);
                    }
                }
            });

        }
    }

    void storeDataInSharedPrefernces(String key, Map<String,Object> value) {
        String jsonString = new Gson().toJson(value);

        SharedPreferences sharedPreferences = context.getSharedPreferences("EditNoteLinkDetails", MODE_PRIVATE);
        SharedPreferences.Editor  editor= sharedPreferences.edit();
        editor.putString(key, jsonString);
//        editor.apply();
        editor.commit();
    }

}
