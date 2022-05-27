package com.njsv.doctorwala.fragment;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njsv.doctorwala.retrofit.UtilMethods;
import com.njsv.doctorwala.retrofit.mCallBackResponse;
import com.njsv.doctorwala.util.AppSharedPreferences;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookViewModel extends ViewModel {

    private MutableLiveData<String> bookingModelList;
    private MutableLiveData<String> pharmacyList;
    private MutableLiveData<String> pharmacyCancel;
    private MutableLiveData<String> nursingList;

    public LiveData<String> getBooking(Context context,String id) {
        if (bookingModelList == null) {
            bookingModelList = new MutableLiveData<>();
            bookingShow(context,id);
        }
        return bookingModelList;
    }

    public LiveData<String> getPharmacy(Context context,String mobile) {
        if (pharmacyList == null) {
            pharmacyList = new MutableLiveData<>();
            showPharmacyOrder(context,mobile);
        }
        return pharmacyList;
    }

    public LiveData<String> getPharmacyCancel(Context context,String id,String reason) {
        if (pharmacyCancel == null) {
            pharmacyCancel = new MutableLiveData<>();
            pharmacyCancel(context,id,reason);
        }
        return pharmacyList;
    }

    public LiveData<String> getNursing(Context context,String mobile) {
        if (nursingList == null) {
            nursingList = new MutableLiveData<>();
            showNursingOrder(context,mobile);
        }
        return nursingList;
    }

    public void bookingShow(Context context,String id) {
        if (UtilMethods.INSTANCE.isNetworkAvialable(context)) {
            UtilMethods.INSTANCE.showBooking(context, id, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {
                    bookingModelList.postValue(message);
                }
                @Override
                public void fail(String from) {
                    bookingModelList.postValue(from);
                }
            });
        } else {
            UtilMethods.INSTANCE.internetNotAvailableMessage(context);
        }
    }

    private void showPharmacyOrder(Context context,String mobile) {
        if (UtilMethods.INSTANCE.isNetworkAvialable(context)) {
            UtilMethods.INSTANCE.showPharmacyOrder(context, mobile, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {
                    pharmacyList.postValue(message);
                }
                @Override
                public void fail(String from) {
                 pharmacyList.postValue(from);
                }
            });

        } else {
            UtilMethods.INSTANCE.internetNotAvailableMessage(context);
        }
    }

    private void pharmacyCancel(Context context,String orderid, String reason) {
        if (UtilMethods.INSTANCE.isNetworkAvialable(context)) {
            UtilMethods.INSTANCE.pharmacyCancel(context, orderid, reason, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {
                    pharmacyCancel.postValue(message);
                }

                @Override
                public void fail(String from) {
                    Toast.makeText(context, "error =>" + from, Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            UtilMethods.INSTANCE.internetNotAvailableMessage(context);
        }
    }

    private void showNursingOrder(Context context,String mobile) {
        if (UtilMethods.INSTANCE.isNetworkAvialable(context)) {
            UtilMethods.INSTANCE.nursingOrder(context, mobile, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {
                    nursingList.postValue(message);
                }
                @Override
                public void fail(String from) {
                    nursingList.postValue(from);
                }
            });

        } else {
            UtilMethods.INSTANCE.internetNotAvailableMessage(context);
        }
    }

    public void updatePharmacy(String s){

    }
}
