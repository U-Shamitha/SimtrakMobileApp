package com.mad_lab.a1_loginpage.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.mad_lab.a1_loginpage.R;
import com.mad_lab.a1_loginpage.adapter.RecyclerTaskDetailsAdapter;
import com.mad_lab.a1_loginpage.model.TaskDetailsModel;

import java.util.ArrayList;
import java.util.Map;

public class AllTasksFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<TaskDetailsModel> arrTaskDetails;

    String TAG = "AllTasksFragment";

    FirebaseFirestore fstore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_tasks, container, false);

        recyclerView = view.findViewById(R.id.tasksRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        arrTaskDetails = new ArrayList<>();
        fstore = FirebaseFirestore.getInstance();

        getTasksDataFromFireStore();

        arrTaskDetails.add(new TaskDetailsModel("id1","26-05-23","task1","27-05-23","self"));
        arrTaskDetails.add(new TaskDetailsModel("id2","27-05-23","task2","28-05-23","self"));
        arrTaskDetails.add(new TaskDetailsModel("id3","28-05-23","task3","29-05-23","self"));
        arrTaskDetails.add(new TaskDetailsModel("id4","29-05-23","task4","30-05-23","self"));
        arrTaskDetails.add(new TaskDetailsModel("id5","30-05-23","task5","31-05-23","self"));

        RecyclerTaskDetailsAdapter adapter = new RecyclerTaskDetailsAdapter(getContext(), arrTaskDetails);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void getTasksDataFromFireStore() {

        String userId = getDataFromSharedPrefernces("userId");
        if(userId!=""){
            // Fetch a single document
//            DocumentSnapshot documentSnapshot = fstore.collection("users").document(userId).get().getResult();
//            if (documentSnapshot.exists()) {
//                Log.d(TAG, documentSnapshot.getData()+"");
//            } else {
//                Log.d(TAG, "User details not found");
//            }

            CollectionReference usersRef = fstore.collection("users");
            usersRef.document(userId).addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                    Map<String, Object> tasks= (Map<String, Object>) documentSnapshot.getBlob("tasks");
                    Log.d(TAG, "documentSnapshotTasksData: "+documentSnapshot.getString("tasks"));
                    Log.d(TAG, "tasks: "+tasks);

                }
            });
        }
    }

    public String getDataFromSharedPrefernces(String key) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserDetails", MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }
}