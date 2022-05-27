package com.njsv.doctorwala.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njsv.doctorwala.R;
import com.njsv.doctorwala.retrofit.UtilMethods;
import com.njsv.doctorwala.retrofit.mCallBackResponse;
import com.njsv.doctorwala.util.AppSharedPreferences;
import com.njsv.doctorwala.util.ApplicationConstants;
import com.njsv.doctorwala.util.MyGlide;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private EditText tvname, tvemail, tvgender, tvmobile;
    private Button tvsubmit;
    private TextView editDetail, walletAmt;
    private RelativeLayout editImageLayout;
    private ImageView editImage;
    private CircleImageView profileImage;
    private Spinner spinner;
    private AppSharedPreferences preferences;
    private File file;
    private String setGender;
    private Dialog dialog;
    private SwipeRefreshLayout swipeRefreshLayout;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "IntentReset"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        init(view);
        preferences = new AppSharedPreferences(requireActivity().getApplication());

        swipeRefreshLayout.setColorSchemeResources(R.color.toolbarColor);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    viewProfile(preferences.getLoginUserLoginId());
                    viewWallet(preferences.getLoginUserLoginId());
                    swipeRefreshLayout.setRefreshing(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        dialog = UtilMethods.INSTANCE.getProgressDialog(getContext());

        String[] genderSpinner = {"Male", "Female"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, genderSpinner);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        try {
            int initialPosition = spinnerAdapter.getPosition(setGender);
            spinner.setSelection(initialPosition);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((TextView) view).setTextColor(Color.parseColor("#000000"));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            spinner.setAdapter(spinnerAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }


        viewProfile(preferences.getLoginUserLoginId());
        viewWallet(preferences.getLoginUserLoginId());

        editDetail.setOnClickListener(view13 -> {

            try {
                editImageLayout.setVisibility(View.VISIBLE);
                tvname.setEnabled(true);
                tvname.setBackground(getResources().getDrawable(R.drawable.edit_underline));

                tvemail.setEnabled(true);
                tvemail.setBackground(getResources().getDrawable(R.drawable.edit_underline));

                tvgender.setVisibility(View.GONE);
                spinner.setVisibility(View.VISIBLE);
                spinner.setBackground(getResources().getDrawable(R.drawable.edit_underline));

//            tvmobile.setEnabled(true);
//            tvmobile.setBackground(getResources().getDrawable(R.drawable.edit_underline));

                tvsubmit.setEnabled(true);
                tvsubmit.setTextColor(Color.rgb(255, 255, 255));
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        editImage.setOnClickListener(view12 -> {
            @SuppressLint("IntentReset") Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, 1);

        });

        tvsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = preferences.getLoginUserLoginId();
                String name = tvname.getText().toString().trim();
                String email = tvemail.getText().toString().trim();
                String gender = spinner.getSelectedItem().toString();
                String mobile = tvmobile.getText().toString().trim();

                if (tvname.getText().toString().isEmpty()) {
                    tvname.setError("Enter your name");
                    return;
                }
                if (tvemail.getText().toString().isEmpty()) {
                    tvemail.setError("Enter your Email");
                    return;
                }
                if (spinner.getSelectedItem().toString().isEmpty()) {
                    tvmobile.setError("Please select gender");
                    return;
                }
                if (tvmobile.getText().toString().isEmpty()) {
                    tvmobile.setError("Enter your Mobile");
                    return;
                }
                if (file == null) {
                    if (UtilMethods.INSTANCE.isNetworkAvialable(requireContext())) {
                        UtilMethods.INSTANCE.updateProfile(requireContext(), id, name, mobile, email, gender, new mCallBackResponse() {
                            @Override
                            public void success(String from, String message) {
                                dialog.dismiss();
                                try {
                                    new SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Success")
                                            .setContentText("Profile update Successfully......")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.dismissWithAnimation();
                                                    viewProfile(preferences.getLoginUserLoginId());
                                                    afterUpload();
                                                }
                                            }).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    dialog.dismiss();
                                }
                            }

                            @Override
                            public void fail(String from) {
                                dialog.show();
                                try {
                                    viewProfile(preferences.getLoginUserLoginId());
                                    afterUpload();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    dialog.dismiss();
                                }
                            }
                        });
                    } else {
                        dialog.dismiss();
                        UtilMethods.INSTANCE.internetNotAvailableMessage(requireContext());
                    }

                } else {

                    if (UtilMethods.INSTANCE.isNetworkAvialable(requireContext())) {
                        UtilMethods.INSTANCE.updateImageProfile(requireContext(), id, name, mobile, email, gender, file, new mCallBackResponse() {
                            @Override
                            public void success(String from, String message) {
                                dialog.dismiss();
                                try {
                                    new SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Success")
                                            .setContentText("Profile update Successfully......")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.dismissWithAnimation();
                                                    viewProfile(preferences.getLoginUserLoginId());
                                                    afterUpload();
                                                }
                                            }).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    dialog.dismiss();
                                }
                            }

                            @Override
                            public void fail(String from) {
                                dialog.show();
                                Log.d("profile",from);
                                try {
                                    viewProfile(preferences.getLoginUserLoginId());
                                    afterUpload();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    dialog.dismiss();
                                }
                            }
                        });
                    } else {
                        dialog.dismiss();
                        UtilMethods.INSTANCE.internetNotAvailableMessage(requireContext());
                    }
                }
            }
        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == 1) {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
//                        MyGlide.Profile(getContext(), String.valueOf(data.getData()), profileImage);
                        try {
//                            String path = data.getData().getPath();
//                            file = new File(path.replace("/raw/", ""));
                            Uri selectedImage = data.getData();
                            file = new File(getRealPathFromURI(selectedImage));

                            String currentPhotoPath = getRealPathFromURI(selectedImage);

                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImage);
                            profileImage.setImageBitmap(bitmap);



                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(getContext(), "Image not found!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init(View view) {
        tvname = view.findViewById(R.id.tvusername);
        tvemail = view.findViewById(R.id.tvemail);
        tvgender = view.findViewById(R.id.tvgender);
        tvmobile = view.findViewById(R.id.tvmobile);
        tvsubmit = view.findViewById(R.id.btnSubmit);
        editDetail = view.findViewById(R.id.edit_detail);
        editImageLayout = view.findViewById(R.id.edit_image_layout);
        editImage = view.findViewById(R.id.edit_image);
        profileImage = view.findViewById(R.id.profile_image);
        spinner = view.findViewById(R.id.spingender);
        walletAmt = view.findViewById(R.id.wallet_amt);
        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);

    }

    private void viewProfile(String id) {
        dialog.show();
        if (UtilMethods.INSTANCE.isNetworkAvialable(requireContext())) {
            UtilMethods.INSTANCE.viewProfile(getContext(), id, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {
                    Type listType = new TypeToken<ArrayList<UpdateProfileModel>>() {
                    }.getType();

                    try {
                        List<UpdateProfileModel> list = new Gson().fromJson(message, listType);
                        tvname.setText(list.get(0).getName());
                        tvemail.setText(list.get(0).getEmail());
                        tvgender.setText(list.get(0).getGender());
                        tvmobile.setText(list.get(0).getContact());
                        setGender = list.get(0).getGender();

                        if (list.get(0).getProfilePic().isEmpty()) {
//                        profileImage.setImageResource(R.drawable.profile);
                            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.profile);
                            Bitmap resized = Bitmap.createScaledBitmap(bm, 100, 100, true);
                            Bitmap conv_bm = getRoundedRectBitmap(resized, 100);
                            profileImage.setImageBitmap(conv_bm);
                        } else {
                            if (!(list.get(0).getProfilePic()).isEmpty()) {
                                Glide.with(getContext().getApplicationContext()).load(ApplicationConstants.INSTANCE.PROFILE_VIEW_IMAGES + list.get(0).getProfilePic()).error(R.drawable.profile).into(profileImage);

                            } else {
                                Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.profile);
                                Bitmap resized = Bitmap.createScaledBitmap(bm, 100, 100, true);
                                Bitmap conv_bm = getRoundedRectBitmap(resized, 100);
                                profileImage.setImageBitmap(conv_bm);
                                profileImage.setImageResource(R.drawable.profile);
                            }
                        }

                        walletAmt.setText(list.get(0).getAmount() + " points");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void fail(String from) {
                }
            });
        } else {
            UtilMethods.INSTANCE.internetNotAvailableMessage(getContext());
        }
        dialog.dismiss();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void afterUpload() {

        try {
            dialog.show();
            editImageLayout.setVisibility(View.GONE);
            tvname.setBackgroundResource(0);
            tvname.setEnabled(false);
            tvemail.setBackgroundResource(0);
            tvemail.setEnabled(false);
            spinner.setVisibility(View.GONE);
            tvgender.setBackgroundResource(0);
            tvgender.setVisibility(View.VISIBLE);

//        tvmobile.setBackground(getResources().getDrawable(R.drawable.btn_text));
//        tvmobile.setEnabled(false);

            tvsubmit.setEnabled(false);
            tvsubmit.setTextColor(Color.argb(90, 255, 255, 255));
            dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void viewWallet(String id) {
        dialog.show();
        if (UtilMethods.INSTANCE.isNetworkAvialable(requireContext())) {
            UtilMethods.INSTANCE.viewWallet(getContext(), id, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {

                    try {
                        Type listType = new TypeToken<ArrayList<WalletModel>>() {
                        }.getType();
                        List<WalletModel> list = new Gson().fromJson(message, listType);
//                    float SUM = 0;
//
//                    for (int i=0;i < list.size();i++){
//
//                        SUM = SUM+ Float.parseFloat(list.get(i).getAmount());
//
//                    }
//
//                    walletAmt.setText(" "+String.valueOf(SUM)+" points");
                        walletAmt.setText(list.get(0).getAmount());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void fail(String from) {
                }
            });
        } else {
            UtilMethods.INSTANCE.internetNotAvailableMessage(getContext());
        }
        dialog.dismiss();
    }

    public static Bitmap getRoundedRectBitmap(Bitmap bitmap, int pixels) {
        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);

            int color = 0xff424242;
            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, 200, 200);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawCircle(50, 50, 50, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

        } catch (NullPointerException e) {
        } catch (OutOfMemoryError o) {
        }
        return result;
    }

    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = requireActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }
}