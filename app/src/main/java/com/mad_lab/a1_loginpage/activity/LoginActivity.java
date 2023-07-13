package com.mad_lab.a1_loginpage.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mad_lab.a1_loginpage.R;
import com.mad_lab.a1_loginpage.model.Users;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText email_et;
    private TextInputEditText password_et;
    private TextView forgotpwd_tv;
    private TextInputLayout email_tl;
    private TextInputLayout password_tl;
    private Button login_bt;
    private TextView signup_tv;
    LottieAnimationView loginLaView;
    private ImageButton mic_ib;
    public int VOICE_INPUT_REQ_CODE = 200;

    FirebaseAuth auth;
    FirebaseFirestore fstore;

    private String TAG="LoginActivity";
    public static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_et = findViewById(R.id.email_et_login1);
        password_et = findViewById(R.id.password_et_login1);
        forgotpwd_tv = findViewById(R.id.forgotPwd_tv);
        login_bt = findViewById(R.id.login_bt);
        email_tl = findViewById(R.id.email_tl);
        password_tl = findViewById(R.id.password_tl);
        loginLaView = findViewById(R.id.loginLaView);
        signup_tv = findViewById(R.id.signUp_tv);
//        mic_ib = findViewById(R.id.mic);

        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        SharedPreferences sharedPref = LoginActivity.this.getSharedPreferences("login_details", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        if(sharedPref.getBoolean("isLogin",false)){
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
            finish();
        }

        getUserDataFromFireStore(sharedPref.getString("userId",""));

        email_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
//                Toast.makeText(LoginActivity.this, "on click email", Toast.LENGTH_SHORT).show();
                int color;
                if(b){
                    color = Color.parseColor("#0099ff");
                }else{
                    color = Color.parseColor("#808080");
                }
                email_tl.setStartIconTintList(ColorStateList.valueOf(color));
                email_tl.setHintTextColor(ColorStateList.valueOf(color));

            }
        });
        password_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
//                Toast.makeText(LoginActivity.this, "on click email", Toast.LENGTH_SHORT).show();
                int color;
                if(b){
                    color = Color.parseColor("#0099ff");
                }else{
                    color = Color.parseColor("#808080");
                }
                password_tl.setEndIconTintList(ColorStateList.valueOf(color));
                password_tl.setHintTextColor(ColorStateList.valueOf(color));
            }
        });
        forgotpwd_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                View dailogView = getLayoutInflater().inflate(R.layout.forgot_pwd_dialog_box, null);
                TextInputEditText email_et = dailogView.findViewById(R.id.email_et_fpwd);
                TextInputLayout email_tl = dailogView.findViewById(R.id.email_tl);

                builder.setView(dailogView);
                AlertDialog dialog = builder.create();

                email_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
//                Toast.makeText(LoginActivity.this, "on click email", Toast.LENGTH_SHORT).show();
                        int color;
                        if(b){
                            color = Color.parseColor("#0099ff");
                        }else{
                            color = Color.parseColor("#808080");
                        }
                        email_tl.setStartIconTintList(ColorStateList.valueOf(color));
                        email_tl.setHintTextColor(ColorStateList.valueOf(color));

                    }
                });

                dailogView.findViewById(R.id.reset_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String userEmail = email_et.getText().toString().trim();

                        if(TextUtils.isEmpty(userEmail)){
                            email_et.setError("Email cannot be empty");
                        } else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                            email_et.setError("Enter valid email");
                        } else {
                            auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(LoginActivity.this, "Email sent", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }else{
                                        Toast.makeText(LoginActivity.this, "Unable to send email", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });

                dailogView.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                if(dialog.getWindow() != null){
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.show();

            }
        });
        signup_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                finish();
            }
        });
        login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(LoginActivity.this, "login button clicked", Toast.LENGTH_SHORT).show();

                String email = email_et.getText().toString().trim();
                String password = password_et.getText().toString().trim();

                if (email.isEmpty()) {
                    email_et.setError("Email cannot be empty");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    email_et.setError("Please enter valid email");
                } else if (password.isEmpty()) {
                    password_et.setError("Password cannot be empty");
                } else if (password.length() < 6) {
                    password_et.setError("Password should be at least 6 characters long");
                } else {

                    auth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
//                                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    editor.putBoolean("isLogin", true);
                                    editor.putString("userEmail",email);
                                    editor.apply();
                                    SignUpActivity.loggedInUserEmail= email;
                                    storeDataInSharedPrefernces("userId", auth.getCurrentUser().getUid());
                                    getUserDataFromFireStore(auth.getCurrentUser().getUid());
//                                    startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
////                                    startActivity(new Intent(LoginActivity.this, SimtrakDashboardActivity.class));
//                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }
        });
