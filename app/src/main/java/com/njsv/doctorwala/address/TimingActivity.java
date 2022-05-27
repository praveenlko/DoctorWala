package com.njsv.doctorwala.address;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njsv.doctorwala.MainActivity;
import com.njsv.doctorwala.R;
import com.njsv.doctorwala.order.OrderModel;
import com.njsv.doctorwala.order.PayOrderActivity;
import com.njsv.doctorwala.retrofit.UtilMethods;
import com.njsv.doctorwala.retrofit.mCallBackResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TimingActivity extends AppCompatActivity {
    String TAG = "Main";
    TextView countText,pleaseWait,weAre;
    private long timeLeftInMilisecond = 300000;   //  5 min

    private String orderId,landmark,pincode;
    private String orderRes = "";
    private CountDownTimer countDownTimer;
    private boolean FIRST = true;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timing);
        countText = findViewById(R.id.textView46);
        pleaseWait = findViewById(R.id.textView42);
        weAre = findViewById(R.id.textView45);
        progressBar = findViewById(R.id.progressBar2);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Service Response");

        landmark = getIntent().getStringExtra("landmark");
        pincode = getIntent().getStringExtra("pincode");

        orderId = getIntent().getStringExtra("orderId");
        tempOrderResponse(orderId);

        Intent intent = new Intent(this, BroadcastService.class);
        startService(intent);
        Log.i(TAG, "Started Service");

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Update GUI
//            updateGUI(intent);
            countDownTimer = new CountDownTimer(timeLeftInMilisecond, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftInMilisecond = millisUntilFinished;
                    if (FIRST) {
                        updateGUI(intent);
                        tempOrderResponse(orderId);
                    }
//                    Log.d("pankaj", "" + timeLeftInMilisecond / 1000);
                }

                @Override
                public void onFinish() {
                    FIRST = false;
                    pleaseWait.setText("Thanks for waiting");
                    weAre.setText("");
                    countText.setText("Sorry! No Response for this service \nPlease try again later...");
                    tempOrderDelete(orderId);
                }
            }.start();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(BroadcastService.COUNTDOWN_BR));
        Log.i(TAG, "Registered broadcast receiver");
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        Log.i(TAG, "Unregistered broadcast receiver");
    }

    @Override
    protected void onStop() {
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            // Receiver was probably already
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, BroadcastService.class));
//        tempOrderDelete(orderId);
        Log.i(TAG, "Stopped service");
        super.onDestroy();
    }

    private void updateGUI(Intent intent) {
        if (intent.getExtras() != null) {

            int minutes = (int) timeLeftInMilisecond / 60000;
            int second = (int) timeLeftInMilisecond % 60000 / 1000;

            String timeLeft;
            timeLeft = "" + minutes;
            timeLeft += ":";

            if (second < 10) timeLeft += "0";
            timeLeft += second;

            countText.setText(timeLeft);

            SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);

            sharedPreferences.edit().putLong("time", timeLeftInMilisecond).apply();
        }
    }

    private void tempOrderResponse(String orderId) {
        if (UtilMethods.INSTANCE.isNetworkAvialable(TimingActivity.this)) {
            UtilMethods.INSTANCE.tempResponse(TimingActivity.this, orderId, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {
                    Type listType = new TypeToken<ArrayList<TempOrderModel>>() {
                    }.getType();
                    List<TempOrderModel> list = new Gson().fromJson(message, listType);

                  try {
                      orderRes = list.get(0).getStatus();
                      String subCategory = list.get(0).getSname();

                      switch (orderRes) {
                          case "Accepted":
                              countDownTimer.cancel();
                              Intent intent = new Intent(TimingActivity.this, PayOrderActivity.class);
                              intent.putExtra("orderId",orderId);
                              intent.putExtra("landmark",landmark);
                              intent.putExtra("pincode",pincode);
                              startActivity(intent);
                              finishAffinity();
                              stopService(new Intent(getApplicationContext(), BroadcastService.class));
                              FIRST = false;
                              break;

                          case "Cancelled":
                              stopService(new Intent(getApplicationContext(), BroadcastService.class));
                              pleaseWait.setText("Thanks for waiting");
                              weAre.setText("");
                              countText.setText("Sorry! "+subCategory+" service is not available");
                              countText.setTextSize(15);
                              countText.setPadding(16,8,16,8);
                              progressBar.setVisibility(View.GONE);
                              tempOrderDelete(orderId);
                              FIRST = false;
                              break;

                          case "Pending":
//                            countText.setText("your service is not available");
//                            tempOrderDelete(orderId);
//                            FIRST = false;
                              break;
                          default:
                              break;
                      }
                  }catch (Exception e){
                      e.printStackTrace();
                  }
                }

                @Override
                public void fail(String from) {
                    tempOrderDelete(orderId);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(TimingActivity.this, "Something went to wrong", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            UtilMethods.INSTANCE.internetNotAvailableMessage(TimingActivity.this);
        }
    }

    private void tempOrderDelete(String orderId) {
        if (UtilMethods.INSTANCE.isNetworkAvialable(TimingActivity.this)) {
            UtilMethods.INSTANCE.tempDelete(TimingActivity.this, orderId, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {
//                    Type listType = new TypeToken<ArrayList<OrderModel>>() {
//                    }.getType();
//                    List<OrderModel> list = new Gson().fromJson(message, listType);

//                    if (list.get(0).getMsg().equals("success")) {
//
//                    }
                    stopService(new Intent(getApplicationContext(), BroadcastService.class));
                    FIRST = false;
                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void fail(String from) {
                    Toast.makeText(TimingActivity.this, "Something went wrong Delete", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            UtilMethods.INSTANCE.internetNotAvailableMessage(TimingActivity.this);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopService(new Intent(this, BroadcastService.class));
        FIRST =  false;
        tempOrderDelete(orderId);
        Intent intent = new Intent(TimingActivity.this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            stopService(new Intent(this, BroadcastService.class));
            FIRST =  false;
            tempOrderDelete(orderId);
            Intent intent = new Intent(TimingActivity.this, MainActivity.class);
            startActivity(intent);
            finishAffinity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}