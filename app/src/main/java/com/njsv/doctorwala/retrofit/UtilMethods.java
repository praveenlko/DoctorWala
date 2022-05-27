package com.njsv.doctorwala.retrofit;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.view.ViewGroup;

import android.widget.Toast;


import com.google.gson.Gson;
import com.njsv.doctorwala.R;
import com.njsv.doctorwala.address.AddressModel;
import com.njsv.doctorwala.address.TempOrderModel;
import com.njsv.doctorwala.charges.ChargesModel;
import com.njsv.doctorwala.fragment.BookingModel;
import com.njsv.doctorwala.fragment.NursingModel;

import com.njsv.doctorwala.fragment.PharmacyOrderModel;
import com.njsv.doctorwala.fragment.UpdateProfileModel;
import com.njsv.doctorwala.fragment.WalletModel;
import com.njsv.doctorwala.login.LoginData;
import com.njsv.doctorwala.model.CategoryModel;
import com.njsv.doctorwala.model.HosipitalModel;

import com.njsv.doctorwala.order.OrderModel;
import com.njsv.doctorwala.payment.TimeSlotModel;
import com.njsv.doctorwala.pharmacy.PharmacyModel;
import com.njsv.doctorwala.signup.OtpModel;
import com.njsv.doctorwala.signup.SignupResponse;
import com.njsv.doctorwala.slider.SliderImageData;
import com.njsv.doctorwala.subcategory.SubCategoriesModel;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public enum UtilMethods {

    INSTANCE;

    public boolean isNetworkAvialable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void internetNotAvailableMessage(Context context) {
        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        dialog.setContentText("Internet Not Available");
        dialog.show();
    }

    public boolean isValidMobile(String mobile) {

//        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String mobilePattern = "^(?:0091|\\\\+91|0)[7-9][0-9]{9}$";
        String mobileSecPattern = "[7-9][0-9]{9}$";

        return mobile.matches(mobilePattern) || mobile.matches(mobileSecPattern);
    }

    public boolean isValidEmail(String email) {

        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    //    public Dialog getProgressDialog(Context context) {
//        Dialog dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.default_progress_dialog);
//        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        ProgressBar progressBar = dialog.findViewById(R.id.progress);
//        FadingCircle doubleBounce = new FadingCircle();
//        progressBar.setIndeterminateDrawable(doubleBounce);
//        return dialog;
//    }
    @SuppressLint("UseCompatLoadingForDrawables")
    public Dialog getProgressDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.loading_progress_dialog);
        dialog.setCancelable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.slider_background));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

    public void categories(Context context, final mCallBackResponse callBackResponse) {

        final Dialog dialog = getProgressDialog(context);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<CategoryModel>> call = git.categories();
            call.enqueue(new Callback<List<CategoryModel>>() {
                @Override
                public void onResponse(@NotNull Call<List<CategoryModel>> call, @NotNull Response<List<CategoryModel>> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse", strResponse);
                    if (response.body() != null) {
                        if (response.body().size() > 0) {
                            dialog.dismiss();
                            callBackResponse.success("", strResponse);
                        } else {
                            callBackResponse.fail("No data");
                        }
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<List<CategoryModel>> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void hosipitalList(Context context, final mCallBackResponse callBackResponse) {

        final Dialog dialog = getProgressDialog(context);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<HosipitalModel>> call = git.hosipitalList();
            call.enqueue(new Callback<List<HosipitalModel>>() {
                @Override
                public void onResponse(@NotNull Call<List<HosipitalModel>> call, @NotNull Response<List<HosipitalModel>> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse", strResponse);
                    if (response.body() != null) {
                        if (response.body().size() > 0) {
                            dialog.dismiss();
                            callBackResponse.success("", strResponse);
                        } else {
                            callBackResponse.fail("No data");
                        }
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<List<HosipitalModel>> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void imageSlider(Context context, final mCallBackResponse callBackResponse) {

        final Dialog dialog = getProgressDialog(context);
        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<SliderImageData>> call = git.imageSlider();
            call.enqueue(new Callback<List<SliderImageData>>() {
                @Override
                public void onResponse(@NotNull Call<List<SliderImageData>> call, @NotNull Response<List<SliderImageData>> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse", strResponse);
                    Log.d("strResponse =>", response.toString());
                    if (response.body() != null) {
                        if (response.body().size() > 0) {
                            callBackResponse.success("", strResponse);
                        } else {
                            callBackResponse.fail("No data");
                        }
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<List<SliderImageData>> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void subCategories(Context context, String id, final mCallBackResponse callBackResponse) {

        final Dialog dialog = getProgressDialog(context);

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<SubCategoriesModel>> call = git.subCategory(id);
            call.enqueue(new Callback<List<SubCategoriesModel>>() {
                @Override
                public void onResponse(@NotNull Call<List<SubCategoriesModel>> call, @NotNull Response<List<SubCategoriesModel>> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse", strResponse);
                    Log.d("strResponses", strResponse);
                    if (response.body() != null) {
                        if (response.body().size() > 0) {
                            callBackResponse.success("", strResponse);
                        } else {
                            callBackResponse.fail("No data");
                        }
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<List<SubCategoriesModel>> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void login(Context context, String mobile, String password, final mCallBackResponse callBackResponse) {

        final Dialog dialog = getProgressDialog(context);
        dialog.show();
        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<LoginData> call = git.login(mobile, password);
            call.enqueue(new Callback<LoginData>() {
                @Override
                public void onResponse(@NotNull Call<LoginData> call, @NotNull Response<LoginData> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());

                    if (response.isSuccessful()) {
                        if (response.body() == null || response.body().getType().equals("error")) {
                            callBackResponse.fail("Invalid UserId or Password");
                            Toast.makeText(context, "Invalid UserId or Password", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (response.body().getId() == null || response.body().getId().isEmpty()) {
                            callBackResponse.fail("Invalid UserId or Password");
                        } else {
                            callBackResponse.success("", strResponse);
                        }
                        callBackResponse.success("", strResponse);

                    } else {
                        callBackResponse.fail("Invalid UserId or Password");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<LoginData> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void pharmacy(Context context, String user, String mobile, String pharmacyId, String message, String address, Double lat, Double log, File file, final mCallBackResponse callBackResponse) {
        final Dialog dialog = getProgressDialog(context);
        dialog.show();

        try {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part pharmacy_img = MultipartBody.Part.createFormData("pharmacy_img", file.getName(), requestFile);

            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<PharmacyModel> call = git.pharmacy(
                    RequestBody.create(MediaType.parse("multipart/form-data"), user),
                    RequestBody.create(MediaType.parse("multipart/form-data"), mobile),
                    RequestBody.create(MediaType.parse("multipart/form-data"), pharmacyId),
                    RequestBody.create(MediaType.parse("multipart/form-data"), message),
                    RequestBody.create(MediaType.parse("multipart/form-data"), address),
                    RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(lat)),
                    RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(log)),
                    pharmacy_img);
            call.enqueue(new Callback<PharmacyModel>() {
                @Override
                public void onResponse(@NotNull Call<PharmacyModel> call, @NotNull Response<PharmacyModel> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.d("strResponses", strResponse);
                    if (response.body() != null) {
                        callBackResponse.success("text", strResponse);
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<PharmacyModel> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }

    }

    public void charges(Context context, String catId, String subId, String timeZone, final mCallBackResponse callBackResponse) {
        final Dialog dialog = getProgressDialog(context);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<ChargesModel>> call = git.charges(catId, subId, timeZone);
            call.enqueue(new Callback<List<ChargesModel>>() {
                @Override
                public void onResponse(@NotNull Call<List<ChargesModel>> call, @NotNull Response<List<ChargesModel>> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse", strResponse);
                    Log.d("strResponses", strResponse);
                    if (response.body() != null) {
                        if (response.body().size() > 0) {
                            callBackResponse.success("text", strResponse);
                        } else {
                            callBackResponse.fail("No data");
                        }
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<List<ChargesModel>> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }

    }

    public void nursing(Context context, String nurOrderId, String name, String userId, String mobile, String address, String documentType, String documentNo, String subname, Double longi, Double lat, String msg, File file, final mCallBackResponse callBackResponse) {

        final Dialog dialog = getProgressDialog(context);
        dialog.show();

        try {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part nursing_img = MultipartBody.Part.createFormData("nursing_img", file.getName(), requestFile);

            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<AddressModel> call = git.nursing(RequestBody.create(MediaType.parse("multipart/form-data"), nurOrderId),
                    RequestBody.create(MediaType.parse("multipart/form-data"), name),
                    RequestBody.create(MediaType.parse("multipart/form-data"), userId),
                    RequestBody.create(MediaType.parse("multipart/form-data"), mobile),
                    RequestBody.create(MediaType.parse("multipart/form-data"), address),
                    RequestBody.create(MediaType.parse("multipart/form-data"), documentType),
                    RequestBody.create(MediaType.parse("multipart/form-data"), documentNo),
                    RequestBody.create(MediaType.parse("multipart/form-data"), subname),
                    RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(longi)),
                    RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(lat)),
                    RequestBody.create(MediaType.parse("multipart/form-data"), msg),
                    nursing_img);
            call.enqueue(new Callback<AddressModel>() {
                @Override
                public void onResponse(@NotNull Call<AddressModel> call, @NotNull Response<AddressModel> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse", strResponse);
                    if (response.body() != null) {

                        callBackResponse.success("", strResponse);

                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<AddressModel> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void updateAddress(Context context, String id, String address, String pincode, String landmark, String documentType, String aadharNo, File file, Double lat, Double log, final mCallBackResponse callBackResponse) {

        final Dialog dialog = getProgressDialog(context);
        dialog.show();

        try {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part aadhar_image = MultipartBody.Part.createFormData("aadhar_image", file.getName(), requestFile);

            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<AddressModel> call = git.updateAddress(RequestBody.create(MediaType.parse("multipart/form-data"), id),
                    RequestBody.create(MediaType.parse("multipart/form-data"), address),
                    RequestBody.create(MediaType.parse("multipart/form-data"), pincode),
                    RequestBody.create(MediaType.parse("multipart/form-data"), landmark),
                    RequestBody.create(MediaType.parse("multipart/form-data"), documentType),
                    RequestBody.create(MediaType.parse("multipart/form-data"), aadharNo),
                    RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(lat)),
                    RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(log)),
                    aadhar_image);
            call.enqueue(new Callback<AddressModel>() {
                @Override
                public void onResponse(@NotNull Call<AddressModel> call, @NotNull Response<AddressModel> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse", strResponse);
                    if (response.body() != null) {

                        callBackResponse.success("", strResponse);

                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<AddressModel> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void timeSlot(Context context, String timeZone, final mCallBackResponse callBackResponse) {
        final Dialog dialog = getProgressDialog(context);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<TimeSlotModel>> call = git.timeSlot(timeZone);
            call.enqueue(new Callback<List<TimeSlotModel>>() {
                @Override
                public void onResponse(@NotNull Call<List<TimeSlotModel>> call, @NotNull Response<List<TimeSlotModel>> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse", strResponse);
                    Log.d("strResponses", strResponse);
                    if (response.body() != null) {
                        if (response.body().size() > 0) {
                            callBackResponse.success("text", strResponse);
                        } else {
                            callBackResponse.fail("No data");
                        }
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<List<TimeSlotModel>> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }

    }

    public void orderPlace(Context context, String orderid, String subCatId, String catId, String id, String service, String aadharNo, String thumbnail, String name, String amount, String discount, String totalamount, String schedule, String deliverycharge, String orderdate, String paymentmethod, String landmark, String address, String pincode, final mCallBackResponse callBackResponse) {

        final Dialog dialog = getProgressDialog(context);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<OrderModel> call = git.orderPlace(orderid, subCatId, catId, id, service, aadharNo, thumbnail, name, amount, discount, totalamount, schedule, deliverycharge, orderdate, paymentmethod, landmark, address, pincode);
            call.enqueue(new Callback<OrderModel>() {
                @Override
                public void onResponse(@NotNull Call<OrderModel> call, @NotNull Response<OrderModel> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse", strResponse);
                    if (response.body() != null) {

                        callBackResponse.success("", strResponse);

                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<OrderModel> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void tempOrderPlace(Context context, String orderid, String subCatId, String catId, String id, String service, String aadharNo, String thumbnail, String name, String amount, String discount, String totalamount, String schedule, String deliverycharge, String orderdate, String landmark, String address, String pincode, final mCallBackResponse callBackResponse) {

        final Dialog dialog = getProgressDialog(context);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<OrderModel> call = git.tempOrderPlace(orderid, subCatId, catId, id, service, aadharNo, thumbnail, name, amount, discount, totalamount, schedule, deliverycharge, orderdate, landmark, address, pincode);
            call.enqueue(new Callback<OrderModel>() {
                @Override
                public void onResponse(@NotNull Call<OrderModel> call, @NotNull Response<OrderModel> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse", strResponse);
                    if (response.body() != null) {

                        callBackResponse.success("", strResponse);

                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<OrderModel> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }


    public void addWallet(Context context, String amt, String userid, final mCallBackResponse callBackResponse) {
        final Dialog dialog = getProgressDialog(context);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<OrderModel> call = git.wallet(amt, userid);
            call.enqueue(new Callback<OrderModel>() {
                @Override
                public void onResponse(@NotNull Call<OrderModel> call, @NotNull Response<OrderModel> response) {
                    dialog.dismiss();

                    String strResponse = new Gson().toJson(response.body());
                    if (response.body() != null) {
                        callBackResponse.success("", strResponse);
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<OrderModel> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void updateImageProfile(Context context, String id, String name, String contact, String email, String gender, File file, final mCallBackResponse callBackResponse) {

        final Dialog dialog = getProgressDialog(context);
        dialog.show();

        try {
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part profile_pic = MultipartBody.Part.createFormData("profile_pic", file.getName(), requestFile);

            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<UpdateProfileModel> call = git.updateImageProfile(RequestBody.create(MediaType.parse("multipart/form-data"), id),
                    RequestBody.create(MediaType.parse("multipart/form-data"), name),
                    RequestBody.create(MediaType.parse("multipart/form-data"), contact),
                    RequestBody.create(MediaType.parse("multipart/form-data"), email),
                    RequestBody.create(MediaType.parse("multipart/form-data"), gender),
                    profile_pic);

            call.enqueue(new Callback<UpdateProfileModel>() {
                @Override
                public void onResponse(@NotNull Call<UpdateProfileModel> call, @NotNull Response<UpdateProfileModel> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    if (response.body() != null) {
                        callBackResponse.success("text", strResponse);
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<UpdateProfileModel> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    Log.d("strResponsesFail", t.getMessage());
                    dialog.dismiss();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void updateProfile(Context context, String id, String name, String contact, String email, String gender, final mCallBackResponse callBackResponse) {

        final Dialog dialog = getProgressDialog(context);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<UpdateProfileModel> call = git.updateProfile(RequestBody.create(MediaType.parse("multipart/form-data"), id),
                    RequestBody.create(MediaType.parse("multipart/form-data"), name),
                    RequestBody.create(MediaType.parse("multipart/form-data"), contact),
                    RequestBody.create(MediaType.parse("multipart/form-data"), email),
                    RequestBody.create(MediaType.parse("multipart/form-data"), gender)
            );

            call.enqueue(new Callback<UpdateProfileModel>() {
                @Override
                public void onResponse(@NotNull Call<UpdateProfileModel> call, @NotNull Response<UpdateProfileModel> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    if (response.body() != null) {
                        callBackResponse.success("text", strResponse);
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<UpdateProfileModel> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    Log.d("strResponsesFail", t.getMessage());
                    dialog.dismiss();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }


    public void viewProfile(Context context, String id, final mCallBackResponse callBackResponse) {

        final Dialog dialog = getProgressDialog(context);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<UpdateProfileModel>> call = git.profileView(id);
            call.enqueue(new Callback<List<UpdateProfileModel>>() {
                @Override
                public void onResponse(@NotNull Call<List<UpdateProfileModel>> call, @NotNull Response<List<UpdateProfileModel>> response) {
                    dialog.dismiss();

                    String strResponse = new Gson().toJson(response.body());
                    Log.d("view Profile", strResponse);
                    if (response.body() != null) {
                        callBackResponse.success("", strResponse);
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<List<UpdateProfileModel>> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void showBooking(Context context, String id, final mCallBackResponse callBackResponse) {

        final Dialog dialog = getProgressDialog(context);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<BookingModel>> call = git.booking(id);
            call.enqueue(new Callback<List<BookingModel>>() {
                @Override
                public void onResponse(@NotNull Call<List<BookingModel>> call, @NotNull Response<List<BookingModel>> response) {
                    dialog.dismiss();

                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse", strResponse);
                    Log.d("strResponses", strResponse);
                    if (response.body() != null) {
                        if (response.body().size() > 0) {
                            callBackResponse.success("", strResponse);
                        } else {
                            callBackResponse.fail("No data");
                        }
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<List<BookingModel>> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void signup(final Context context, String fullname, String email, String password, String mobile, String gender, final mCallBackResponse callBackResponse) {
        final Dialog dialog = getProgressDialog(context);
        dialog.show();
        try {
            APIClient.getClient().create(EndPointInterface.class).signup(fullname, email, password, mobile, gender).enqueue(new Callback<SignupResponse>() {
                public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.d("strResponses", strResponse);
                    if (response.body() == null) {
                        callBackResponse.fail("No data");
                    } else if (response.body().getMsg().equals("success")) {
                        callBackResponse.success("", strResponse);
                    } else {
                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }

                public void onFailure(Call<SignupResponse> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void verifyOtp(final Context context, String mobile, final mCallBackResponse callBackResponse) {
        final Dialog dialog = getProgressDialog(context);
        dialog.show();
        try {
            APIClient.getClient().create(EndPointInterface.class).verifyOtp("", mobile).enqueue(new Callback<OtpModel>() {
                public void onResponse(Call<OtpModel> call, Response<OtpModel> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.d("strResponses", strResponse);
                    if (response.body() == null) {
                        callBackResponse.fail("No data");
                    } else {
                        callBackResponse.success("", strResponse);
                    }
                }

                public void onFailure(Call<OtpModel> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void showPharmacyOrder(Context context, String mobile, final mCallBackResponse callBackResponse) {

        final Dialog dialog = getProgressDialog(context);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<PharmacyOrderModel>> call = git.pharmacyList(mobile);
            call.enqueue(new Callback<List<PharmacyOrderModel>>() {
                @Override
                public void onResponse(@NotNull Call<List<PharmacyOrderModel>> call, @NotNull Response<List<PharmacyOrderModel>> response) {
                    dialog.dismiss();

                    String strResponse = new Gson().toJson(response.body());

                    if (response.body() != null) {
                        if (response.body().size() > 0) {
                            callBackResponse.success("", strResponse);
                            Log.d("show pharmacy", "enter => " + strResponse);
                        } else {
                            callBackResponse.fail("No Order in the list");
                            Log.d("show pharmacy", "enter");
                        }
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<List<PharmacyOrderModel>> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void cancelOrder(Context context, String orderid, String mobile, String reason, final mCallBackResponse callBackResponse) {

        final Dialog dialog = getProgressDialog(context);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<OrderModel> call = git.orderCancel("", orderid, mobile, reason);
            call.enqueue(new Callback<OrderModel>() {
                @Override
                public void onResponse(@NotNull Call<OrderModel> call, @NotNull Response<OrderModel> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.d("strResponseOrder", strResponse);
                    if (response.body() != null) {
                        callBackResponse.success("", strResponse);

                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<OrderModel> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void viewWallet(Context context, String id, final mCallBackResponse callBackResponse) {

        final Dialog dialog = getProgressDialog(context);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<WalletModel>> call = git.viewWallet(id);
            call.enqueue(new Callback<List<WalletModel>>() {
                @Override
                public void onResponse(@NotNull Call<List<WalletModel>> call, @NotNull Response<List<WalletModel>> response) {
                    dialog.dismiss();

                    String strResponse = new Gson().toJson(response.body());
                    Log.d("view wallet", strResponse);
                    if (response.body() != null) {
                        callBackResponse.success("", strResponse);
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<List<WalletModel>> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void pharmacyCancel(Context context, String pharmacyOrderId, String reason, final mCallBackResponse callBackResponse) {
        final Dialog dialog = getProgressDialog(context);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<OrderModel> call = git.pharmacyCancel(pharmacyOrderId, reason, "");
            call.enqueue(new Callback<OrderModel>() {
                @Override
                public void onResponse(@NotNull Call<OrderModel> call, @NotNull Response<OrderModel> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.d("strResponses", strResponse);
                    if (response.body() != null) {
                        callBackResponse.success("text", strResponse);
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<OrderModel> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }

    }

    public void getsubCategoryName(Context context, String id, final mCallBackResponse callBackResponse) {

        final Dialog dialog = getProgressDialog(context);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<SubCategoriesModel>> call = git.getSubcategoryName(id);
            call.enqueue(new Callback<List<SubCategoriesModel>>() {
                @Override
                public void onResponse(@NotNull Call<List<SubCategoriesModel>> call, @NotNull Response<List<SubCategoriesModel>> response) {
                    dialog.dismiss();

                    String strResponse = new Gson().toJson(response.body());
                    Log.d("order subcategory", strResponse);
                    if (response.body() != null) {
                        callBackResponse.success("", strResponse);
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<List<SubCategoriesModel>> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void nursingOrder(Context context, String mobile, final mCallBackResponse callBackResponse) {
        final Dialog dialog = getProgressDialog(context);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<NursingModel>> call = git.nursingOrder(mobile);
            call.enqueue(new Callback<List<NursingModel>>() {
                @Override
                public void onResponse(@NotNull Call<List<NursingModel>> call, @NotNull Response<List<NursingModel>> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());

                    Log.d("strResponses", strResponse);
                    if (response.body() != null) {
                        if (response.body().size() > 0) {
                            callBackResponse.success("text", strResponse);
                        } else {
                            callBackResponse.fail("No data");
                        }
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<List<NursingModel>> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }

    }

    public void nurseCancel(Context context, String nurseOrderId, String reason, final mCallBackResponse callBackResponse) {
        final Dialog dialog = getProgressDialog(context);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<OrderModel> call = git.nurseCancel(nurseOrderId, reason);
            call.enqueue(new Callback<OrderModel>() {
                @Override
                public void onResponse(@NotNull Call<OrderModel> call, @NotNull Response<OrderModel> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.d("strResponses", strResponse);
                    if (response.body() != null) {
                        callBackResponse.success("text", strResponse);

                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<OrderModel> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }

    }

    public void redeemPoint(Context context, String point, String userId, String redeemPoint, final mCallBackResponse callBackResponse) {
        final Dialog dialog = getProgressDialog(context);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<OrderModel> call = git.redeemPoint(point, userId, redeemPoint);
            call.enqueue(new Callback<OrderModel>() {
                @Override
                public void onResponse(@NotNull Call<OrderModel> call, @NotNull Response<OrderModel> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.d("strResponses", strResponse);
                    if (response.body() != null) {
                        callBackResponse.success("text", strResponse);

                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<OrderModel> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }

    }

    public void tempResponse(Context context, String orderId, final mCallBackResponse callBackResponse) {

//        final Dialog dialog = getProgressDialog(context);
//        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<TempOrderModel>> call = git.tempResponse(orderId);
            call.enqueue(new Callback<List<TempOrderModel>>() {
                @Override
                public void onResponse(@NotNull Call<List<TempOrderModel>> call, @NotNull Response<List<TempOrderModel>> response) {
//                    dialog.dismiss();

                    String strResponse = new Gson().toJson(response.body());
                    Log.d("strResponses", strResponse);
                    if (response.body() != null) {
                        if (response.body().size() > 0) {
                            callBackResponse.success("", strResponse);
                        } else {
                            callBackResponse.fail("No data");
                        }
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<List<TempOrderModel>> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
//                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
//            dialog.dismiss();
        }
    }

    public void tempDelete(Context context, String orderId, final mCallBackResponse callBackResponse) {

//        final Dialog dialog = getProgressDialog(context);
//        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<OrderModel> call = git.tempDelete(orderId);
            call.enqueue(new Callback<OrderModel>() {
                @Override
                public void onResponse(@NotNull Call<OrderModel> call, @NotNull Response<OrderModel> response) {
//                    dialog.dismiss();

                    String strResponse = new Gson().toJson(response.body());
                    Log.d("strResponses", strResponse);
                    if (response.body() != null) {
                        callBackResponse.success("", strResponse);
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<OrderModel> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
//                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
//            dialog.dismiss();
        }
    }

    public void resetLink(Context context, String email, final mCallBackResponse callBackResponse) {

        final Dialog dialog = getProgressDialog(context);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<OrderModel> call = git.resetlink(email);
            call.enqueue(new Callback<OrderModel>() {
                @Override
                public void onResponse(@NotNull Call<OrderModel> call, @NotNull Response<OrderModel> response) {
                    dialog.dismiss();

//                    String strResponse = new Gson().toJson(response.body());
                    if (response.body() != null) {
                        if (response.body().getMsg1() != null) {
                            String strResponse = new Gson().toJson(response.body().getMsg1());
                            callBackResponse.success("", strResponse.replaceAll("\"", ""));
                        } else if (response.body().getMsg() != null) {
                            String strResponse = new Gson().toJson(response.body().getMsg());
                            callBackResponse.success("", strResponse);
                        } else {
                            callBackResponse.fail("Something went wrong...");
                        }

                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<OrderModel> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void updatePassword(Context context, String email, String password, String confirmPassword, final mCallBackResponse callBackResponse) {

        final Dialog dialog = getProgressDialog(context);
        dialog.show();
        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<OrderModel> call = git.updatepass("",email, password, confirmPassword);
            call.enqueue(new Callback<OrderModel>() {
                @Override
                public void onResponse(@NotNull Call<OrderModel> call, @NotNull Response<OrderModel> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    if (response.body() != null) {
                        callBackResponse.success("", strResponse);
                    } else {
                        callBackResponse.fail("No data");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<OrderModel> call, @NotNull Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }
}
