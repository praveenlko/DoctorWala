package com.njsv.doctorwala.fragment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NursingModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("nur_order_id")
    @Expose
    private String nurOrderId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("nursing_img")
    @Expose
    private String nursingImg;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("longi")
    @Expose
    private String longi;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("addby")
    @Expose
    private String addby;
    @SerializedName("subname")
    @Expose
    private String subname;
    @SerializedName("document_type")
    @Expose
    private String documentType;
    @SerializedName("document_no")
    @Expose
    private String documentNo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNurOrderId() {
        return nurOrderId;
    }

    public void setNurOrderId(String nurOrderId) {
        this.nurOrderId = nurOrderId;
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

    public String getNursingImg() {
        return nursingImg;
    }

    public void setNursingImg(String nursingImg) {
        this.nursingImg = nursingImg;
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

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getAddby() {
        return addby;
    }

    public void setAddby(String addby) {
        this.addby = addby;
    }

    public String getSubname() {
        return subname;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNo() {
        return documentNo;
    }

    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }
}
