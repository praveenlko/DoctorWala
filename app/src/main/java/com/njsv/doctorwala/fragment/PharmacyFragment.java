package com.njsv.doctorwala.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njsv.doctorwala.R;
import com.njsv.doctorwala.pharmacy.PharmacyActivity;
import com.njsv.doctorwala.retrofit.UtilMethods;
import com.njsv.doctorwala.retrofit.mCallBackResponse;
import com.njsv.doctorwala.util.AppSharedPreferences;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PharmacyFragment extends Fragment {

    private static RecyclerView bookingPharmacy;
    private static TextView noPharmacy;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static PharmacyAdapter pharmacyAdapter;
    private static String pharmacyCancelMobile;

    public PharmacyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pharmacy, container, false);
        AppSharedPreferences preferences = new AppSharedPreferences(requireActivity().getApplication());
        String mobile = preferences.getLoginMobile();
        pharmacyCancelMobile = preferences.getLoginMobile();

        bookingPharmacy = view.findViewById(R.id.rv_pharmacy);
        noPharmacy = view.findViewById(R.id.no_pharmacy);
        showPharmacyOrder(requireContext(),mobile);

        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);

        swipeRefreshLayout.setColorSchemeResources(R.color.toolbarColor);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showPharmacyOrder(requireContext(),mobile);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

//        bookViewModel.getPharmacy(requireActivity(),mobile).observe(getActivity(), new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                if (!s.isEmpty()){
//                    Type listType = new TypeToken<ArrayList<PharmacyOrderModel>>() {
//                    }.getType();
//                    List<PharmacyOrderModel> list = new Gson().fromJson(s, listType);
//
//                    if (list != null) {
//                        if (list.size() > 0) {
//                            noPharmacy.setVisibility(View.GONE);
//                            bookingPharmacy.setVisibility(View.VISIBLE);
//                            setPharmacy(list);
//                        } else {
//                            bookingPharmacy.setVisibility(View.GONE);
//                            noPharmacy.setVisibility(View.VISIBLE);
//                        }
//                    } else {
//                        bookingPharmacy.setVisibility(View.GONE);
//                        noPharmacy.setVisibility(View.VISIBLE);
//                    }
//                }
//            }
//        });



        return view;
    }

    public static void showPharmacyOrder(Context context,String mobile) {
        if (UtilMethods.INSTANCE.isNetworkAvialable(context)) {
            UtilMethods.INSTANCE.showPharmacyOrder(context, mobile, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {
                    noPharmacy.setVisibility(View.GONE);
                    bookingPharmacy.setVisibility(View.VISIBLE);
                    Log.d("order pharmacy", "enter => " + message);
                    Type listType = new TypeToken<ArrayList<PharmacyOrderModel>>() {
                    }.getType();
                    List<PharmacyOrderModel> list = new Gson().fromJson(message, listType);

                    setPharmacy(context,list);
                }

                @Override
                public void fail(String from) {
                    bookingPharmacy.setVisibility(View.GONE);
                    noPharmacy.setVisibility(View.VISIBLE);

                }
            });

        } else {
            UtilMethods.INSTANCE.internetNotAvailableMessage(context);
        }
    }

    private static void setPharmacy(Context context,List<PharmacyOrderModel> list) {
        pharmacyAdapter = new PharmacyAdapter(context, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        bookingPharmacy.setLayoutManager(layoutManager);
        bookingPharmacy.setAdapter(pharmacyAdapter);
        pharmacyAdapter.notifyDataSetChanged();
    }

    public static void pharmacyCancel(String orderid, String reason, Context context) {

        if (UtilMethods.INSTANCE.isNetworkAvialable(context)) {
            UtilMethods.INSTANCE.pharmacyCancel(context, orderid, reason, new mCallBackResponse() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void success(String from, String message) {
                    showPharmacyOrder(context,pharmacyCancelMobile);

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

}