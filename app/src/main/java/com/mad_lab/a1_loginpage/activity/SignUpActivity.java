package com.mad_lab.a1_loginpage.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.hbb20.CountryCodePicker;
import com.mad_lab.a1_loginpage.R;
import com.mad_lab.a1_loginpage.model.Users;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private  TextInputLayout salutation_tl;
    private TextInputLayout applyingFor_tl;
    private TextInputLayout firstName_tl;
    private TextInputLayout lastName_tl;
    private TextInputLayout email_tl;
    private TextInputLayout phoneNumber_tl;
    private TextInputLayout city_tl;
    private TextInputLayout pincode_tl;
    private TextInputLayout password_tl;
    private TextInputLayout confirmPassword_tl;


    private AutoCompleteTextView salutation_atv;
    private AutoCompleteTextView applyingFor_atv;
    private TextInputEditText firstName_et;
    private TextInputEditText lastName_et;
    private TextInputEditText email_et;
    private TextInputEditText phoneNumber_et;
    private TextInputEditText city_et;
    private TextInputEditText pincode_et;
    private TextInputEditText password_et;
    private TextInputEditText confirmPassword_et;
    private CountryCodePicker ccp;

    private TextView errorMsg_tv;
    private Button signup_bt;
    private TextView login_tv;
    public static String loggedInUserEmail;

    FirebaseAuth auth;
    public static FirebaseDatabase database;
    public FirebaseFirestore fstore;
    public static String userId;

    String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);


        if(getApplicationContext().getSharedPreferences("login_details", Context.MODE_PRIVATE).getBoolean("isLogin", false)){
            loggedInUserEmail = getApplicationContext().getSharedPreferences("login_details", Context.MODE_PRIVATE).getString("userEmail","");
            if(!loggedInUserEmail.equals("")) {
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            }
        }

        salutation_tl = findViewById(R.id.salutation_tl);
        applyingFor_tl = findViewById(R.id.applyingFor_tl);
        firstName_tl = findViewById(R.id.firstName_tl);
        lastName_tl = findViewById(R.id.lastName_tl);
        email_tl = findViewById(R.id.email_tl_signup);
        phoneNumber_tl = findViewById(R.id.phoneNumber_tl);
        city_tl = findViewById(R.id.city_tl);
        pincode_tl = findViewById(R.id.pincode_tl);
        password_tl = findViewById(R.id.password_tl_signup);
        confirmPassword_tl = findViewById(R.id.confirmPassword_tl);

        salutation_atv = findViewById(R.id.salutation_atv);
        applyingFor_atv = findViewById(R.id.applyingFor_atv);
        firstName_et = findViewById(R.id.firstName_et);
        lastName_et = findViewById(R.id.lastName_et);
        email_et = findViewById(R.id.email_et_signup);
        phoneNumber_et = findViewById(R.id.phoneNumber_et);
        city_et = findViewById(R.id.city_et);
        pincode_et = findViewById(R.id.pincode_et);
        password_et = findViewById(R.id.password_et_signup);
        confirmPassword_et = findViewById(R.id.confirmPassword_et);

        ccp = findViewById(R.id.countryCodePicker);
        errorMsg_tv = findViewById(R.id.errorMsg_tv);
        signup_bt = findViewById(R.id.signup_bt);
        login_tv = findViewById(R.id.login_tv);

        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();

        changeIconColorOnFocus(salutation_tl, null, salutation_atv, "start");
        changeIconColorOnFocus(applyingFor_tl, null, applyingFor_atv, "start");
        changeIconColorOnFocus(firstName_tl, firstName_et, null, "start");
        changeIconColorOnFocus(lastName_tl, lastName_et, null, "start");
        changeIconColorOnFocus(email_tl, email_et, null, "start");
        changeIconColorOnFocus(city_tl, city_et, null, "start");
        changeIconColorOnFocus(pincode_tl, pincode_et, null, "start");
        changeIconColorOnFocus(password_tl, password_et, null, "end");
        changeIconColorOnFocus(confirmPassword_tl, confirmPassword_et, null, "end");

        setSpinnerForAtv(salutation_atv, getResources().getStringArray(R.array.salutationSpinnerList));
        setSpinnerForAtv(applyingFor_atv, getResources().getStringArray(R.array.applyingForSpinnerList));


        signup_bt.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });
        signup_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String salutation = salutation_atv.getText().toString().trim();
                String applyingFor = applyingFor_atv.getText().toString().trim();
                String firstName = firstName_et.getText().toString().trim();
                String lastName = lastName_et.getText().toString().trim();
                String email = email_et.getText().toString().trim();
                String cc = ccp.getSelectedCountryCodeWithPlus().trim();
                String countryName = ccp.getSelectedCountryEnglishName();
                String phoneNumber = phoneNumber_et.getText().toString().trim();
                String city = city_et.getText().toString().trim();
                String pincode = pincode_et.getText().toString().trim();
                String password = password_et.getText().toString().trim();
                String confirmPassword = confirmPassword_et.getText().toString().trim();

