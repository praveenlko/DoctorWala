package com.njsv.doctorwala.fragment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PharmacyOrderModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("phar_order_id")
    @Expose
    private String pharOrderId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("pharmacy_img")
    @Expose
    private String pharmacyImg;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("long")
    @Expose
    private String _long;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("cancelreason")
    @Expose
    private String cancelreason;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPharOrderId() {
        return pharOrderId;
    }

    public void setPharOrderId(String pharOrderId) {
        this.pharOrderId = pharOrderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPharmacyImg() {
        return pharmacyImg;
    }

    public void setPharmacyImg(String pharmacyImg) {
        this.pharmacyImg = pharmacyImg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLong() {
        return _long;
    }

    public void setLong(String _long) {
        this._long = _long;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getCancelreason() {
        return cancelreason;
    }

    public void setCancelreason(String cancelreason) {
        this.cancelreason = cancelreason;
    }


}
