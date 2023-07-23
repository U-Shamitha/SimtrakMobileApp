package com.mad_lab.a1_loginpage.fragments.home;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.reflect.TypeToken;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.mad_lab.a1_loginpage.R;
import com.mad_lab.a1_loginpage.activity.DashboardActivity;
import com.mad_lab.a1_loginpage.adapter.RecyclerJournalAdapter;
import com.mad_lab.a1_loginpage.adapter.RecyclerNotesAdapter;
import com.mad_lab.a1_loginpage.adapter.RecyclerTaskDetailsAdapter;
import com.mad_lab.a1_loginpage.model.JournalModel;
import com.mad_lab.a1_loginpage.model.NotesModel;
import com.mad_lab.a1_loginpage.model.TaskDetailsModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

public class ViewNotesFragment extends Fragment {

    SearchView searchNotes;

    ImageButton addNote_btn;
    RecyclerView notesRecyclerView;
    RecyclerNotesAdapter notesArrAdapter ;
    ArrayList<NotesModel> notesArrayList;

    FirebaseFirestore fstore;
    String TAG = "ViewNotesFragment";

    String taskId, givenOn, givenBy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_viewnotes, container, false);

        searchNotes = view.findViewById(R.id.search_notes);
        addNote_btn = view.findViewById(R.id.add_notes_bt);
        notesRecyclerView = view.findViewById(R.id.notes_recyclerView);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notesArrayList = new ArrayList<>();

        fstore = FirebaseFirestore.getInstance();

        searchNotes.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                //                Toast.makeText(LoginActivity.this, "on click email", Toast.LENGTH_SHORT).show();
                if(b){
                    searchNotes.setBackgroundResource(R.drawable.round_border_primary_color);
                }else{
                    searchNotes.setBackgroundResource(R.drawable.round_border);
                }
//                searchTask.setBackgroundTintList(ColorStateList.valueOf(color));
            }
        });

        addNote_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getContext().getApplicationContext(), DashboardActivity.class);
                intent.putExtra("DesFragment","AddNote");
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });

//        notesArrAdapter = new RecyclerNotesAdapter(getContext(), notesArrayList);
//        notesRecyclerView.setAdapter(notesArrAdapter);

        getTaskDataFromSharedPrefernces("selectedTaskDetails");
        getNotesFromFirestore();

        return view;
    }

    public void getNotesFromFirestore(){
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
                                        ArrayList<Map<String, Object>> notes = (ArrayList<Map<String, Object>>) task.get("notes");
                                        Log.d("notes", notes + "");

                                        //add notes to the recycler view
                                        if (notes != null) {
                                            for (Map<String, Object> note : notes) {
//                                            if ((task.get("taskName") + "").contains(searchTask_txt)) {
                                                notesArrayList.add(new NotesModel(note.get("noteId") + "", note.get("note") + "", givenOn + "", givenBy + "", note.get("timeTaken") + ""));
                                                Log.d(TAG, task.toString());
//                                            }
                                            }
                                            notesArrAdapter = new RecyclerNotesAdapter(getContext(), notesArrayList);
                                            notesRecyclerView.setAdapter(notesArrAdapter);
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