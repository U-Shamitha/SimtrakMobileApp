package com.mad_lab.a1_loginpage.fragments.home;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.common.reflect.TypeToken;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.mad_lab.a1_loginpage.R;
import com.mad_lab.a1_loginpage.activity.DashboardActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EditNoteFragment extends Fragment {

    TextInputLayout note_tl;
    TextInputEditText note_et;
    TextInputLayout file_tl;
    TextInputEditText file_et;
    TextInputLayout timeTaken_tl;
    TextInputEditText timeTaken_et;


    TextView errMsg_tv;
    Button addNote_btn;

    String TAG= "AddNoteFragment";

    String taskId ;
    String noteId;
    String note, file, timeTaken;

    private LinearLayout progressBar_ll;
    TextView uploadingFileName_tv;
    TextView uploadPer_tv;
    private ProgressBar progressBar;
    private static final int REQUEST_CODE_FILE_PICKER = 101;
    private List<Uri> selectedFileUris = new ArrayList<>();
    private List<String> uploadedFileUrls = new ArrayList<>();

    String uploadedFileNames = "";

    FirebaseFirestore fstore;
    FirebaseStorage storage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_note, container, false);

        progressBar_ll = view.findViewById(R.id.progressBar_ll);
        uploadingFileName_tv = view.findViewById(R.id.uploadingFileName_tv);
        uploadPer_tv = view.findViewById(R.id.uploadPer_tv);
        progressBar = view.findViewById(R.id.progressBar);

        note_tl = view.findViewById(R.id.note_tl);
        note_et = view.findViewById(R.id.note_et);
        file_tl = view.findViewById(R.id.file_tl);
        file_et = view.findViewById(R.id.file_et);
        timeTaken_tl = view.findViewById(R.id.timeTaken_tl);
        timeTaken_et = view.findViewById(R.id.timeTaken_et);
        errMsg_tv = view.findViewById(R.id.errorMsg_tv);
        addNote_btn = view.findViewById(R.id.addNote_btn);


        fstore = FirebaseFirestore.getInstance();

        note_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                int color;
                if(b){
                    color = Color.parseColor("#0099ff");
                }else{
                    color = Color.parseColor("#808080");
                }
                note_tl.setStartIconTintList(ColorStateList.valueOf(color));
            }
        });
        file_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                int color;
                if(b){
                    color = Color.parseColor("#0099ff");
                    file_et.setText("");
                    uploadedFileUrls.clear();
                    selectedFileUris.clear();
                    uploadedFileNames = "";
                    openFilePicker();
                }else{
                    color = Color.parseColor("#808080");
                }
                file_tl.setStartIconTintList(ColorStateList.valueOf(color));
            }
        });
        timeTaken_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                int color;
                if(b){
                    color = Color.parseColor("#0099ff");
                }else{
                    color = Color.parseColor("#808080");
                }
                timeTaken_tl.setStartIconTintList(ColorStateList.valueOf(color));
            }
        });

        file_et.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                file_et.setText("");
                uploadedFileUrls.clear();
                selectedFileUris.clear();
                uploadedFileNames = "";
                openFilePicker();
            }
        });


        addNote_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errMsg_tv.setVisibility(View.GONE);
                errMsg_tv.setText("Error message appear here");
                note = note_et.getText().toString().trim();
                file = file_et.getText().toString().trim();
                timeTaken = timeTaken_et.getText().toString().trim();

                if(TextUtils.isEmpty(note)){
                    note_et.setError("Please enter note");
                    errMsg_tv.setText("Please enter note");
                }