//        mic_ib.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openVoiceDialog();
//            }
//        });

    }


    //    voice command extraction start
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == VOICE_INPUT_REQ_CODE && resultCode == RESULT_OK){
            ArrayList<String> arrayList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String voice = arrayList.get(0);
            Toast.makeText(this, "voice command: "+voice, Toast.LENGTH_SHORT).show();
            if (voice.contains("signup") || voice.contains("sign up")) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        }else{
            Toast.makeText(this, "Invalid request code", Toast.LENGTH_SHORT).show();
        }
    }

    private void openVoiceDialog() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, VOICE_INPUT_REQ_CODE);
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

//    voice command extraction end


    private void getUserDataFromFireStore(String userId) {

//        String userId = getDataFromSharedPrefernces("userId");
//        String userId = auth.getCurrentUser().getUid();
        Toast.makeText(getApplicationContext(), "userId: "+userId, Toast.LENGTH_SHORT).show();
        if(userId!=""){
            // Fetch a single document
//            DocumentSnapshot documentSnapshot = fstore.collection("users").document(userId).get().getResult();
//            if (documentSnapshot.exists()) {
//                Log.d(TAG, documentSnapshot.getData()+"");
//            } else {
//                Log.d(TAG, "User details not found");
//            }

            CollectionReference usersRef = fstore.collection("users");
            usersRef.document(userId).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                    Log.d(TAG, "documentSnapshotUsersData "+documentSnapshot.getData().toString());
                    storeDataInSharedPrefernces("salutation", documentSnapshot.getString("salutation"));
                    storeDataInSharedPrefernces("applyingFor", documentSnapshot.getString("applyingFor"));
                    storeDataInSharedPrefernces("firstName", documentSnapshot.getString("firstName"));
                    storeDataInSharedPrefernces("lastName", documentSnapshot.getString("lastName"));
                    storeDataInSharedPrefernces("email", documentSnapshot.getString("email"));
                    storeDataInSharedPrefernces("countryCode", documentSnapshot.getString("countryCode"));
                    storeDataInSharedPrefernces("countryName", documentSnapshot.getString("countryName"));
                    storeDataInSharedPrefernces("phoneNumber", documentSnapshot.getString("phoneNumber"));
                    storeDataInSharedPrefernces("city", documentSnapshot.getString("city"));
                    storeDataInSharedPrefernces("pincode", documentSnapshot.getString("pincode"));
                    startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
//                                    startActivity(new Intent(LoginActivity.this, SimtrakDashboardActivity.class));
                    finish();
                }
            });
        }

// Fetch multiple documents from a collection
      /*  Query query = firestore.collection(collectionName).whereEqualTo("field", "value");
        ApiFuture<QuerySnapshot> querySnapshotFuture = query.get();
        QuerySnapshot querySnapshot = querySnapshotFuture.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot doc : documents) {
            System.out.println("Document data: " + doc.getData());
        } */
    }

    public String getDataFromSharedPrefernces(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

    void storeDataInSharedPrefernces(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
        SharedPreferences.Editor  editor= sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }


}