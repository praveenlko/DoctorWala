package com.njsv.doctorwala.order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njsv.doctorwala.MainActivity;
import com.njsv.doctorwala.R;
import com.njsv.doctorwala.address.AddressActivity;
import com.njsv.doctorwala.fragment.BookingModel;
import com.njsv.doctorwala.retrofit.UtilMethods;
import com.njsv.doctorwala.retrofit.mCallBackResponse;
import com.njsv.doctorwala.util.AppSharedPreferences;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {

    private TextView orderId,fullName,mobile,address,orderDate,paymentMode,aadhar,totalPriceAmt,documentType,btnDone,tvPaid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipt);

        init();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Book Service Receipt");

        bookingShow();

        btnDone = findViewById(R.id.btn_done);
//        new SweetAlertDialog(OrderActivity.this,SweetAlertDialog.SUCCESS_TYPE)
//                .setTitleText("Success")
//                .setContentText("Service Placed Successfully......")
//                .show();
        
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finishAffinity();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(OrderActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finishAffinity();
    }

    private void init(){

        orderId = findViewById(R.id.order_id);
        fullName = findViewById(R.id.full_name);
        mobile = findViewById(R.id.mobile);
        address = findViewById(R.id.address);
        orderDate = findViewById(R.id.order_date);
        paymentMode = findViewById(R.id.payment_mode);
        aadhar = findViewById(R.id.aadhar);
        totalPriceAmt = findViewById(R.id.total_price_amt);
        documentType = findViewById(R.id.document_type);
        tvPaid = findViewById(R.id.tv_paid);

    }

    public void bookingShow() {
        AppSharedPreferences preferences = new AppSharedPreferences(getApplication());
        String id = preferences.getLoginUserLoginId();
        if (UtilMethods.INSTANCE.isNetworkAvialable(OrderActivity.this)) {
            UtilMethods.INSTANCE.showBooking(OrderActivity.this, id, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {
                    Type listType = new TypeToken<ArrayList<BookingModel>>() {
                    }.getType();
                    List<BookingModel> list = new Gson().fromJson(message, listType);
//                    Toast.makeText(OrderActivity.this, "Successfully "+list, Toast.LENGTH_SHORT).show();
                    Log.d("orderDetails=",list.toString());


                    orderId.setText(list.get(0).getOrderId());
                    fullName.setText(preferences.getLoginUserName());
                    mobile.setText(preferences.getLoginMobile());
                    address.setText(list.get(0).getAddress());
                    orderDate.setText(list.get(0).getOrderdate());
                    paymentMode.setText(list.get(0).getPaymentmethod());
                    documentType.setText(list.get(0).getDocumentType());
                    aadhar.setText(list.get(0).getAadharNo());
                    totalPriceAmt.setText("Total Amount : "+list.get(0).getTotalamount()+"/-");

                    if (list.get(0).getPaymentmethod().equals("ONLINE")){
                        tvPaid.setText("Paid\nYour bill has been paid.");
                    }else if(list.get(0).getPaymentmethod().equals("COD")){
                        tvPaid.setText("Unpaid");
                    }else{
                        Toast.makeText(OrderActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void fail(String from) {

                }
            });

        } else {

            UtilMethods.INSTANCE.internetNotAvailableMessage(OrderActivity.this);
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