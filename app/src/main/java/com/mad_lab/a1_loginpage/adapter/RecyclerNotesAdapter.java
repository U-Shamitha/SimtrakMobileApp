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
import com.mad_lab.a1_loginpage.model.NotesModel;
import com.mad_lab.a1_loginpage.model.TaskDetailsModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecyclerNotesAdapter extends RecyclerView.Adapter<RecyclerNotesAdapter.ViewHolder> {

    Context context;
    ArrayList<NotesModel> arrNotes;

    public RecyclerNotesAdapter(Context context, ArrayList<NotesModel> arrNotes){
        this.context = context;
        this.arrNotes = arrNotes;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.notes_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.id_tv.setText(arrNotes.get(position).id);
        holder.givenOn_tv.setText(arrNotes.get(position).givenOn);
        holder.givenBy_tv.setText(arrNotes.get(position).givenBy);
        holder.timeTaken_tv.setText(arrNotes.get(position).timeTaken);
        holder.note_tv.setText(arrNotes.get(position).note);


        if(!arrNotes.get(position).note.equals("") && !arrNotes.get(position).note.equals("null")){
            holder.note_ll.setVisibility(View.VISIBLE);
        }else{
            holder.note_ll.setVisibility(View.GONE);
        }


    }


    @Override
    public int getItemCount() {
        return arrNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView id_tv, note_tv, givenOn_tv, givenBy_tv, timeTaken_tv;

        Button noteMenu_btn;
        LinearLayout note_ll;

        boolean noteExpanded = false;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id_tv = itemView.findViewById(R.id.id_tv);
            givenOn_tv = itemView.findViewById(R.id.givenOn_tv);
            givenBy_tv = itemView.findViewById(R.id.givenBy_tv);
            timeTaken_tv = itemView.findViewById(R.id.timeTaken_tv);
            note_tv = itemView.findViewById(R.id.note_tv);
            note_ll = itemView.findViewById(R.id.note_ll);
            noteMenu_btn = itemView.findViewById(R.id.menu);

            noteMenu_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu noteCardOptionsPopupMenu = new PopupMenu(context, view);
                    noteCardOptionsPopupMenu.inflate(R.menu.notes_card_options);

                    noteCardOptionsPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.editNote:
                                    Map<String, Object> mapData = new HashMap<>();
                                    mapData.put("NoteId", id_tv.getText());
                                    mapData.put("GivenOn", givenOn_tv.getText());
                                    mapData.put("GivenBY", givenBy_tv.getText());
                                    mapData.put("TimeTaken", timeTaken_tv.getText());
                                    mapData.put("Note", note_tv.getText());
                                    storeDataInSharedPrefernces("selectedNoteDetails", mapData);
                                    Log.d("EditNoteData", mapData.toString());
                                    Intent intent= new Intent(context.getApplicationContext(), DashboardActivity.class);
                                    intent.putExtra("DesFragment","EditNote");
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

            note_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    noteExpanded = !noteExpanded;
                    if(noteExpanded){
//                        Toast.makeText(context, "expanding", Toast.LENGTH_LONG).show();
                        note_ll.setOrientation(LinearLayout.VERTICAL);
                        ViewGroup.LayoutParams params = note_ll.getLayoutParams();
                        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        note_ll.setLayoutParams(params);
                    }else{
//                        Toast.makeText(context, "collapsing", Toast.LENGTH_LONG).show();
                        note_ll.setOrientation(LinearLayout.HORIZONTAL);
                        ViewGroup.LayoutParams params = note_ll.getLayoutParams();
                        params.height = 60;
                        note_ll.setLayoutParams(params);
                    }
                }
            });

        }
    }

    void storeDataInSharedPrefernces(String key, Map<String,Object> value) {
        String jsonString = new Gson().toJson(value);

        SharedPreferences sharedPreferences = context.getSharedPreferences("EditNoteDetails", MODE_PRIVATE);
        SharedPreferences.Editor  editor= sharedPreferences.edit();
        editor.putString(key, jsonString);
        editor.apply();
    }

}
