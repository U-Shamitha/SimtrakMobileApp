package com.mad_lab.a1_loginpage.fragments.trainees;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mad_lab.a1_loginpage.R;
import com.mad_lab.a1_loginpage.adapter.RecyclerJournalAdapter;
import com.mad_lab.a1_loginpage.adapter.RecyclerTaskDetailsAdapter;
import com.mad_lab.a1_loginpage.model.JournalModel;
import com.mad_lab.a1_loginpage.model.TaskDetailsModel;

import java.util.ArrayList;

public class JournalFragment extends Fragment {

    SearchView searchJournal;

    RecyclerView journalRecyclerView;
    RecyclerJournalAdapter journalArrAdapter ;
    ArrayList<JournalModel> journalArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_journal, container, false);

        searchJournal = view.findViewById(R.id.search_journal);
        journalRecyclerView = view.findViewById(R.id.dailyJournal_recyclerView);
        journalRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        journalArrayList = new ArrayList<>();

        searchJournal.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                //                Toast.makeText(LoginActivity.this, "on click email", Toast.LENGTH_SHORT).show();
                if(b){
                    searchJournal.setBackgroundResource(R.drawable.round_border_primary_color);
                }else{
                    searchJournal.setBackgroundResource(R.drawable.round_border);
                }
//                searchTask.setBackgroundTintList(ColorStateList.valueOf(color));
            }
        });

        journalArrayList.add(new JournalModel("24-06-23","JOURNAL1","report1","learnings1", "pendings1", "adminRemarks1","leaderRemarks1"));
        journalArrayList.add(new JournalModel("25-06-23","JOURNAL2","report2","learnings2", "pendings2", "adminRemarks2","leaderRemarks2"));
        journalArrayList.add(new JournalModel("27-06-23","JOURNAL3","report3","learnings3", "pendings3", "adminRemarks3","leaderRemarks3"));
        journalArrayList.add(new JournalModel("28-06-23","JOURNAL4","report4","learnings4", "pendings4", "adminRemarks4","leaderRemarks4"));
        journalArrayList.add(new JournalModel("29-06-23","JOURNAL5","report5","learnings5", "pendings5", "adminRemarks5","leaderRemarks5"));

        journalArrAdapter = new RecyclerJournalAdapter(getContext(), journalArrayList);
        journalRecyclerView.setAdapter(journalArrAdapter);

        return view;
    }
}