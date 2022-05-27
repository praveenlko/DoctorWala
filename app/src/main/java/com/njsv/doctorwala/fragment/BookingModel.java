package com.njsv.doctorwala.fragment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookingModel {

    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("orderdate")
    @Expose
    private String orderdate;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("sname")
    @Expose
    private String sname;
    @SerializedName("schedule")
    @Expose
    private String schedule;
    @SerializedName("sub_cat_id")
    @Expose
    private String subCatId;
    @SerializedName("service")
    @Expose
    private String service;
    @SerializedName("paymentmethod")
    @Expose
    private String paymentmethod;
    @SerializedName("totalamount")
    @Expose
    private String totalamount;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("document_type")
    @Expose
    private String documentType;
    @SerializedName("aadhar_no")
    @Expose
    private String aadharNo;
    @SerializedName("contact")
    @Expose
    private String contact;
    @SerializedName("status")
    @Expose
    private String status;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(String subCatId) {
        this.subCatId = subCatId;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPaymentmethod() {
        return paymentmethod;
    }

    public void setPaymentmethod(String paymentmethod) {
        this.paymentmethod = paymentmethod;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
