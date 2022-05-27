package com.njsv.doctorwala.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.njsv.doctorwala.R;
import com.njsv.doctorwala.login.LoginActivity;
import com.njsv.doctorwala.retrofit.UtilMethods;
import com.njsv.doctorwala.retrofit.mCallBackResponse;

public class OtpActivity extends AppCompatActivity {
    private String name,email,mob,pwd,gender,msg;
    private EditText etOtp;
    private Button btnSubmit;
    private TextView resend;
    private String enterOtp;
    private int otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        init();

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        mob = getIntent().getStringExtra("mobile");
        pwd = getIntent().getStringExtra("password");
        gender = getIntent().getStringExtra("gender");
        otp = getIntent().getIntExtra("otp",-1);
        msg = getIntent().getStringExtra("msg");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    enterOtp = etOtp.getText().toString().trim();
                    if (Integer.parseInt(enterOtp) == otp){
                        signup(name,email,mob,pwd,gender);
//                        Toast.makeText(OtpActivity.this, "valid otp", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(OtpActivity.this, "Please enter valid otp", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOtp(mob);
            }
        });
    }

    private void init(){
        etOtp = findViewById(R.id.enter_otp);
        btnSubmit = findViewById(R.id.btn_submit);
        resend = findViewById(R.id.tv_resend);
    }

    public void signup(String name, String email, String mobile, String password, String gender) {
        if (UtilMethods.INSTANCE.isNetworkAvialable(getApplicationContext())) {
            try {
                UtilMethods.INSTANCE.signup(this, name, email, password, mobile, gender, new mCallBackResponse() {
                    public void success(String from, String message) {
                        Intent intent = new Intent(OtpActivity.this, LoginActivity.class);
                        startActivity(intent);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        finishAffinity();
                    }
                    public void fail(String from) {
                        Toast.makeText(OtpActivity.this, "number/Email already exist", Toast.LENGTH_SHORT).show();
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

    public void verifyOtp(String mobile) {
        if (UtilMethods.INSTANCE.isNetworkAvialable(getApplicationContext())) {
            try {
                UtilMethods.INSTANCE.verifyOtp(this, mobile, new mCallBackResponse() {
                    public void success(String from, String message) {
                        try {
                            OtpModel otpModel = new Gson().fromJson(message,OtpModel.class);

                            if (otpModel != null){
                                if (otpModel.getMessage().equalsIgnoreCase("success")) {
                                   otp = otpModel.getOtp();
                                } else {
                                    Toast.makeText(OtpActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(OtpActivity.this, "otp model null", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    public void fail(String from) {
                        Toast.makeText(OtpActivity.this, "number is not valid", Toast.LENGTH_SHORT).show();
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

}