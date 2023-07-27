package com.mad_lab.a1_loginpage.fragments.home;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.reflect.TypeToken;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.mad_lab.a1_loginpage.R;
import com.mad_lab.a1_loginpage.activity.DashboardActivity;
import com.mad_lab.a1_loginpage.adapter.RecyclerNoteLinksAdapter;
import com.mad_lab.a1_loginpage.model.NoteLinksModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

public class ViewNoteLinksFragment extends Fragment {

    SearchView searchNoteLinks;

    ImageButton addNoteLinks_btn;
    RecyclerView noteLinksRecyclerView;
    RecyclerNoteLinksAdapter noteLinksArrAdapter ;
    ArrayList<NoteLinksModel> noteLinksArrayList;

    FirebaseFirestore fstore;
    String TAG = "ViewLinksFragment";

    String taskId, givenOn, givenBy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_links, container, false);

        searchNoteLinks = view.findViewById(R.id.search_noteLinks);
        addNoteLinks_btn = view.findViewById(R.id.add_noteLinks_bt);
        noteLinksRecyclerView = view.findViewById(R.id.noteLinks_recyclerView);
        noteLinksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        noteLinksArrayList = new ArrayList<>();

        fstore = FirebaseFirestore.getInstance();

        searchNoteLinks.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                //                Toast.makeText(LoginActivity.this, "on click email", Toast.LENGTH_SHORT).show();
                if(b){
                    searchNoteLinks.setBackgroundResource(R.drawable.round_border_primary_color);
                }else{
                    searchNoteLinks.setBackgroundResource(R.drawable.round_border);
                }
//                searchTask.setBackgroundTintList(ColorStateList.valueOf(color));
            }
        });

        addNoteLinks_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getContext().getApplicationContext(), DashboardActivity.class);
                intent.putExtra("DesFragment","AddNoteLink");
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });

//        notesArrAdapter = new RecyclerNotesAdapter(getContext(), notesArrayList);
//        notesRecyclerView.setAdapter(notesArrAdapter);

        getTaskDataFromSharedPrefernces("selectedTaskDetails");
        getNoteLinksFromFirestore();

        return view;
    }

    public void getNoteLinksFromFirestore(){
        DocumentReference documentReference = fstore.collection("users").document(getUserIdFromSharedPrefernces("userId"));
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            ArrayList<Map<String, Object>> tasks= (ArrayList<Map<String, Object>>) documentSnapshot.get("tasks")!=null ? (ArrayList<Map<String, Object>>) documentSnapshot.get("tasks") : new ArrayList<>();
                            String dateFormat = "dd-MM-yyyy";
                            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

                            if (tasks != null) {
                                for (Map<String, Object> task : tasks) {
                                    Log.d("all tasks", task.toString());
                                    if (taskId.equals(task.get("taskId"))) {
                                        ArrayList<Map<String, Object>> noteLinks = (ArrayList<Map<String, Object>>) task.get("noteLinks");
                                        Log.d("noteLinks", noteLinks + "");

                                        //add notes to the recycler view
                                        if (noteLinks != null) {
                                            for (Map<String, Object> noteLink : noteLinks) {
//                                            if ((task.get("taskName") + "").contains(searchTask_txt)) {
                                                noteLinksArrayList.add(new NoteLinksModel(noteLink.get("noteLinkId") + "",  givenOn + "", givenBy + "",noteLink.get("noteLink") + "", noteLink.get("noteLinkType") + ""));
//                                            }
                                            }
                                            noteLinksArrAdapter = new RecyclerNoteLinksAdapter(getContext(), noteLinksArrayList);
                                            noteLinksRecyclerView.setAdapter(noteLinksArrAdapter);
                                            break; // Assuming there is only one matching item
                                        }
                                    }
                                }
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "user doc not found"+e.getMessage());
                    }
                });
    }

    public String getUserIdFromSharedPrefernces(String key) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserDetails", MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

    public void getTaskDataFromSharedPrefernces(String key) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("EditTaskDetails", MODE_PRIVATE);
        String jsonString = sharedPreferences.getString(key,"");
        Log.d("DataRetInEditTask",jsonString);
        if(jsonString!= null) {
            Map<String, Object> mapData = new Gson().fromJson(jsonString, new TypeToken<Map<String, Object>>() {
            }.getType());

            Toast.makeText(getContext(), "taskId: "+mapData.get("TaskId").toString(), Toast.LENGTH_LONG).show();
            taskId = mapData.get("TaskId").toString();
            givenOn = mapData.get("AssignedDate").toString();
            givenBy = mapData.get("AssignedBy").toString();
        }

    }
}