//                else if (TextUtils.isEmpty(file)) {
//                    file_et.setError("Please select file");
//                    errMsg_tv.setText("Please select file");
//                }
                else if (TextUtils.isEmpty(timeTaken)) {
                    timeTaken_et.setError("Please enter time taken");
                    errMsg_tv.setText("Please enter time taken");
                }
                else {
                    if(selectedFileUris.size()>0){
                        uploadFilesToFirebaseStorage(view);
                    }else {
                        performSubmission(view, false);
                    }
                }

                if(!errMsg_tv.getText().toString().equals("Error message appear here")){
                    errMsg_tv.setVisibility(View.VISIBLE);
                }
            }
        });


        getTaskDataFromSharedPrefernces("selectedTaskDetails");
        getNoteDataFromSharedPrefernces("selectedNoteDetails");

        return view;
    }

    public String getUserIdFromSharedPrefernces(String key) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserDetails", MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

    public void getTaskDataFromSharedPrefernces(String key) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("EditTaskDetails", MODE_PRIVATE);
        String jsonString = sharedPreferences.getString(key,"");
        if(jsonString!= null) {
            Map<String, Object> mapData = new Gson().fromJson(jsonString, new TypeToken<Map<String, Object>>() {
            }.getType());
            Toast.makeText(getContext(), "taskId: "+mapData.get("TaskId").toString(), Toast.LENGTH_LONG).show();
            taskId = mapData.get("TaskId").toString();
        }

    }

    public void getNoteDataFromSharedPrefernces(String key) {
        Log.d("DataRetInEditNote","in getData");
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("EditNoteDetails", MODE_PRIVATE);
        String jsonString = sharedPreferences.getString(key,"");
        Log.d("DataRetInEditNote",jsonString);
        if(jsonString!= null) {
            Map<String, Object> mapData = new Gson().fromJson(jsonString, new TypeToken<Map<String, Object>>() {
            }.getType());

            note_et.setText(mapData.get("Note") + "");
            timeTaken_et.setText(mapData.get("TimeTaken")+"");
            noteId = mapData.get("NoteId").toString();
        }

    }


    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        try {
            startActivityForResult(Intent.createChooser(intent, "Select Files"), REQUEST_CODE_FILE_PICKER);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "No file manager found on your device", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_FILE_PICKER && resultCode == RESULT_OK) {
            if (data != null) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri uri = data.getClipData().getItemAt(i).getUri();
                        selectedFileUris.add(uri);
                    }
                } else if (data.getData() != null) {
                    Uri uri = data.getData();
                    selectedFileUris.add(uri);
                }

                // Call the method to upload files to Firebase Storage
                progressBar_ll.setVisibility(View.VISIBLE);
                uploadingFileName_tv.setText(selectedFileUris.size() +" file(s) selected, Click on submit to upload files");
//                uploadFilesToFirebaseStorage();
            }
        }
    }

    private void uploadFilesToFirebaseStorage(View view) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String userId = getUserIdFromSharedPrefernces("userId");
        StorageReference folderRef = storageRef.child( userId+"/"+taskId+"/"+"notes/"+noteId);
        // List all items (files) in the folder
        folderRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        // Iterate through the items and delete each file
                        for (StorageReference item : listResult.getItems()) {
                            item.delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // File deleted successfully
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Handle any errors that occurred during deletion
                                            e.printStackTrace();
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors that occurred during listing
                        // For example, handle permission issues or network failures
                        e.printStackTrace();
                    }
                });
        for (Uri uri : selectedFileUris) {
            // Get the file name from the URI
            String fileName = getFileNameFromUri(getActivity().getApplicationContext(), uri);

            // Create a reference to the destination location in Firebase Storage
            StorageReference fileRef = storageRef.child( userId+"/"+taskId+"/"+"notes/"+noteId+"/"+fileName);

            // Upload the file to Firebase Storage
            UploadTask uploadTask = fileRef.putFile(uri);

            progressBar_ll.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            uploadPer_tv.setVisibility(View.VISIBLE);

            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    // Update the progress bar with the upload progress
                    double progress = (double) ((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                    uploadingFileName_tv.setText("Uploading "+getFileNameFromUri(getContext(), uri)+"  ");
                    uploadPer_tv.setText(Math.round(progress)+"%");
                    progressBar.setProgress((int)progress);
                    if(Math.round(progress) == 100){
                        if(uploadedFileNames=="") {
                            uploadedFileNames = uploadedFileNames + getFileNameFromUri(getContext(), uri);
                        }else{
                            uploadedFileNames = uploadedFileNames + "\n"+getFileNameFromUri(getContext(), uri);
                        }
                        file_et.setText(uploadedFileNames);
                    }
                }
            });


            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        // Get the download URL of the uploaded file
                        fileRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri downloadUrl = task.getResult();
                                    String fileUrl = downloadUrl.toString();
                                    uploadedFileUrls.add(fileUrl);

                                    // Check if all files are uploaded, and do something with the URLs
                                    if (uploadedFileUrls.size() == selectedFileUris.size()) {
                                        // All files are uploaded, use the 'uploadedFileUrls' list as needed.
                                        progressBar_ll.setVisibility(View.GONE);
                                        file_et.setText(uploadedFileNames);
//                                        Toast.makeText(getContext(),uploadedFileUrls.toString(), Toast.LENGTH_LONG).show();
                                        if(getContext() != null) {
                                            Toast.makeText(getContext(), "Files uploaded successfully", Toast.LENGTH_LONG).show();
                                        }
                                        Log.d("Uploads", "File URLs: " + uploadedFileUrls.toString());
                                        performSubmission(view, true);
                                    }

