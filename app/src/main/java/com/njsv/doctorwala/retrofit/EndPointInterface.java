package com.njsv.doctorwala.retrofit;

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
import com.njsv.doctorwala.order.CurrentOrderModel;
import com.njsv.doctorwala.order.OrderModel;
import com.njsv.doctorwala.payment.TimeSlotModel;
import com.njsv.doctorwala.pharmacy.PharmacyModel;
import com.njsv.doctorwala.signup.OtpModel;
import com.njsv.doctorwala.signup.SignupResponse;
import com.njsv.doctorwala.slider.SliderImageData;
import com.njsv.doctorwala.subcategory.SubCategoriesModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EndPointInterface {
    @GET("API/api_categories")
    Call<List<CategoryModel>> categories();

    @GET("API/api_offers/")
    Call<List<SliderImageData>> imageSlider();

    @GET("API/api_subcategory")
    Call<List<SubCategoriesModel>> subCategory(@Query("id") String id);

    @GET("API/get_hospital_cat")
    Call<List<HosipitalModel>> hosipitalList();

    @POST("API/api_login")
    @FormUrlEncoded
    Call<LoginData> login(@Field("mobile") String mobile,
                          @Field("password") String password);

    @Headers("multipart: true")
    @Multipart
    @POST("API/Add_pharmacy")
    Call<PharmacyModel> pharmacy(@Part("name") RequestBody name,
                                       @Part("mobile") RequestBody mobile,
                                       @Part("phar_order_id") RequestBody pharOrderId,
                                       @Part("msg") RequestBody message,
                                       @Part("address") RequestBody address,
                                       @Part("lat") RequestBody lat,
                                       @Part("long") RequestBody log,
                                       @Part MultipartBody.Part pharmacy_img);

//    @GET("API/api_charges/{cat_id}/{sub_id}")
//    Call<List<ChargesModel>> charges(@Path("cat_id") String cat_id, @Path("sub_id") String sub_id);

    @GET("API/api_charges/{cat_id}/{sub_id}/{time_zone}")
    Call<List<ChargesModel>> charges(@Path("cat_id") String cat_id, @Path("sub_id") String sub_id,@Path("time_zone") String time_zone);

    @Headers("multipart: true")
    @Multipart
    @POST("API/add_nursing")
    Call<AddressModel> nursing(
            @Part("nur_order_id") RequestBody nurOrderId,
            @Part("name") RequestBody name,
            @Part("user_id") RequestBody userId,
            @Part("mobile") RequestBody mobile,
            @Part("address") RequestBody address,
            @Part("document_type") RequestBody documentType,
            @Part("document_no") RequestBody documentNo,
            @Part("subname") RequestBody subname,
            @Part("longi") RequestBody longi,
            @Part("lat") RequestBody lat,
            @Part("msg") RequestBody msg,
            @Part MultipartBody.Part nursing_img
    );

    @Headers("multipart: true")
    @Multipart
    @POST("API/update_address")
    Call<AddressModel> updateAddress(
            @Part("u_id") RequestBody id,
            @Part("address") RequestBody address,
            @Part("pin_code") RequestBody pincode,
            @Part("landmark") RequestBody landmark,
            @Part("document_type") RequestBody documentType,
            @Part("aadhar_no") RequestBody aadharNo,
            @Part("lat") RequestBody lat,
            @Part("log") RequestBody log,
            @Part MultipartBody.Part aadhar_image
    );

    @GET("API/api_timeslots/{time_zone}")
    Call<List<TimeSlotModel>> timeSlot(@Path("time_zone")  String serivce);

    @POST("api.php?checkout")
    @FormUrlEncoded
    Call<OrderModel> orderPlace(
            @Field("orderid") String orderId,
            @Field("sub_cat_id") String subCatId,
            @Field("cat_id") String catId,
            @Field("id") String customerId,
            @Field("service") String bookingType,
            @Field("aadhar_no") String aadharNo,
            @Field("thumbnail") String thumbnail,
            @Field("name") String subCategoryName,
            @Field("amount") String amount,
            @Field("discount") String discount,
            @Field("totalamount") String totalamount,
            @Field("schedule") String schedule,
            @Field("deliverycharge") String deliverycharge,
            @Field("u_date") String date,
            @Field("paymentmethod") String paymentmethod,
            @Field("landmark") String landmark,
            @Field("address") String address,
            @Field("pincode") String pincode
    );

@POST("api.php?tempcheckout")
    @FormUrlEncoded
    Call<OrderModel> tempOrderPlace(
            @Field("orderid") String orderId,
            @Field("sub_cat_id") String subCatId,
            @Field("cat_id") String catId,
            @Field("id") String customerId,
            @Field("service") String bookingType,
            @Field("aadhar_no") String aadharNo,
            @Field("thumbnail") String thumbnail,
            @Field("name") String subCategoryName,
            @Field("amount") String amount,
            @Field("discount") String discount,
            @Field("totalamount") String totalamount,
            @Field("schedule") String schedule,
            @Field("deliverycharge") String deliverycharge,
            @Field("u_date") String date,

            @Field("landmark") String landmark,
            @Field("address") String address,
            @Field("pincode") String pincode
    );

    @Headers("multipart: true")
    @Multipart
    @POST("API/update_profile")
    Call<UpdateProfileModel> updateImageProfile(@Part("id") RequestBody id,
                                           @Part("name") RequestBody name,
                                           @Part("contact") RequestBody contact,
                                           @Part("email") RequestBody email,
                                           @Part("gender") RequestBody gender,
                                           @Part MultipartBody.Part profile_pic);

    @Headers("multipart: true")
    @Multipart
    @POST("API/update_profile")
    Call<UpdateProfileModel> updateProfile(@Part("id") RequestBody id,
                                           @Part("name") RequestBody name,
                                           @Part("contact") RequestBody contact,
                                           @Part("email") RequestBody email,
                                           @Part("gender") RequestBody gender);

    @GET("API/api_historyorders")
    Call<List<BookingModel>> booking(@Query("id") String id);

    @GET("API/api_currentorder")
    Call<List<CurrentOrderModel>> currentOrder();

    @GET("API/api_profile_view/{user_id}")
    Call<List<UpdateProfileModel>> profileView(@Path("user_id") String id);

    @FormUrlEncoded
    @POST("API/User_Signup")
    Call<SignupResponse> signup(@Field("name") String name,
                                @Field("email") String email,
                                @Field("password") String password,
                                @Field("contact") String mobile,
                                @Field("gender") String gender);

    @POST("api.php?sms")
    @FormUrlEncoded
    Call<OtpModel> verifyOtp(
            @Field("sms") String sms,
            @Query("mobile") String mobile
    );

    @GET("API/api_get_pharmacy/{mobile}")
    Call<List<PharmacyOrderModel>> pharmacyList(@Path("mobile")  String pharmacy);

    @FormUrlEncoded
    @POST("API/api_wallet")
    Call<OrderModel> wallet(@Field("amount") String amt,
                                @Field("userid") String user);

    @POST("api.php?cancel")
    @FormUrlEncoded
    Call<OrderModel> orderCancel(
            @Field("cancel") String cancel,
            @Field("orderid") String orderId,
            @Field("mobile") String mobile,
            @Field("reason") String reason
    );


    @POST("API/DoctorParchcancel/{phar_order_id}/{reason}")
    @FormUrlEncoded
    Call<OrderModel> pharmacyCancel(
            @Path("phar_order_id") String orderId,
            @Path("reason") String reason,
            @Field("") String empty
    );

    @GET("API/get_wallet_list/{user_id}")
    Call<List<WalletModel>> viewWallet(@Path("user_id") String id);

    @GET("API/get_orderby_subcategory/{sub_cat_id}")
    Call<List<SubCategoriesModel>> getSubcategoryName(@Path("sub_cat_id") String id);

    @GET("API/api_get_nursing/{mobile}")
    Call<List<NursingModel>> nursingOrder(@Path("mobile")  String mobile);

    @POST("API/NurseCarecancel")
    @FormUrlEncoded
    Call<OrderModel> nurseCancel(
            @Field("nur_order_id") String orderId,
            @Field("reason") String reason
    );

    @POST("API/redeem_point")
    @FormUrlEncoded
    Call<OrderModel> redeemPoint(
            @Field("point") String point,
            @Field("user_id") String userId,
            @Field("redeem_point") String redeemPoint
    );

    @GET("API/api_temp_historyorders")
    Call<List<TempOrderModel>> tempResponse(@Query("order_id") String id);


    @GET("API/temp_api_delete")
    Call<OrderModel> tempDelete(@Query("order_id") String id);

    @GET("API/resetlink")
    Call<OrderModel> resetlink(@Query("email") String email);

    @POST("API/updatepass")
    @FormUrlEncoded
    Call<OrderModel> updatepass(@Field("") String empty,
                          @Query("email") String email,
                          @Query("password") String password,
                          @Query("cpassword") String cpassword);

}
