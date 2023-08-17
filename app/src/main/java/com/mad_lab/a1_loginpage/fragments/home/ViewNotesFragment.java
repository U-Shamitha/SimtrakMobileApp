package com.mad_lab.a1_loginpage.fragments.home;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class ViewNotesFragment extends Fragment {

    SearchView searchNotes;
    ImageButton sort_ib;
    LinearLayout search_ll;
    ImageView search_iv;
    ImageButton addNote_btn;
    RecyclerView notesRecyclerView;
    RecyclerNotesAdapter notesArrAdapter ;
    ArrayList<NotesModel> notesArrayList;

    FirebaseFirestore fstore;
    String TAG = "ViewNotesFragment";
    boolean desSort = false;
    String searchNote_txt="";
    String searchNote_ctxt="";
    String taskId, taskName, givenOn, givenBy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_viewnotes, container, false);

        searchNotes = view.findViewById(R.id.search_notes);
        sort_ib = view.findViewById(R.id.sort_ib);
        search_ll = view.findViewById(R.id.search_ll);
        search_iv = view.findViewById(R.id.search_iv);
        addNote_btn = view.findViewById(R.id.add_notes_bt);
        notesRecyclerView = view.findViewById(R.id.notes_recyclerView);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notesArrayList = new ArrayList<>();

        fstore = FirebaseFirestore.getInstance();


        //Notes sort start
        sort_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenuForSortNotes(view);
            }
        });

        //Notes sort end

        //Search bar
        searchNotes.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    search_ll.setBackgroundResource(R.drawable.round_border_primary_color);
                    search_iv.setImageResource(R.drawable.ic_baseline_search_24_primary);
                }else{
                    search_ll.setBackgroundResource(R.drawable.round_border);
                    search_iv.setImageResource(R.drawable.ic_baseline_search_24);
                }
            }
        });

        searchNotes.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchNote_txt = query;
                Toast.makeText(getContext(),"query: "+query, Toast.LENGTH_SHORT).show();
                getSearchNotesDataFromFireStore();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchNote_ctxt = newText;
//                Toast.makeText(getContext(),"newText: "+newText, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        search_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchNote_txt = searchNote_ctxt;
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                getSearchNotesDataFromFireStore();
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


        getTaskDataFromSharedPrefernces("selectedTaskDetails");
        getSearchNotesDataFromFireStore();

        return view;
    }

    public void getSearchNotesDataFromFireStore(){

        notesArrayList.clear();

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
                                        ArrayList<Map<String, Object>> notes = (ArrayList<Map<String, Object>>) task.get("notes")!=null ? (ArrayList<Map<String, Object>>) task.get("notes") : new ArrayList<>();
                                        Log.d("notes", notes + "");

                                        //add notes to the recycler view
                                        if (notes != null) {
                                            if(searchNote_txt.isEmpty() || searchNote_txt.equalsIgnoreCase("all")){
                                                for (Map<String, Object> note : notes) {
    //
                                                    notesArrayList.add(new NotesModel(taskName, note.get("noteId") + "", note.get("note") + "", givenOn + "", givenBy + "", note.get("timeTaken") + ""));
                                                    Log.d(TAG, task.toString());
    //
                                                }
                                            }
                                            else{
                                                for (Map<String, Object> note : notes) {

                                                    if(
                                                        (note.get("noteId")+"").contains(searchNote_txt) ||
                                                        (note.get("note")+"").contains(searchNote_txt) ||
                                                        (note.get("noteId")+"").contains(searchNote_txt) ||
                                                        (note.get("timeTaken")+"").contains(searchNote_txt)
                                                    ) {
                                                        notesArrayList.add(new NotesModel(taskName, note.get("noteId") + "", note.get("note") + "", givenOn + "", givenBy + "", note.get("timeTaken") + ""));
                                                        Log.d(TAG, task.toString());
                                                    }

                                                }
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
            taskName = mapData.get("TaskName").toString();
            taskId = mapData.get("TaskId").toString();
            givenOn = mapData.get("AssignedDate").toString();
            givenBy = mapData.get("AssignedBy").toString();
        }

    }

    private void showPopupMenuForSortNotes(View v) {
        PopupMenu sortTaskPopupMenu = new PopupMenu(getContext(), v);
        sortTaskPopupMenu.inflate(R.menu.note_sort_menu);

        MenuItem checkboxItem = sortTaskPopupMenu.getMenu().findItem(R.id.revSort_cb);

        checkboxItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                checkboxItem.setChecked(!checkboxItem.isChecked());
                desSort = !desSort;

                // Keep the popup menu open
                checkboxItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
                checkboxItem.setActionView(new View(getContext()));
                MenuItemCompat.setOnActionExpandListener(checkboxItem, new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return false;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        return false;
                    }
                });

                return false;
            }

        });

        sortTaskPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.noteId_sort:
                        Toast.makeText(getContext(), "noteId sort", Toast.LENGTH_SHORT).show();
                        Collections.sort(notesArrayList, new Comparator<NotesModel>() {
                            @Override
                            public int compare(NotesModel note1, NotesModel note2) {
                                return note1.id.compareTo(note2.id);
                            }
                        });
                        if(desSort){
                            Collections.reverse(notesArrayList);
                            desSort=false;
                        }
                        notesArrAdapter.notifyDataSetChanged();
                        return true;
                    case R.id.timeTaken_sort:
                        Toast.makeText(getContext(), "time taken sort", Toast.LENGTH_SHORT).show();
                        Collections.sort(notesArrayList, new Comparator<NotesModel>() {
                            @Override
                            public int compare(NotesModel note1, NotesModel note2) {
                                return note1.timeTaken.compareTo(note2.timeTaken);
                            }
                        });
                        if(desSort){
                            Collections.reverse(notesArrayList);
                            desSort=false;
                        }
                        notesArrAdapter.notifyDataSetChanged();
                        return true;
                    case R.id.note_sort:
                        Toast.makeText(getContext(), "note sort", Toast.LENGTH_SHORT).show();
                        Collections.sort(notesArrayList, new Comparator<NotesModel>() {
                            @Override
                            public int compare(NotesModel note1, NotesModel note2) {
                                return note1.note.compareTo(note2.note);
                            }
                        });
                        if(desSort){
                            Collections.reverse(notesArrayList);
                            desSort=false;
                        }
                        notesArrAdapter.notifyDataSetChanged();
                        return true;

                    default:
                        return false;
                }
            }
        });

        sortTaskPopupMenu.show();
    }
}