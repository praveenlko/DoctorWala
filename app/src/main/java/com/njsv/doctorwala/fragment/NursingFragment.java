package com.njsv.doctorwala.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njsv.doctorwala.R;
import com.njsv.doctorwala.payment.EmergencyAdapter;
import com.njsv.doctorwala.payment.PaymentActivity;
import com.njsv.doctorwala.payment.TimeSlotModel;
import com.njsv.doctorwala.retrofit.UtilMethods;
import com.njsv.doctorwala.retrofit.mCallBackResponse;
import com.njsv.doctorwala.util.AppSharedPreferences;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class NursingFragment extends Fragment {

    private static RecyclerView bookingNursing;
    private static TextView noPharmacy;
    private List<NursingModel> nursingModelList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private static String cancelMobile;


    public NursingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nursing, container, false);
        bookingNursing = view.findViewById(R.id.rv_nursing);
        noPharmacy = view.findViewById(R.id.no_pharmacy);
        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.toolbarColor);

        AppSharedPreferences preferences = new AppSharedPreferences(requireActivity().getApplication());
        String mobile = preferences.getLoginMobile();
        cancelMobile = preferences.getLoginMobile();
        nursingOrder(requireContext(),mobile);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                nursingOrder(requireContext(),mobile);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

//    private void setNursingList(String mobile){
//        bookViewModel.getNursing(requireActivity(),mobile).observe(requireActivity(), new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                Type listType = new TypeToken<ArrayList<NursingModel>>() {
//                }.getType();
//                List<NursingModel> list = new Gson().fromJson(s, listType);
//
//                if (list != null) {
//                    if (list.size() > 0) {
//                        noPharmacy.setVisibility(View.GONE);
//                        bookingNursing.setVisibility(View.VISIBLE);
//                        setNursing(list);
//                    } else {
//                        bookingNursing.setVisibility(View.GONE);
//                        noPharmacy.setVisibility(View.VISIBLE);
//                    }
//                } else {
//                    bookingNursing.setVisibility(View.GONE);
//                    noPharmacy.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//    }


    private static void nursingOrder(Context context,String mobile) {
        if (UtilMethods.INSTANCE.isNetworkAvialable(context)) {
            UtilMethods.INSTANCE.nursingOrder(context, mobile, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {

                    noPharmacy.setVisibility(View.GONE);
                    bookingNursing.setVisibility(View.VISIBLE);
                    Type listType = new TypeToken<ArrayList<NursingModel>>() {}.getType();
                    List<NursingModel> list = new Gson().fromJson(message, listType);

                    setNursing(context,list);

                }

                @Override
                public void fail(String from) {
                    bookingNursing.setVisibility(View.GONE);
                    noPharmacy.setVisibility(View.VISIBLE);
                }
            });

        } else {

            UtilMethods.INSTANCE.internetNotAvailableMessage(context);
        }
    }

    private static void setNursing(Context context,List<NursingModel> list) {
        NursingAdapter nursingAdapter = new NursingAdapter(context, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        bookingNursing.setLayoutManager(layoutManager);
        bookingNursing.setAdapter(nursingAdapter);
        nursingAdapter.notifyDataSetChanged();
    }

    public static void reqCancel(String orderid, String reason, Context context) {
        if (UtilMethods.INSTANCE.isNetworkAvialable(context)) {
            UtilMethods.INSTANCE.nurseCancel(context, orderid, reason, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {
                    nursingOrder(context,cancelMobile);
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