package com.mad_lab.a1_loginpage.fragments.trainees;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mad_lab.a1_loginpage.R;
import com.mad_lab.a1_loginpage.activity.DashboardActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadFinalReportFragment extends Fragment {

    TextInputLayout submissionDate_tl, uploadFiles_tl;
    TextInputEditText submissionDate_et, uploadFiles_et;
    TextView errMsg_tv;
    Button uploadFinalReport_btn;

    String submissionDate;
    String TAG = "UploadFinalReportFragment";
    private LinearLayout progressBar_ll;
    TextView uploadingFileName_tv;
    TextView uploadPer_tv;
    private ProgressBar progressBar;
    private static final int REQUEST_CODE_FILE_PICKER = 101;
    private List<Uri> selectedFileUris = new ArrayList<>();
    private List<String> uploadedFileUrls = new ArrayList<>();

    String uploadedFileNames = "";
    String finalReportId = null;

    FirebaseFirestore fstore;
    FirebaseStorage storage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_uplaod_final_report, container, false);

        progressBar_ll = view.findViewById(R.id.progressBar_ll);
        uploadingFileName_tv = view.findViewById(R.id.uploadingFileName_tv);
        uploadPer_tv = view.findViewById(R.id.uploadPer_tv);
        progressBar = view.findViewById(R.id.progressBar);

        submissionDate_tl = view.findViewById(R.id.submissionDate_tl);
        uploadFiles_tl = view.findViewById(R.id.uploadFiles_tl);
        submissionDate_et = view.findViewById(R.id.submissionDate_et);
        uploadFiles_et = view.findViewById(R.id.uploadFiles_et);
        errMsg_tv = view.findViewById(R.id.errorMsg_tv);
        uploadFinalReport_btn = view.findViewById(R.id.uploadFinalReport_btn);

        fstore = FirebaseFirestore.getInstance();



//        submissionDate_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                int color;
//                if(b){
//                    color = Color.parseColor("#0099ff");
//                    showDatePickerDialog(view);
//                }else{
//                    color = Color.parseColor("#808080");
//                }
//                submissionDate_tl.setStartIconTintList(ColorStateList.valueOf(color));
//            }
//        });
//        submissionDate_et.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showDatePickerDialog(view);
//            }
//        });

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String currentDate = day + "/" + (month + 1) + "/" + year;
        submissionDate_et.setText(currentDate);


        uploadFiles_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                int color;
                if(b){
                    color = Color.parseColor("#0099ff");
                    uploadFiles_et.setText("");
                    uploadedFileUrls.clear();
                    selectedFileUris.clear();
                    uploadedFileNames = "";
                    openFilePicker();
                }else{
                    color = Color.parseColor("#808080");
                }
                uploadFiles_tl.setStartIconTintList(ColorStateList.valueOf(color));
            }
        });

        uploadFiles_et.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                uploadFiles_et.setText("");
                uploadedFileUrls.clear();
                selectedFileUris.clear();
                uploadedFileNames = "";
                openFilePicker();
            }
        });


        uploadFinalReport_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errMsg_tv.setText("Error message appear here");
                errMsg_tv.setVisibility(View.GONE);

                submissionDate = submissionDate_et.getText().toString().trim();

                if(TextUtils.isEmpty(submissionDate)){
                    errMsg_tv.setText("Please select submission date");
                    submissionDate_et.setError("Please select submission date");
                }else{
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



        return view;
    }


    public void showDatePickerDialog(View v) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                submissionDate_et.setText(selectedDate);
            }
        }, year, month, day);

        datePickerDialog.show();

    }


    //upload files et clicked
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);

        try {
            startActivityForResult(Intent.createChooser(intent, "Select Files"), REQUEST_CODE_FILE_PICKER);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "No file manager found on your device", Toast.LENGTH_SHORT).show();
        }
    }


    //after selecting files
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

                //show the selected file names
                progressBar_ll.setVisibility(View.VISIBLE);
                uploadingFileName_tv.setText(selectedFileUris.size() +" file(s) selected, Click on submit to upload files");

            }
        }
    }


    private void uploadFilesToFirebaseStorage(View view) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String userId = getUserIdFromSharedPrefernces("userId");
        if(finalReportId==null) {
            String dateTimeFormat = "dd-MM-yyyy hh:mm:ss";
            SimpleDateFormat sdtf = new SimpleDateFormat(dateTimeFormat);
            finalReportId = sdtf.format(new Date());
        }
//        StorageReference folderRef = storageRef.child( userId+"/finalReport/"+finalReportId);
//
//        // List all items (files) in the folder
//        folderRef.listAll()
//                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
//                    @Override
//                    public void onSuccess(ListResult listResult) {
//                        // Iterate through the items and delete each file
//                        for (StorageReference item : listResult.getItems()) {
//                            item.delete()
//                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            // File deleted successfully
//                                        }
//                                    })
//                                    .addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            // Handle any errors that occurred during deletion
//                                            e.printStackTrace();
//                                        }
//                                    });
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        e.printStackTrace();
//                    }
//                });


        for (Uri uri : selectedFileUris) {
            // Get the file name from the URI
            String fileName = getFileNameFromUri(getActivity().getApplicationContext(), uri);

            // Create a reference to the destination location in Firebase Storage
            StorageReference fileRef = storageRef.child( userId+"/finalReport/"+finalReportId+"/"+fileName);

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
                        uploadFiles_et.setText(uploadedFileNames);
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

                                        progressBar_ll.setVisibility(View.GONE);
                                        uploadFiles_et.setText(uploadedFileNames);
//                                        Toast.makeText(getContext(),uploadedFileUrls.toString(), Toast.LENGTH_LONG).show();
                                        if(getContext() != null) {
                                            Toast.makeText(getContext(), "Files uploaded successfully", Toast.LENGTH_LONG).show();
                                        }
                                        Log.d("Uploads", "File URLs: " + uploadedFileUrls.toString());
                                        performSubmission(view, true);
                                    }

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

    public String getUserIdFromSharedPrefernces(String key) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserDetails", MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
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

                            ArrayList<Map<String, Object>> finalReports = (ArrayList<Map<String, Object>>) documentSnapshot.get("finalReports")!=null ? (ArrayList<Map<String, Object>>) documentSnapshot.get("finalReports") : new ArrayList<>();

                            Map<String, Object> finalReportMap = new HashMap<>();
                            finalReportMap.put("reportId", finalReportId);
                            finalReportMap.put("status", "in-process");
                            finalReportMap.put("fileName", uploadedFileNames);
                            finalReportMap.put("uploadedOn", submissionDate);
                            if(filesChanged){
                                finalReportMap.put("fileNames", Arrays.asList(uploadedFileNames.split("\n")));
                                finalReportMap.put("fileLinks", uploadedFileUrls);
                            }
                            finalReports.add(finalReportMap);

                            // Save the updated data back to the document
                            documentReference.update("finalReports",finalReports)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getActivity().getApplicationContext(), "Final Report Added Successfully", Toast.LENGTH_LONG).show();
                                            Intent intent= new Intent(getActivity(), DashboardActivity.class);
                                            intent.putExtra("DesFragment","ViewFinalReports");
                                            getActivity().startActivity(intent);
                                            getActivity().finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "Final Report didn't get added: "+e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });

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