package com.njsv.doctorwala.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.njsv.doctorwala.R;

public class SuccessPasswordActivity extends AppCompatActivity {
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_password);
        
        btnLogin = findViewById(R.id.login_now);
        
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuccessPasswordActivity.this,LoginActivity.class);
                startActivity(intent);
                finishAffinity();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(SuccessPasswordActivity.this,LoginActivity.class);
        startActivity(intent);
        finishAffinity();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}