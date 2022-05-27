package com.njsv.doctorwala.assistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.njsv.doctorwala.R;
import com.njsv.doctorwala.address.AddressActivity;
import com.njsv.doctorwala.util.AppSharedPreferences;

import java.util.Calendar;

public class AssistantFormActivity extends AppCompatActivity {

    private EditText assistName,assistDes;
    private Button btnSubmit;
    private String hospitalNamel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant_form);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Patient Information");

        hospitalNamel = getIntent().getStringExtra("hospital");

        assistName = findViewById(R.id.assist_name);
        assistDes = findViewById(R.id.message_des);
        btnSubmit = findViewById(R.id.btn_assist_submit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(assistName.getText())){
                    if (!TextUtils.isEmpty(assistDes.getText())){

                        String name = assistName.getText().toString();
                        String reason = assistDes.getText().toString();

                        Intent intent = new Intent(AssistantFormActivity.this, AddressActivity.class);
                        intent.putExtra("name",name);
                        intent.putExtra("reason",reason);
                        intent.putExtra("hospitalNamel",hospitalNamel);
                        intent.putExtra("CARE_TAKER",true);
                        startActivity(intent);

                    }else{
                        assistDes.requestFocus();
                        assistDes.setError("Please fill reason");
                    }
                }else{
                    assistName.requestFocus();
                    assistName.setError("Please fill name");
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}