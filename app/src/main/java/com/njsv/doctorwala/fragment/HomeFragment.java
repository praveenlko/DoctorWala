package com.njsv.doctorwala.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njsv.doctorwala.R;
import com.njsv.doctorwala.address.AddressActivity;
import com.njsv.doctorwala.address.TimingActivity;
import com.njsv.doctorwala.assistant.CareServiceDetailActivity;
import com.njsv.doctorwala.charges.ServiceChargesActivity;
import com.njsv.doctorwala.maincategory.CategoriesAdapter;
import com.njsv.doctorwala.model.CategoryModel;
import com.njsv.doctorwala.pharmacy.PharmacyActivity;
import com.njsv.doctorwala.retrofit.UtilMethods;
import com.njsv.doctorwala.retrofit.mCallBackResponse;
import com.njsv.doctorwala.services.ServicesActivity;
import com.njsv.doctorwala.slider.CustomSlider;
import com.njsv.doctorwala.slider.BannerSliderAdapter;
import com.njsv.doctorwala.slider.SliderImageData;
import com.njsv.doctorwala.slider.BannerSliderModel;
import com.njsv.doctorwala.util.AppSharedPreferences;
import com.njsv.doctorwala.util.ApplicationConstants;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class HomeFragment extends Fragment {

    private RecyclerView recycler;
    private SliderLayout home_list_banner;
    private TextView category;
    private Dialog dialog;
    private Button btnPrescription;
    private ViewPager bannerSliderViewPager;
    private List<BannerSliderModel> sliderModelList;
    private int currentPage = 2;
    private Timer timer;
    final private long DELAY_TIME = 2000;
    final private long PERIOD_TIME = 2000;
    private SwipeRefreshLayout swipeRefreshLayout;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        init(view);

        dialog = UtilMethods.INSTANCE.getProgressDialog(getContext());
        swipeRefreshLayout.setColorSchemeResources(R.color.toolbarColor);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                categorySlider();
                bannerSlider();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        AppSharedPreferences appSharedPreferences = new AppSharedPreferences(requireActivity().getApplication());
        appSharedPreferences.getLoginEmail();

        btnPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PharmacyActivity.class);
//                Intent intent = new Intent(getContext(), ServiceChargesActivity.class);
                intent.putExtra("HomeOrder","HomeOrder");
                startActivity(intent);
            }
        });

        categorySlider();
        bannerSlider();

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setCategory(List<CategoryModel> list) {
        try {
            CategoriesAdapter categoriesAdapter = new CategoriesAdapter(getContext(), list);
            recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
            recycler.setAdapter(categoriesAdapter);
            categoriesAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setSlider(List<SliderImageData> list) {

        try {
            for (SliderImageData data : list) {
                CustomSlider textSliderView = new CustomSlider(getContext());
                // initialize a SliderLayout
                textSliderView
                        .image(ApplicationConstants.INSTANCE.OFFER_IMAGES + data.getImage())
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener((BaseSliderView.OnSliderClickListener) getContext());
                home_list_banner.addSlider(textSliderView);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bannerSlider() {

        sliderModelList = new ArrayList<>();

        sliderModelList.add(new BannerSliderModel(R.drawable.banner5));
        sliderModelList.add(new BannerSliderModel(R.drawable.banner6));

        sliderModelList.add(new BannerSliderModel(R.drawable.banner1));
        sliderModelList.add(new BannerSliderModel(R.drawable.banner2));
        sliderModelList.add(new BannerSliderModel(R.drawable.banner3));
        sliderModelList.add(new BannerSliderModel(R.drawable.banner4));
        sliderModelList.add(new BannerSliderModel(R.drawable.banner5));
        sliderModelList.add(new BannerSliderModel(R.drawable.banner6));

        sliderModelList.add(new BannerSliderModel(R.drawable.banner1));
        sliderModelList.add(new BannerSliderModel(R.drawable.banner2));

        BannerSliderAdapter sliderAdapter = new BannerSliderAdapter(sliderModelList);
        bannerSliderViewPager.setAdapter(sliderAdapter);
        bannerSliderViewPager.setClipToPadding(false);
        bannerSliderViewPager.setPageMargin(20);

        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    pageLooper();
                }
            }
        };

        bannerSliderViewPager.addOnPageChangeListener(onPageChangeListener);
        startBannerSlideShow();

        bannerSliderViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pageLooper();
                stopBannerSlideShow();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    startBannerSlideShow();
                }
                return false;
            }
        });


    }

    private void pageLooper() {
        if (currentPage == sliderModelList.size() - 2) {
            currentPage = 2;
            bannerSliderViewPager.setCurrentItem(currentPage, false);
        }

        if (currentPage == 1) {
            currentPage = sliderModelList.size() - 3;
            bannerSliderViewPager.setCurrentItem(currentPage, false);
        }
    }

    private void startBannerSlideShow() {
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if (currentPage >= sliderModelList.size()) {
                    currentPage = 1;
                }
                bannerSliderViewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, DELAY_TIME, PERIOD_TIME);
    }

    private void stopBannerSlideShow() {
        timer.cancel();
    }

    private void init(View view){
        home_list_banner = view.findViewById(R.id.home_img_slider);
        recycler = view.findViewById(R.id.recycler);
        category = view.findViewById(R.id.tv_category);
        btnPrescription = view.findViewById(R.id.btn_prescription);
        bannerSliderViewPager = view.findViewById(R.id.banner_slider_view_pager);
        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
    }

    private void categorySlider(){
        if (UtilMethods.INSTANCE.isNetworkAvialable(requireContext())) {

            try {
                dialog.show();
                UtilMethods.INSTANCE.categories(getContext(), new mCallBackResponse() {
                    @Override
                    public void success(String from, String message) {
                        Type listType = new TypeToken<ArrayList<CategoryModel>>() {
                        }.getType();
                        List<CategoryModel> list = new Gson().fromJson(message, listType);

                        category.setVisibility(View.VISIBLE);

                        setCategory(list);

                    }

                    @Override
                    public void fail(String from) {
                    }
                });

                UtilMethods.INSTANCE.imageSlider(getContext(), new mCallBackResponse() {
                    @Override
                    public void success(String from, String message) {
                        Type listType = new TypeToken<ArrayList<SliderImageData>>() {
                        }.getType();
                        List<SliderImageData> list = new Gson().fromJson(message, listType);
                        Log.d("TAG----", "success: " + list);

                        setSlider(list);

                    }

                    @Override
                    public void fail(String from) {
                        dialog.dismiss();
                    }
                });
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                dialog.dismiss();
            }
        } else {
            UtilMethods.INSTANCE.internetNotAvailableMessage(getContext());
        }
    }

}