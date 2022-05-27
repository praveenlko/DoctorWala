package com.njsv.doctorwala.login;

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
import com.google.gson.reflect.TypeToken;
import com.njsv.doctorwala.R;
import com.njsv.doctorwala.fragment.PharmacyOrderModel;
import com.njsv.doctorwala.login.ForgetPasswordActivity;
import com.njsv.doctorwala.order.OrderModel;
import com.njsv.doctorwala.retrofit.UtilMethods;
import com.njsv.doctorwala.retrofit.mCallBackResponse;
import com.njsv.doctorwala.signup.SignupActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText email;
    private Button btnChangePwd;
    private TextView signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        email = findViewById(R.id.email);
        btnChangePwd = findViewById(R.id.btn_change_pwd);
        signup = findViewById(R.id.text_signup);

        btnChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!email.getText().toString().isEmpty()) {
                    resetLink(email.getText().toString());
                } else {
                    email.setError("Please enter email address");
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tvSignupIntent = new Intent(ForgetPasswordActivity.this, SignupActivity.class);
                startActivity(tvSignupIntent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    private void resetLink(String email) {
        if (UtilMethods.INSTANCE.isNetworkAvialable(ForgetPasswordActivity.this)) {
            UtilMethods.INSTANCE.resetLink(ForgetPasswordActivity.this, email, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {
                    if (message.equalsIgnoreCase("success")) {
                        Intent intent = new Intent(ForgetPasswordActivity.this, ChangePasswordActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    } else if (message.equalsIgnoreCase("\"Please enter a valid email\"")) {
                        Toast.makeText(ForgetPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ForgetPasswordActivity.this, "Something went forget wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void fail(String from) {
                    Toast.makeText(ForgetPasswordActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            UtilMethods.INSTANCE.internetNotAvailableMessage(ForgetPasswordActivity.this);
        }
    }
}