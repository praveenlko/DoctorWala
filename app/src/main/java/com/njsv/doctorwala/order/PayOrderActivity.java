package com.njsv.doctorwala.order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njsv.doctorwala.MainActivity;
import com.njsv.doctorwala.R;
import com.njsv.doctorwala.address.BroadcastService;
import com.njsv.doctorwala.address.TempOrderModel;
import com.njsv.doctorwala.order.PayOrderActivity;
import com.njsv.doctorwala.charges.ServiceChargesActivity;
import com.njsv.doctorwala.order.PayOrderActivity;
import com.njsv.doctorwala.fragment.WalletModel;
import com.njsv.doctorwala.retrofit.UtilMethods;
import com.njsv.doctorwala.retrofit.mCallBackResponse;
import com.njsv.doctorwala.util.AppSharedPreferences;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PayOrderActivity extends AppCompatActivity implements PaymentResultListener {

    private Button btnPay,btnReedeme;
    private RadioGroup selectRadio;
    private String paymentmethod = "";
    private Boolean boolCod = false, boolOnline = false;
    private String priceTotal = "";
    private AppSharedPreferences preferences;
    private String orderIds,subCategoryId,categoryId,customerId,service, aadharCard, subCategoryLink,subCategoryName,charge, slotTiming,convenCharge,
            date,landmark,adddress,pincode;
    private TextView walletAmt;
    private String totalWallet;
    private EditText enterPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_order);
        init();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Payment");

        orderIds = getIntent().getStringExtra("orderId");
        landmark = getIntent().getStringExtra("landmark");
        pincode = getIntent().getStringExtra("pincode");
        tempOrderResponse(orderIds);

        Checkout.preload(PayOrderActivity.this);

        preferences = new AppSharedPreferences(getApplication());
        customerId = preferences.getLoginUserLoginId();

        selectRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.cod_radio:

                        paymentmethod = "COD";
                        boolCod = true;
                        boolOnline = false;
                        break;

                    case R.id.online_radio:
                        paymentmethod = "ONLINE";
                        boolCod = false;
                        boolOnline = true;
                        break;

                    default:
                        break;
                }
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!paymentmethod.equals("")){
                    if (paymentmethod != null) {
                        if (boolCod) {
                            try {
                                placeOrder(orderIds, subCategoryId, categoryId, customerId, service, aadharCard, subCategoryLink, subCategoryName, charge, "0", priceTotal, slotTiming, convenCharge, date, paymentmethod, landmark, adddress, pincode);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (boolOnline) {
                            startPayment();
                        }
                    } else {
                        Toast.makeText(PayOrderActivity.this, "Please select Payment Mode", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(PayOrderActivity.this, "Please select Payment Mode", Toast.LENGTH_SHORT).show();
                }
            }
        });

        enterPoints.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (!enterPoints.getText().toString().isEmpty()) {
                        float pts = Float.parseFloat(enterPoints.getText().toString());
                        if (pts >= 1.0) {
                            if (pts <= Float.parseFloat(totalWallet)) {
                                btnReedeme.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        redeemPoints(totalWallet, customerId, String.valueOf(pts));
                                    }
                                });
                            } else {
                                enterPoints.setError("your points are not valid");
                            }
                        } else {
                            enterPoints.setError("You are entered wrong points");
                        }
                    } else {
                        enterPoints.setError("Please enter points");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void init(){
        btnPay = findViewById(R.id.btn_pay);
        selectRadio = findViewById(R.id.select_option);
        walletAmt = findViewById(R.id.textView41);
        enterPoints = findViewById(R.id.et_enter_points);
        btnReedeme = findViewById(R.id.btn_reedeme);
    }

    public void startPayment() {
//        checkout.setKeyID("rzp_test_o8PvbyI0vmNKl8");
//        checkout.setKeyID(getString(R.string.rozor_key_id));
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
//        checkout.setKeyID("CXYmzrr48IhGlN37YZgObaoy");

        /**
         * Set your logo here
         */
        checkout.setImage(R.mipmap.ic_launcher);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        int dAmount = Integer.parseInt("0" + priceTotal);
        dAmount *= 100;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", "DoctorWala");
//            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("order_id", orderIds);//from response of step 3.
            options.put("theme.color", "#000000");
            options.put("currency", "INR");
//            options.put("amount", dAmount);//pass amount in currency subunits
            options.put("amount", dAmount);//pass amount in currency subunits
//            options.put("prefill.email", "praveenunnav@gmail.com");
            options.put("prefill.contact", preferences.getLoginMobile());
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch (Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onPaymentSuccess(String s) {
        if (!s.isEmpty()) {
            try {
//                placeOrder(orderIds, subCategoryId, categoryId, customerId, service, aadharCard, subCategoryLink, subCategoryName, charge, "0", priceTotal, slotTiming, convenCharge, date, paymentmethod, landmark, adddress, pincode);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Failed and cause : " + s, Toast.LENGTH_SHORT).show();
    }

    private void placeOrder(String orderid, String subCatId, String catId, String id, String service, String aadharNo, String thumbnail, String name, String amount, String discount, String totalamount, String schedule, String deliverycharge, String orderdate, String paymentmethod, String landmark, String address, String pincode) {

        try {
            if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {
                UtilMethods.INSTANCE.orderPlace(this, orderid, subCatId, catId, id, service, aadharNo, thumbnail, name, amount, discount, totalamount, schedule, deliverycharge, orderdate, paymentmethod, landmark, address, pincode, new mCallBackResponse() {
                    @Override
                    public void success(String from, String message) {
                        try {
                            addWallet(totalamount, id);
                            String msg = "Your " + subCategoryName + " service placed successfully";
                            getNotification(msg);
                            Intent intent = new Intent(PayOrderActivity.this, OrderActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            tempOrderDelete(orderid);
                            finishAffinity();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void fail(String from) {
                        Toast.makeText(PayOrderActivity.this, "error =>" + from, Toast.LENGTH_SHORT).show();
                    }
                });

            } else {

                UtilMethods.INSTANCE.internetNotAvailableMessage(this);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getNotification(String notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("MyNotification", "MyNotification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MyNotification")
                .setContentTitle("Service").setSmallIcon(R.drawable.logo_white).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo_white))
                .setAutoCancel(true).setStyle(new NotificationCompat.BigTextStyle().bigText(notification)).setAutoCancel(true);
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(999, builder.build());
    }

    private void addWallet(String amt, String userid) {
        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {
            UtilMethods.INSTANCE.addWallet(this, amt, userid, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {
//                    Toast.makeText(PayOrderActivity.this, "ADD POINT" +from, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void fail(String from) {
                    Toast.makeText(PayOrderActivity.this, "error =>" + from, Toast.LENGTH_SHORT).show();
                }
            });

        } else {

            UtilMethods.INSTANCE.internetNotAvailableMessage(this);
        }
    }
//
//    private void viewWallet(String id) {
//        if (UtilMethods.INSTANCE.isNetworkAvialable(PayOrderActivity.this)) {
//            UtilMethods.INSTANCE.viewWallet(PayOrderActivity.this, id, new mCallBackResponse() {
//                @Override
//                public void success(String from, String message) {
//                    Type listType = new TypeToken<ArrayList<WalletModel>>() {
//                    }.getType();
//                    List<WalletModel> list = new Gson().fromJson(message, listType);
//                    try {
//                        totalWallet = list.get(0).getAmount();
//                        walletAmt.setText("Your Points : " + list.get(0).getAmount());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//
//                }
//
//                @Override
//                public void fail(String from) {
//                }
//            });
//        } else {
//            UtilMethods.INSTANCE.internetNotAvailableMessage(PayOrderActivity.this);
//        }
//    }
//
//    private void redeemPoints(String points, String userId, String redeemPoint) {
//        if (UtilMethods.INSTANCE.isNetworkAvialable(PayOrderActivity.this)) {
//            UtilMethods.INSTANCE.redeemPoint(PayOrderActivity.this, points, userId, redeemPoint, new mCallBackResponse() {
//                @Override
//                public void success(String from, String message) {
//
//                }
//
//                @Override
//                public void fail(String from) {
//                    Toast.makeText(PayOrderActivity.this, "error =>" + from, Toast.LENGTH_SHORT).show();
//                }
//            });
//        } else {
//            UtilMethods.INSTANCE.internetNotAvailableMessage(PayOrderActivity.this);
//        }
//    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            tempOrderDelete(orderIds);
            Intent intent = new Intent(PayOrderActivity.this, MainActivity.class);
            startActivity(intent);
            finishAffinity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void tempOrderResponse(String orderId) {
        if (UtilMethods.INSTANCE.isNetworkAvialable(PayOrderActivity.this)) {
            UtilMethods.INSTANCE.tempResponse(PayOrderActivity.this, orderId, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {
                    Type listType = new TypeToken<ArrayList<TempOrderModel>>() {
                    }.getType();
                    List<TempOrderModel> list = new Gson().fromJson(message, listType);

                    subCategoryId = list.get(0).getSubCatId();
                    categoryId = list.get(0).getCatId();
                    customerId = list.get(0).getCustomerId();
                    service = list.get(0).getService();
                    aadharCard = list.get(0).getAadharNo();
                    subCategoryLink = list.get(0).getThumbnail();
                    subCategoryName = list.get(0).getSname();
                    charge = list.get(0).getAmount();
                    priceTotal = list.get(0).getTotalamount();
                    slotTiming = list.get(0).getSchedule();
                    convenCharge = list.get(0).getDeliverycharge();
                    date = list.get(0).getuDate();
                    adddress = list.get(0).getAddress();

                }

                @Override
                public void fail(String from) {
                    Toast.makeText(PayOrderActivity.this, "Something went to wrong", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            UtilMethods.INSTANCE.internetNotAvailableMessage(PayOrderActivity.this);
        }
    }

    private void redeemPoints(String points, String userId, String redeemPoint) {
        if (UtilMethods.INSTANCE.isNetworkAvialable(PayOrderActivity.this)) {
            UtilMethods.INSTANCE.redeemPoint(PayOrderActivity.this, points, userId, redeemPoint, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {

                }

                @Override
                public void fail(String from) {
                    Toast.makeText(PayOrderActivity.this, "error =>" + from, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            UtilMethods.INSTANCE.internetNotAvailableMessage(PayOrderActivity.this);
        }
    }

    private void tempOrderDelete(String orderId) {
        if (UtilMethods.INSTANCE.isNetworkAvialable(PayOrderActivity.this)) {
            UtilMethods.INSTANCE.tempDelete(PayOrderActivity.this, orderId, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {


                }

                @Override
                public void fail(String from) {
                    Toast.makeText(PayOrderActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            UtilMethods.INSTANCE.internetNotAvailableMessage(PayOrderActivity.this);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        tempOrderDelete(orderIds);
        Intent intent = new Intent(PayOrderActivity.this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tempOrderDelete(orderIds);
    }
}