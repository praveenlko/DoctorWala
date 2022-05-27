package com.njsv.doctorwala.services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.njsv.doctorwala.R;
import com.njsv.doctorwala.address.AddressActivity;
import com.njsv.doctorwala.charges.ServiceChargesActivity;
import com.njsv.doctorwala.payment.EmergencyAdapter;

public class ServicesActivity extends AppCompatActivity {

    String service,categoryId,subCategoryId,date,slotTiming;
    TextView note;
    private String subCategoryLink;
    private String subCategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        service =getIntent().getStringExtra("service");
        categoryId = getIntent().getStringExtra("categoryId");
        subCategoryId = getIntent().getStringExtra("subCategoryId");
        date = getIntent().getStringExtra("date");
        slotTiming = getIntent().getStringExtra("slotTiming");
        subCategoryName = getIntent().getStringExtra("subCategoryName");
        subCategoryLink = getIntent().getStringExtra("subCategoryLink");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(subCategoryName);

    }
    public void makerequest(View view) {
        try {
            Intent intent=new Intent(ServicesActivity.this, AddressActivity.class);
            intent.putExtra("service",service);
            intent.putExtra("categoryId",categoryId);
            intent.putExtra("subCategoryId",subCategoryId);
            intent.putExtra("date",date);
            intent.putExtra("slotTiming",slotTiming);
            intent.putExtra("subCategoryName", subCategoryName);
            intent.putExtra("subCategoryLink", subCategoryLink);
            startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}