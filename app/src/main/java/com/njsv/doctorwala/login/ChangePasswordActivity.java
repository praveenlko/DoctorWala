package com.njsv.doctorwala.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.njsv.doctorwala.R;
import com.njsv.doctorwala.retrofit.UtilMethods;
import com.njsv.doctorwala.retrofit.mCallBackResponse;

public class ChangePasswordActivity extends AppCompatActivity {

    private Button btnChange;
    private TextView errormsg;
    private EditText newPwd, confirmPwd;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        btnChange = findViewById(R.id.btn_change_pwd);
        errormsg = findViewById(R.id.error_pwd);
        newPwd = findViewById(R.id.new_pwd);
        confirmPwd = findViewById(R.id.confirm_pwd);
        email = getIntent().getStringExtra("email");

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!newPwd.getText().toString().isEmpty()){
                    if (!confirmPwd.getText().toString().isEmpty()){
                        if (newPwd.getText().toString().equals(confirmPwd.getText().toString())) {
                            updatePassword(email, newPwd.getText().toString(), confirmPwd.getText().toString());
                        } else {
                            Toast.makeText(ChangePasswordActivity.this, "Password are not same", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        confirmPwd.setError("Please enter confirm password");
                    }
                }else{
                    newPwd.setError("Please enter new password");
                }
            }
        });


        confirmPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                errormsg.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (confirmPwd.getText().toString().equals(newPwd.getText().toString())) {
                    errormsg.setVisibility(View.GONE);
                } else {
                    errormsg.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        newPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                errormsg.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (newPwd.getText().toString().equals(confirmPwd.getText().toString())) {
                    errormsg.setVisibility(View.GONE);
                } else {
                    errormsg.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void updatePassword(String email, String password, String confirmPassword) {
        if (UtilMethods.INSTANCE.isNetworkAvialable(ChangePasswordActivity.this)) {
            UtilMethods.INSTANCE.updatePassword(ChangePasswordActivity.this, email, password, confirmPassword, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {
                    Intent intent = new Intent(ChangePasswordActivity.this,SuccessPasswordActivity.class);
                    startActivity(intent);
                    finishAffinity();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
                @Override
                public void fail(String from) {
                    Toast.makeText(ChangePasswordActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            UtilMethods.INSTANCE.internetNotAvailableMessage(ChangePasswordActivity.this);
        }
    }
}