//                                    performSubmission(view, true);
                                }
                            }
                        });
                    } else {
                        // Handle the failure to upload the file
                        Toast.makeText(getContext(), "File upload failed", Toast.LENGTH_SHORT).show();
                        Log.e("Uploads", "File upload failed", task.getException());
                    }
                }
            });
        }
    }

    @SuppressLint("Range")
    private static String getFileNameFromUri(Context context, Uri uri) {
        String fileName = "";
        if(context != null) {
            String scheme = uri.getScheme();

            if (scheme != null) {
                if (scheme.equals(ContentResolver.SCHEME_CONTENT)) {
                    Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        cursor.close();
                    }
                } else if (scheme.equals(ContentResolver.SCHEME_FILE)) {
                    fileName = new File(uri.getPath()).getName();
                }
            }
        }

        return fileName;
    }

    public void performSubmission(View view, boolean filesChanged){
        String userId = getUserIdFromSharedPrefernces("userId");
        DocumentReference documentReference = fstore.collection("users").document(userId);
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            ArrayList<Map<String, Object>> tasks= (ArrayList<Map<String, Object>>) documentSnapshot.get("tasks")!=null ? (ArrayList<Map<String, Object>>) documentSnapshot.get("tasks") : new ArrayList<>();

                            if (tasks != null) {
                                for (Map<String, Object> task : tasks) {
                                    Log.d("all tasks", task.toString());
                                    if (taskId.equals(task.get("taskId"))) {
//
                                        ArrayList<Map<String, Object>> notesinTask = (ArrayList<Map<String, Object>>) task.get("notes");
                                        Log.d("notesInTask", notesinTask.toString());

                                        Map<String, Object> noteMap = new HashMap<>();
                                        noteMap.put("noteId", noteId);
                                        noteMap.put("note", note);
                                        if(filesChanged){
                                            noteMap.put("fileNames", Arrays.asList(uploadedFileNames.split("\n")));
                                            noteMap.put("fileLinks", uploadedFileUrls);
                                        }
                                        noteMap.put("timeTaken", timeTaken);

                                        for (Map<String, Object> note : notesinTask) {
                                            if (noteId.equals(note.get("noteId"))) {
                                                if(!filesChanged){
                                                    noteMap.put("fileNames", note.get("fileNames"));
                                                    noteMap.put("fileLinks", note.get("fileLinks"));
                                                }
                                                notesinTask.remove(note);
                                                notesinTask.add(noteMap);
                                                break;
                                            }
                                        }
//                                        notesinTask.add(noteMap);

                                        task.put("notes",notesinTask);
                                        Log.d("notesInTask", task.toString());
                                        Log.d(TAG, task.toString());

                                        // Save the updated data back to the document
                                        documentReference.update("tasks",tasks)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(getActivity().getApplicationContext(), "Note edited Successfully", Toast.LENGTH_LONG).show();
                                                        Intent intent= new Intent(getActivity(), DashboardActivity.class);
                                                        intent.putExtra("DesFragment","ViewNotes");
                                                        getActivity().startActivity(intent);
//                                                                    startActivity(new Intent(getActivity(), DashboardActivity.class));
                                                                    getActivity().finish();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getContext(), "Note didn't get edited: "+e.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                });


                                        break; // Assuming there is only one matching item
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


}