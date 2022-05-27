package com.njsv.doctorwala.charges;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njsv.doctorwala.MainActivity;
import com.njsv.doctorwala.R;
import com.njsv.doctorwala.address.AddressActivity;
import com.njsv.doctorwala.address.TimingActivity;
import com.njsv.doctorwala.fragment.WalletModel;
import com.njsv.doctorwala.order.OrderActivity;
import com.njsv.doctorwala.retrofit.UtilMethods;
import com.njsv.doctorwala.retrofit.mCallBackResponse;
import com.njsv.doctorwala.util.AppSharedPreferences;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ServiceChargesActivity extends AppCompatActivity {

    private TextView charges, convenience, gst, totalPrice;
    private String priceTotal = "0";
    private String service, categoryId, subCategoryId, date, slotTiming, charge, convenCharge;
    private String subCategoryLink;
    private String subCategoryName;
    private Button btnProceed;
    private AppSharedPreferences preferences;
    private String orderIds;
    private String adddress, landmark, pincode, aadharCard;
    private String customerId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_charges);

        preferences = new AppSharedPreferences(getApplication());
        customerId = preferences.getLoginUserLoginId();

        service = getIntent().getStringExtra("service");
        categoryId = getIntent().getStringExtra("categoryId");
        subCategoryId = getIntent().getStringExtra("subCategoryId");
        date = getIntent().getStringExtra("date");
        slotTiming = getIntent().getStringExtra("slotTiming");
        subCategoryName = getIntent().getStringExtra("subCategoryName");
        subCategoryLink = getIntent().getStringExtra("subCategoryLink");

        adddress = getIntent().getStringExtra("adddress");
        landmark = getIntent().getStringExtra("landmark");
        pincode = getIntent().getStringExtra("pincode");
        aadharCard = getIntent().getStringExtra("aadhar");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Service Charge");

        orderIds = "ORD"+String.valueOf(Calendar.getInstance().getTimeInMillis());
        Checkout.preload(ServiceChargesActivity.this);

        init();

        if (UtilMethods.INSTANCE.isNetworkAvialable(getApplicationContext())) {

            UtilMethods.INSTANCE.charges(ServiceChargesActivity.this, categoryId, subCategoryId, service, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {

                    Type listType = new TypeToken<ArrayList<ChargesModel>>() {
                    }.getType();
                    List<ChargesModel> list = new Gson().fromJson(message, listType);

                    charge = list.get(0).getCharge();
                    convenCharge = list.get(0).getConCharge();
                    charges.setText(charge + "/- ");
                    convenience.setText("" + convenCharge + "/-");
                    gst.setText("" + list.get(0).getGst() + "%");
                    totalPrice.setText("" + list.get(0).getTotal() + "/-");
                    priceTotal = list.get(0).getTotal();

                }

                @Override
                public void fail(String from) {

                }
            });
        } else {

            UtilMethods.INSTANCE.internetNotAvailableMessage(ServiceChargesActivity.this);
        }

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tempPlaceOrder(orderIds, subCategoryId, categoryId, customerId, service, aadharCard, subCategoryLink, subCategoryName, charge, "0", priceTotal, slotTiming, convenCharge, date, landmark, adddress, pincode);

            }
        });
    }

    public void init() {
        charges = findViewById(R.id.charges);
        convenience = findViewById(R.id.conven);
        gst = findViewById(R.id.gst);
        totalPrice = findViewById(R.id.total_price);
        btnProceed = findViewById(R.id.btnProceed);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void tempPlaceOrder(String orderid, String subCatId, String catId, String id, String service, String aadharNo, String thumbnail, String name, String amount, String discount, String totalamount, String schedule, String deliverycharge, String orderdate, String landmark, String address, String pincode) {

        try {
            if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {
                UtilMethods.INSTANCE.tempOrderPlace(this, orderid, subCatId, catId, id, service, aadharNo, thumbnail, name, amount, discount, totalamount, schedule, deliverycharge, orderdate, landmark, address, pincode, new mCallBackResponse() {
                    @Override
                    public void success(String from, String message) {
                        try {
                            Intent intent = new Intent(ServiceChargesActivity.this, TimingActivity.class);
                            intent.putExtra("orderId",orderid);
                            intent.putExtra("landmark",landmark);
                            intent.putExtra("pincode",pincode);
                            startActivity(intent);
                            finishAffinity();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void fail(String from) {
                        Toast.makeText(ServiceChargesActivity.this, "error =>" + from, Toast.LENGTH_SHORT).show();
                    }
                });

            } else {

                UtilMethods.INSTANCE.internetNotAvailableMessage(this);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}