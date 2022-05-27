package com.njsv.doctorwala.assistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.njsv.doctorwala.MainActivity;
import com.njsv.doctorwala.R;
import com.njsv.doctorwala.order.OrderActivity;
import com.njsv.doctorwala.util.AppSharedPreferences;

public class CareServiceDetailActivity extends AppCompatActivity {

    private TextView hospitalName, nusrsingId, name, mobile, documentName, documentNumber, address, btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_service_detail);
        init();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Book Service Receipt");


        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CareServiceDetailActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finishAffinity();
            }
        });
    }

    private void init() {
        hospitalName = findViewById(R.id.hospital_name);
        nusrsingId = findViewById(R.id.care_order_id);
        name = findViewById(R.id.care_name);
        mobile = findViewById(R.id.care_mobile);
        documentName = findViewById(R.id.doc_name);
        documentNumber = findViewById(R.id.doc_no);
        address = findViewById(R.id.care_address);
        btnHome = findViewById(R.id.btn_home);
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