//                Toast.makeText(getApplicationContext(), "cc"+cc, Toast.LENGTH_SHORT).show();

                if(salutation.isEmpty()){
//                    salutation_atv.setError("Please select salutation");
                    errorMsg_tv.setText("Please select salutation");
                }
                else if(applyingFor.isEmpty()){
//                     applyingFor_atv.setError("Please select any domain");
                    errorMsg_tv.setText("Please select any domain in Applying For");
                }
                else if(firstName.isEmpty()){
                    firstName_et.setError("First name cannot be empty");
                    errorMsg_tv.setText("First name cannot be empty");
                }
                else if(lastName.isEmpty()){
                    lastName_et.setError("Last name cannot be empty");
                    errorMsg_tv.setText("Last name cannot be empty");
                }
                else if(email.isEmpty()){
                    email_et.setError("Email cannot be empty");
                    errorMsg_tv.setText("Email cannot be empty");
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    email_et.setError("Please enter valid email");
                    errorMsg_tv.setText("Please enter valid email");
                }
                else if(phoneNumber.isEmpty()){
                    phoneNumber_et.setError("Phone number cannot be empty");
                    errorMsg_tv.setText("Phone number cannot be empty");
                }
                else if(phoneNumber.length()<10){
                    phoneNumber_et.setError("Phone number must contain 10 digits");
                    errorMsg_tv.setText("Phone number must contain 10 digits");
                }
                else if(cc.isEmpty() || !Patterns.PHONE.matcher(cc+phoneNumber).matches()){
                    phoneNumber_et.setError("Please enter valid phone number");
                    errorMsg_tv.setText("Please enter valid phone number");
                }
                else if(city.isEmpty()){
                    city_et.setError("City cannot be empty");
                    errorMsg_tv.setText("City cannot be empty");
                }
                else if(pincode.isEmpty()){
                    pincode_et.setError("Pincode cannot be empty");
                    errorMsg_tv.setText("Pincode cannot be empty");
                }
                else if(password.isEmpty()){
//                    password_et.setError("Password cannot be empty");
                    errorMsg_tv.setText("Password cannot be empty");
                }
                else if(password.length()<6){
//                    password_et.setError("Password should be at least 6 characters long");
                    errorMsg_tv.setText("Password should be at least 6 characters long");
                }
                else if(!confirmPassword.equals(password)){
//                    confirmPassword_et.setError("Password does not match");
                    errorMsg_tv.setText("Password and Confirm Password didn't match");
                }
                else{

                    Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()) {
//                               Toast.makeText(SignUpActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                               Log.d(TAG, "SignUp Successful");

                               //store data to firebase
                               userId = auth.getCurrentUser().getUid(); //getting uid of currently registered user
                               DocumentReference documentReference = fstore.collection("users").document(userId);
                               Map<String, Object> user = new HashMap<>();
                               user.put("salutation", salutation);
                               user.put("applyingFor", applyingFor);
                               user.put("firstName", firstName);
                               user.put("lastName", lastName);
                               user.put("email", email);
                               user.put("countryCode", cc);
                               user.put("countryName", countryName);
                               user.put("phoneNumber", phoneNumber);
                               user.put("city", city);
                               user.put("pincode", pincode);
//                                    Users user2 = new Users(salutation, applyingFor, firstName, lastName, email, cc, phoneNumber, city, pincode);
                               documentReference.set(user)
                                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                                           @Override
                                           public void onSuccess(Void unused) {
//                                               Toast.makeText(getApplicationContext(), "user data uploaded to firestore", Toast.LENGTH_SHORT).show();
                                               storeDataInSharedPrefernces("userId", userId);
                                               Log.d(TAG, "user data uploaded to firestore");
                                               startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                               finish();
                                           }
                                       })
                                       .addOnFailureListener(new OnFailureListener() {
                                           @Override
                                           public void onFailure(@NonNull Exception e) {
                                               Log.e(TAG, "unable to upload user data to firestore");
                                           }
                                       });

                           }

}
                    })

                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "SignUp Failed", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "SignUpFailed: "+e.getMessage());
                                errorMsg_tv.setText("SignUpFailed: \n"+e.getMessage());
                                errorMsg_tv.setVisibility(View.VISIBLE);
                            }
                        });

                }

                if(!errorMsg_tv.getText().toString().equals("Error message appear here")){
                    errorMsg_tv.setVisibility(View.VISIBLE);
                }

            }
        });


        login_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }

    void storeDataInSharedPrefernces(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
        SharedPreferences.Editor  editor= sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public void changeIconColorOnFocus(TextInputLayout tl, TextInputEditText et, AutoCompleteTextView atv, String iconPositon) {
        if (et != null) {
            et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    int color;
                    if (b) {
                        color = Color.parseColor("#0099ff");
                    } else {
                        color = Color.parseColor("#808080");
                    }
                    if (iconPositon.equals("start")) {
                        tl.setStartIconTintList(ColorStateList.valueOf(color));
                    } else if (iconPositon.equals("end")) {
                        tl.setEndIconTintList(ColorStateList.valueOf(color));
                    }
                }
            });
        }
        if(atv != null){
            atv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    int color;
                    if (b) {
                        color = Color.parseColor("#0099ff");
                    } else {
                        color = Color.parseColor("#808080");
                    }
                    if (iconPositon.equals("start")) {
                        tl.setStartIconTintList(ColorStateList.valueOf(color));
                    } else if (iconPositon.equals("end")) {
                        tl.setEndIconTintList(ColorStateList.valueOf(color));
                    }
                    atv.showDropDown();
                }
            });
        }
    }

    public void setSpinnerForAtv(AutoCompleteTextView atv, String[] dropDownList){

        ArrayAdapter<String> taskTopicArrayAdapter = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, dropDownList);
        atv.setAdapter(taskTopicArrayAdapter);
        atv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                atv.showDropDown();
            }
        });


        atv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = atv.getAdapter().getItem(i).toString();
//                Toast.makeText(getApplicationContext(), selectedItem, Toast.LENGTH_SHORT).show();
            }
        });
    }
}