package com.njsv.doctorwala.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njsv.doctorwala.R;
import com.njsv.doctorwala.fragment.WalletModel;
import com.njsv.doctorwala.login.LoginActivity;
import com.njsv.doctorwala.login.LoginData;
import com.njsv.doctorwala.retrofit.UtilMethods;
import com.njsv.doctorwala.retrofit.mCallBackResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SignupActivity extends AppCompatActivity {

    private EditText signupName, signupemail, signupMob, signupPassword;
    private TextView tvSignin;
    private Button btnSignup;
    private String select;
    private String[] select_Type = {"Select Gender", "Male", "Female"};
    public Spinner spinner;
    private String name, email, mob, pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        init();
        tvSignin.setOnClickListener(view -> {
            Intent tvSigninIntent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(tvSigninIntent);
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, this.select_Type);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (!spinner.getSelectedItem().toString().equals("Select Gender")) {
                    select = spinner.getSelectedItem().toString();
                    Toast.makeText(SignupActivity.this, "Gender : " + select, Toast.LENGTH_SHORT).show();
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        btnSignup.setOnClickListener(view -> {

            name = signupName.getText().toString().trim();
            email = signupemail.getText().toString().trim();
            mob = signupMob.getText().toString().trim();
            pwd = signupPassword.getText().toString().trim();

//            try {
//                if (formvalidate()) {
//
//                    verifyOtp(mob);
////                    signup(signupName.getText().toString().trim(), signupemail.getText().toString().trim(), signupMob.getText().toString().trim(), signupPassword.getText().toString().trim(), select);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            verifyOtp(mob);

        });

    }

    public void init() {

        signupName = findViewById(R.id.name);
        signupemail = findViewById(R.id.email);
        signupMob = findViewById(R.id.signup_mobile);
        signupPassword = findViewById(R.id.password);
        btnSignup = findViewById(R.id.btn_signup);
        tvSignin = findViewById(R.id.text_signin);
        spinner = findViewById(R.id.spinner);

    }

    public boolean formvalidate() {
        if (signupName.getText().toString().trim().equals("")) {
            signupName.setError("Please Enter your Name");
            return false;
        }
        if (select == null) {
            Toast.makeText(this, "Please select Gender", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (signupemail.getText().toString().trim().equals("")) {
            signupemail.setError("Please Enter your Email");
            return false;
        }
        if (signupMob.getText().toString().equals("")) {
            signupMob.setError("Enter Your Mobile No.");
            return false;
        }
        if (signupPassword.getText().toString().equals("")) {
            signupPassword.setError("Enter Your Password");
            return false;
        }
        return true;
    }

    public void verifyOtp(String mobile) {
        if (UtilMethods.INSTANCE.isNetworkAvialable(getApplicationContext())) {
            try {
                UtilMethods.INSTANCE.verifyOtp(this, mobile, new mCallBackResponse() {
                    public void success(String from, String message) {
                        try {
                            OtpModel otpModel = new Gson().fromJson(message,OtpModel.class);

                            if (otpModel != null){
                                if (otpModel.getMessage().equalsIgnoreCase("success")) {
                                    Intent intent = new Intent(SignupActivity.this, OtpActivity.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("email", email);
                                    intent.putExtra("mobile", mob);
                                    intent.putExtra("password", pwd);
                                    intent.putExtra("gender", select);
                                    intent.putExtra("otp", otpModel.getOtp());
                                    intent.putExtra("msg", otpModel.getMessage());
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(SignupActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(SignupActivity.this, "otp model null", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    public void fail(String from) {
                        Toast.makeText(SignupActivity.this, "number is not valid", Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "number already exist", Toast.LENGTH_SHORT).show();
            }
        }
        UtilMethods.INSTANCE.internetNotAvailableMessage(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent tvSignupIntent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(tvSignupIntent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}