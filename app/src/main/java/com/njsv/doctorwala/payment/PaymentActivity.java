package com.njsv.doctorwala.payment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njsv.doctorwala.R;
import com.njsv.doctorwala.charges.ChargesModel;
import com.njsv.doctorwala.charges.ServiceChargesActivity;
import com.njsv.doctorwala.retrofit.UtilMethods;
import com.njsv.doctorwala.retrofit.mCallBackResponse;
import com.njsv.doctorwala.services.ServicesActivity;
import com.njsv.doctorwala.subcategory.SubCategoryActivity;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PaymentActivity extends AppCompatActivity implements EmergencyAdapter.OnTimeSlotSelected {

    private Button btnContinue;
    private RadioGroup selectRadio;
    private String select = null, slotTiming = null;
    private EmergencyAdapter adapter;
    private List<TimeSlotModel> emergencyModelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DatePicker datePicker;
    private String datePic;
    private String subCategoryLink,subCategoryName;
    private String date,month,year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        String categoryId = getIntent().getStringExtra("categoryId");
        String subCategoryId = getIntent().getStringExtra("subCategoryId");
        subCategoryName = getIntent().getStringExtra("subCategoryName");
        subCategoryLink = getIntent().getStringExtra("subCategoryLink");
        init();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(subCategoryName);

        Calendar calendar = Calendar.getInstance();
        datePicker.setMinDate(System.currentTimeMillis() - 1000);

        date = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        month = String.valueOf(calendar.get(Calendar.MONTH));
        year = String.valueOf(calendar.get(Calendar.YEAR));

        datePic = date + "-" + month + "-" + year;

        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String curDate = SimpleDateFormat.getDateInstance().format(calendar.getTime());
                Log.d("checkDate", "" + curDate);

                date = String.valueOf(datePicker.getDayOfMonth());
                month = String.valueOf(datePicker.getMonth() + 1);
                year = String.valueOf(datePicker.getYear());

                datePic = date + "-" + month + "-" + year;
                Log.d("seDae", "" + datePic);
            }
        });

        if (datePic == null){

        }

/*
//        String day  = String.valueOf(datePicker.getDayOfMonth());
//        String month  = String.valueOf(datePicker.getMonth() + 1);
//        String year  = String.valueOf(datePicker.getYear());
//
//        String date = day+"-"+month+"-"+year;
//        Log.d("seDae",""+date);
*/

//        adapter = new EmergencyAdapter(emergencyModelList, new EmergencyAdapter.OnTimeSlotSelected() {
//            @Override
//            public void onTimeSlot(String slot, int position) {
//                slotTiming = slot;
//            }
//        });

        selectRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.btn_emergency:

                        select = "emergency";
                        timeSlots(select);
                        break;

                    case R.id.btn_non_emergency:

                        select = "non_emergency";
                        timeSlots(select);
                        break;

                    default:
                        break;
                }
            }
        });


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (select != null) {
                    if (slotTiming != null) {
                        if (datePic != null) {
                            Intent intent = new Intent(PaymentActivity.this, ServicesActivity.class);
                            intent.putExtra("service", select);
                            intent.putExtra("categoryId", categoryId);
                            intent.putExtra("subCategoryId", subCategoryId);
                            intent.putExtra("date", datePic);
                            intent.putExtra("slotTiming", slotTiming);
                            intent.putExtra("subCategoryName", subCategoryName);
                            intent.putExtra("subCategoryLink", subCategoryLink);
                            startActivity(intent);
                        }else{
                            Toast.makeText(PaymentActivity.this, "Please select Date", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(PaymentActivity.this, "Please select Timing", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PaymentActivity.this, "Please select Service", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void init() {
        btnContinue = findViewById(R.id.btn_continue);
        recyclerView = findViewById(R.id.emergency);
        selectRadio = findViewById(R.id.select);
        datePicker = findViewById(R.id.date_picker);

    }


    private void timeSlots(String time_zone) {
        if (UtilMethods.INSTANCE.isNetworkAvialable(getApplicationContext())) {


            UtilMethods.INSTANCE.timeSlot(PaymentActivity.this, time_zone, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {

                    Type listType = new TypeToken<ArrayList<TimeSlotModel>>() {
                    }.getType();
                    List<TimeSlotModel> list = new Gson().fromJson(message, listType);

                    setList(list);

                }

                @Override
                public void fail(String from) {

                }
            });

        } else {

            UtilMethods.INSTANCE.internetNotAvailableMessage(PaymentActivity.this);
        }
    }

    private void setList(List<TimeSlotModel> list) {


        emergencyModelList.clear();
        recyclerView.setVisibility(View.VISIBLE);
//        adapter = new EmergencyAdapter(list,onTimeSlotSelected);
        adapter = new EmergencyAdapter(list, new EmergencyAdapter.OnTimeSlotSelected() {
            @Override
            public void onTimeSlot(String slot, int position) {
                slotTiming = slot;
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(PaymentActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onTimeSlot(String slot, int position) {
        slotTiming = slot;

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