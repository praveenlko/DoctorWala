package com.njsv.doctorwala.orderHistory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njsv.doctorwala.MainActivity;
import com.njsv.doctorwala.R;
import com.njsv.doctorwala.fragment.PharmacyOrderModel;
import com.njsv.doctorwala.order.OrderActivity;
import com.njsv.doctorwala.retrofit.UtilMethods;
import com.njsv.doctorwala.retrofit.mCallBackResponse;
import com.njsv.doctorwala.util.AppSharedPreferences;
import com.njsv.doctorwala.util.ApplicationConstants;
import com.njsv.doctorwala.util.MyGlide;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DescriptionShowActivity extends AppCompatActivity {

    private TextView btnDone;
    private ImageView imageView;
    private TextView address, message, name, mobile, desc, pharmacyCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_show);

        btnDone = findViewById(R.id.btn_done);
        address = findViewById(R.id.pharmacy_address);
        message = findViewById(R.id.textView7);
        name = findViewById(R.id.pharmacy_name);
        mobile = findViewById(R.id.pharmacy_mobile);
        desc = findViewById(R.id.pharmacy_desc);
        pharmacyCancel = findViewById(R.id.pharmacy_cancel);
        imageView = findViewById(R.id.imageView6);

        AppSharedPreferences preferences = new AppSharedPreferences(getApplication());
        String contact = preferences.getLoginMobile();
        showPharmacyOrder(contact);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DescriptionShowActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finishAffinity();

            }
        });

    }

    private void showPharmacyOrder(String mob) {
        if (UtilMethods.INSTANCE.isNetworkAvialable(DescriptionShowActivity.this)) {
            UtilMethods.INSTANCE.showPharmacyOrder(DescriptionShowActivity.this, mob, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {
                    Log.d("order pharmacy", "enter => "+message);
                    Type listType = new TypeToken<ArrayList<PharmacyOrderModel>>() {
                    }.getType();
                    List<PharmacyOrderModel> pharmacyModelList = new Gson().fromJson(message, listType);

                    name.setText(pharmacyModelList.get(0).getName());
                    mobile.setText(pharmacyModelList.get(0).getMobile());
                    address.setText(pharmacyModelList.get(0).getAddress());
                    desc.setText(pharmacyModelList.get(0).getMsg());
                    String date = pharmacyModelList.get(0).getDate();
                    String id = pharmacyModelList.get(0).getPharOrderId();
                    MyGlide.with(DescriptionShowActivity.this, ApplicationConstants.INSTANCE.PROFILE_VIEW_IMAGES + pharmacyModelList.get(0).getPharmacyImg(), imageView);


                }
                @Override
                public void fail(String from) {


                }
            });

        } else {
            UtilMethods.INSTANCE.internetNotAvailableMessage(DescriptionShowActivity.this);
        }
    }
}