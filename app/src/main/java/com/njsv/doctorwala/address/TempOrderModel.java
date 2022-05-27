package com.njsv.doctorwala.address;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TempOrderModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("location_id")
    @Expose
    private String locationId;
    @SerializedName("razorpay_payment_id")
    @Expose
    private String razorpayPaymentId;
    @SerializedName("deladd")
    @Expose
    private String deladd;
    @SerializedName("del_time")
    @Expose
    private String delTime;
    @SerializedName("dat")
    @Expose
    private String dat;
    @SerializedName("del_amt")
    @Expose
    private String delAmt;
    @SerializedName("pstatus")
    @Expose
    private String pstatus;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("sub_cat_id")
    @Expose
    private String subCatId;
    @SerializedName("schedule")
    @Expose
    private String schedule;
    @SerializedName("charge")
    @Expose
    private String charge;
    @SerializedName("booking_type")
    @Expose
    private String bookingType;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("unit_in")
    @Expose
    private String unitIn;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("sname")
    @Expose
    private String sname;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("orderdate")
    @Expose
    private String orderdate;
    @SerializedName("status_updatedate")
    @Expose
    private String statusUpdatedate;
    @SerializedName("cancelreason")
    @Expose
    private String cancelreason;
    @SerializedName("paymentstatus")
    @Expose
    private String paymentstatus;
    @SerializedName("paidon")
    @Expose
    private String paidon;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("totalamount")
    @Expose
    private String totalamount;
    @SerializedName("total_redeem_amount")
    @Expose
    private String totalRedeemAmount;
    @SerializedName("deliverycharge")
    @Expose
    private String deliverycharge;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("service")
    @Expose
    private String service;
    @SerializedName("aadhar_no")
    @Expose
    private String aadharNo;
    @SerializedName("u_date")
    @Expose
    private String uDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getRazorpayPaymentId() {
        return razorpayPaymentId;
    }

    public void setRazorpayPaymentId(String razorpayPaymentId) {
        this.razorpayPaymentId = razorpayPaymentId;
    }

    public String getDeladd() {
        return deladd;
    }

    public void setDeladd(String deladd) {
        this.deladd = deladd;
    }

    public String getDelTime() {
        return delTime;
    }

    public void setDelTime(String delTime) {
        this.delTime = delTime;
    }

    public String getDat() {
        return dat;
    }

    public void setDat(String dat) {
        this.dat = dat;
    }

    public String getDelAmt() {
        return delAmt;
    }

    public void setDelAmt(String delAmt) {
        this.delAmt = delAmt;
    }

    public String getPstatus() {
        return pstatus;
    }

    public void setPstatus(String pstatus) {
        this.pstatus = pstatus;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(String subCatId) {
        this.subCatId = subCatId;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnitIn() {
        return unitIn;
    }

    public void setUnitIn(String unitIn) {
        this.unitIn = unitIn;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public String getStatusUpdatedate() {
        return statusUpdatedate;
    }

    public void setStatusUpdatedate(String statusUpdatedate) {
        this.statusUpdatedate = statusUpdatedate;
    }

    public String getCancelreason() {
        return cancelreason;
    }

    public void setCancelreason(String cancelreason) {
        this.cancelreason = cancelreason;
    }

    public String getPaymentstatus() {
        return paymentstatus;
    }

    public void setPaymentstatus(String paymentstatus) {
        this.paymentstatus = paymentstatus;
    }

    public String getPaidon() {
        return paidon;
    }

    public void setPaidon(String paidon) {
        this.paidon = paidon;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }

    public String getTotalRedeemAmount() {
        return totalRedeemAmount;
    }

    public void setTotalRedeemAmount(String totalRedeemAmount) {
        this.totalRedeemAmount = totalRedeemAmount;
    }

    public String getDeliverycharge() {
        return deliverycharge;
    }

    public void setDeliverycharge(String deliverycharge) {
        this.deliverycharge = deliverycharge;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }

    public String getuDate() {
        return uDate;
    }

    public void setuDate(String uDate) {
        this.uDate = uDate;
    }
}
