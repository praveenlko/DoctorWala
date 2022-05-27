package com.njsv.doctorwala.hosipital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njsv.doctorwala.R;
import com.njsv.doctorwala.model.HosipitalModel;
import com.njsv.doctorwala.retrofit.UtilMethods;
import com.njsv.doctorwala.retrofit.mCallBackResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HosipitalListActivity extends AppCompatActivity {

    private String categoryId,categoryName;
    private int position;
    private Toolbar toolbar;
    private RecyclerView hosipitalList;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosipital_list);

        categoryId = getIntent().getStringExtra("categoryId");
        categoryName = getIntent().getStringExtra("categoryName");
        position = getIntent().getIntExtra("position",-1);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(categoryName);

        hosipitalList = findViewById(R.id.hosipital_list);
        dialog = UtilMethods.INSTANCE.getProgressDialog(HosipitalListActivity.this);

        hosipitalList();

    }


    private void hosipitalList(){
        if (UtilMethods.INSTANCE.isNetworkAvialable(HosipitalListActivity.this)) {
            try {
                dialog.show();
                UtilMethods.INSTANCE.hosipitalList(HosipitalListActivity.this, new mCallBackResponse() {
                    @Override
                    public void success(String from, String message) {
                        Type listType = new TypeToken<ArrayList<HosipitalModel>>() {
                        }.getType();
                        List<HosipitalModel> list = new Gson().fromJson(message, listType);

                        setHosipitalList(list);

                    }
                    @Override
                    public void fail(String from) {
                    }
                });
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                dialog.dismiss();
            }
        } else {
            UtilMethods.INSTANCE.internetNotAvailableMessage(HosipitalListActivity.this);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setHosipitalList(List<HosipitalModel> list) {
        try {
            LinearLayoutManager layoutManager = new LinearLayoutManager(HosipitalListActivity.this);
            layoutManager.setOrientation(RecyclerView.VERTICAL);
            hosipitalList.setLayoutManager(layoutManager);
            HosipitalListAdapter hosipitalListAdapter = new HosipitalListAdapter(HosipitalListActivity.this, list);
            hosipitalList.setAdapter(hosipitalListAdapter);
            hosipitalListAdapter.notifyDataSetChanged();
        } catch (Exception e) {
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