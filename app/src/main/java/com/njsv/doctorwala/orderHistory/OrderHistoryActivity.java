package com.njsv.doctorwala.orderHistory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njsv.doctorwala.R;
import com.njsv.doctorwala.fragment.BookingModel;
import com.njsv.doctorwala.order.OrderActivity;
import com.njsv.doctorwala.retrofit.UtilMethods;
import com.njsv.doctorwala.retrofit.mCallBackResponse;
import com.njsv.doctorwala.subcategory.SubCategoriesModel;
import com.njsv.doctorwala.util.AppSharedPreferences;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    private TextView orderId,fullName,mobile,address,orderDate,paymentMode,aadhar,totalPriceAmt,documentType,nameService,tvPaid;
    private int position;
    private String subCatName;
    private String subCatid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        init();

        position = getIntent().getIntExtra("position",-1);
        subCatid = getIntent().getStringExtra("subCatId");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Order Detail");
        getSubcategoryName(subCatid);

        bookingShow(position);
//        nameService.setText("subCatName");
        Log.d("orders","subCatId = "+subCatid+"\nsubCatName = "+subCatName);




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
        nameService = findViewById(R.id.name_service);
        tvPaid = findViewById(R.id.tv_paid);

    }

    private void getSubcategoryName(String id){
        if (UtilMethods.INSTANCE.isNetworkAvialable(OrderHistoryActivity.this)) {
            UtilMethods.INSTANCE.getsubCategoryName(OrderHistoryActivity.this,id, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {
                    Type listType = new TypeToken<ArrayList<SubCategoriesModel>>() {
                    }.getType();
                    List<SubCategoriesModel> list = new Gson().fromJson(message, listType);
                    nameService.setText(list.get(0).getName());

                }
                @Override
                public void fail(String from) {
                    Toast.makeText(OrderHistoryActivity.this, "error =>" +from, Toast.LENGTH_SHORT).show();
                }
            });

        } else {

            UtilMethods.INSTANCE.internetNotAvailableMessage(OrderHistoryActivity.this);
        }
    }

    public void bookingShow(int position) {
        AppSharedPreferences preferences = new AppSharedPreferences(getApplication());
        String id = preferences.getLoginUserLoginId();
        if (UtilMethods.INSTANCE.isNetworkAvialable(OrderHistoryActivity.this)) {
            UtilMethods.INSTANCE.showBooking(OrderHistoryActivity.this, id, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {
                    Type listType = new TypeToken<ArrayList<BookingModel>>() {
                    }.getType();
                    List<BookingModel> list = new Gson().fromJson(message, listType);
                    Log.d("orderDetails=",list.toString());

                    orderId.setText(list.get(position).getOrderId());
                    fullName.setText(preferences.getLoginUserName());
                    mobile.setText(preferences.getLoginMobile());
                    address.setText(list.get(position).getAddress());
                    orderDate.setText(list.get(position).getOrderdate());
                    paymentMode.setText(list.get(position).getPaymentmethod());
                    documentType.setText(list.get(position).getDocumentType());
                    aadhar.setText(list.get(position).getAadharNo());
                    totalPriceAmt.setText("Total Amount : "+list.get(position).getTotalamount());

                    if (list.get(position).getPaymentmethod().equals("ONLINE")){
                        tvPaid.setTextColor(Color.rgb(17, 161, 12));
                        tvPaid.setText("Paid\nYour bill has been paid.");
                    }else if(list.get(position).getPaymentmethod().equals("COD")){
                        tvPaid.setTextColor(Color.rgb(0, 0, 0));
                        tvPaid.setText("Cash on Delivery");
                    }else{
                        Toast.makeText(OrderHistoryActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void fail(String from) {

                }
            });

        } else {

            UtilMethods.INSTANCE.internetNotAvailableMessage(OrderHistoryActivity.this);
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