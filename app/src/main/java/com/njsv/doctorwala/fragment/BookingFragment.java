package com.njsv.doctorwala.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njsv.doctorwala.R;
import com.njsv.doctorwala.retrofit.UtilMethods;
import com.njsv.doctorwala.retrofit.mCallBackResponse;
import com.njsv.doctorwala.util.AppSharedPreferences;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookingFragment extends Fragment {

    private static RecyclerView bookingOrderRecylerVIew;
    private static TextView noOrder;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static BookingAdapter bookingAdapter;
    private static String userId, cancelMobile;

    public BookingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        AppSharedPreferences preferences = new AppSharedPreferences(requireActivity().getApplication());
        String id = preferences.getLoginUserLoginId();
        userId = preferences.getLoginUserLoginId();
        cancelMobile = preferences.getLoginMobile();
        bookingShow(requireContext(), id);

        bookingOrderRecylerVIew = view.findViewById(R.id.rv_booking);
        noOrder = view.findViewById(R.id.no_order);
        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);

        swipeRefreshLayout.setColorSchemeResources(R.color.toolbarColor);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bookingShow(requireContext(), id);
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        return view;
    }

    public static void bookingShow(Context context, String id) {
        if (UtilMethods.INSTANCE.isNetworkAvialable(context)) {
            UtilMethods.INSTANCE.showBooking(context, id, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {
                    noOrder.setVisibility(View.GONE);
                    bookingOrderRecylerVIew.setVisibility(View.VISIBLE);
                    Type listType = new TypeToken<ArrayList<BookingModel>>() {
                    }.getType();
                    List<BookingModel> list = new Gson().fromJson(message, listType);

                    setBooking(context, list);
                }

                @Override
                public void fail(String from) {
                    bookingOrderRecylerVIew.setVisibility(View.GONE);
                    noOrder.setVisibility(View.VISIBLE);
                }
            });

        } else {
            UtilMethods.INSTANCE.internetNotAvailableMessage(context);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private static void setBooking(Context context, List<BookingModel> list) {
        //    private List<BookingModel> bookingModelList = new ArrayList<>();
//        AppSharedPreferences preferences = new AppSharedPreferences(context);
//        String mobile = preferences.getLoginMobile();

        bookingAdapter = new BookingAdapter(list, context, cancelMobile);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        bookingOrderRecylerVIew.setLayoutManager(layoutManager);
        bookingOrderRecylerVIew.setAdapter(bookingAdapter);
        bookingAdapter.notifyDataSetChanged();
    }

    public static void cancelOrder(String orderid, String mobile, String reason, int position, Context context) {
        if (UtilMethods.INSTANCE.isNetworkAvialable(context)) {
            UtilMethods.INSTANCE.cancelOrder(context, orderid, mobile, reason, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {
                    bookingShow(context, userId);
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