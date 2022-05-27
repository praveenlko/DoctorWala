package com.njsv.doctorwala.subcategory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.njsv.doctorwala.R;
import com.njsv.doctorwala.maincategory.CategoriesAdapter;
import com.njsv.doctorwala.model.CategoryModel;
import com.njsv.doctorwala.model.HosipitalModel;
import com.njsv.doctorwala.payment.PaymentActivity;
import com.njsv.doctorwala.retrofit.UtilMethods;
import com.njsv.doctorwala.retrofit.mCallBackResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njsv.doctorwala.slider.SliderImageData;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SubCategoryActivity extends AppCompatActivity {
    private RecyclerView subcategories;
    private CategoryModel category;
    private String categoryId,categoryName,nurshing;
    private int position;
    private Boolean labRadio;
    private RelativeLayout hospitalAds;
    private Button next;
    private Toolbar toolbar;
    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        inite();
        categoryId = getIntent().getStringExtra("categoryId");
        categoryName = getIntent().getStringExtra("categoryName");
        position = getIntent().getIntExtra("position",-1);
        nurshing = getIntent().getStringExtra("nurshing");
        labRadio = getIntent().getExtras().getBoolean("labRadio");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(categoryName);

        dialog = UtilMethods.INSTANCE.getProgressDialog(SubCategoryActivity.this);

        if (position == 0){
            subcategories.setVisibility(View.GONE);
            hospitalAds.setVisibility(View.VISIBLE);

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SubCategoryActivity.this, PaymentActivity.class);
                    intent.putExtra("categoryId",categoryId);
                    startActivity(intent);
                }
            });

        }

        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {
            UtilMethods.INSTANCE.subCategories(this, categoryId, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {
                    Type listType = new TypeToken<ArrayList<SubCategoriesModel>>() {
                    }.getType();
                    List<SubCategoriesModel> list = new Gson().fromJson(message, listType);
                    if (labRadio){
                        setLabRadio(list);
                    }else{
                        setCategory(list);
                    }
                }

                @Override
                public void fail(String from) {
                }
            });

        } else {

            UtilMethods.INSTANCE.internetNotAvailableMessage(this);
        }

    }

    private void setCategory(List<SubCategoriesModel> list) {
        SubCategoriesAdapter subCategoriesAdapter = new SubCategoriesAdapter(list, SubCategoryActivity.this,labRadio);
        subcategories.setLayoutManager(new GridLayoutManager(SubCategoryActivity.this,2));
        subcategories.setAdapter(subCategoriesAdapter);
        subCategoriesAdapter.notifyDataSetChanged();
    }

    private void setLabRadio(List<SubCategoriesModel> list) {
        SubCategoriesAdapter subCategoriesAdapter = new SubCategoriesAdapter(list, SubCategoryActivity.this,labRadio);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        subcategories.setLayoutManager(layoutManager);
        subcategories.setLayoutManager(new GridLayoutManager(SubCategoryActivity.this,2));
        subcategories.setAdapter(subCategoriesAdapter);
        subCategoriesAdapter.notifyDataSetChanged();
    }

    private void inite() {
        subcategories = findViewById(R.id.subcategories);
        hospitalAds = findViewById(R.id.hospital_ads);
        next = findViewById(R.id.next);

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