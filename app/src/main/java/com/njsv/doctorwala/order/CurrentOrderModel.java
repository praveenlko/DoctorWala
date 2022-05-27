package com.njsv.doctorwala.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrentOrderModel {
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
    private Object locationId;
    @SerializedName("razorpay_payment_id")
    @Expose
    private Object razorpayPaymentId;
    @SerializedName("deladd")
    @Expose
    private Object deladd;
    @SerializedName("del_time")
    @Expose
    private Object delTime;
    @SerializedName("dat")
    @Expose
    private Object dat;
    @SerializedName("del_amt")
    @Expose
    private Object delAmt;
    @SerializedName("pstatus")
    @Expose
    private Object pstatus;
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
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("paymentmethod")
    @Expose
    private String paymentmethod;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("orderdate")
    @Expose
    private String orderdate;
    @SerializedName("status_updatedate")
    @Expose
    private Object statusUpdatedate;
    @SerializedName("cancelreason")
    @Expose
    private Object cancelreason;
    @SerializedName("paymentstatus")
    @Expose
    private Object paymentstatus;
    @SerializedName("paidon")
    @Expose
    private String paidon;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("totalamount")
    @Expose
    private String totalamount;
    @SerializedName("deliverycharge")
    @Expose
    private String deliverycharge;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("color")
    @Expose
    private String color;

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

    public Object getLocationId() {
        return locationId;
    }

    public void setLocationId(Object locationId) {
        this.locationId = locationId;
    }

    public Object getRazorpayPaymentId() {
        return razorpayPaymentId;
    }

    public void setRazorpayPaymentId(Object razorpayPaymentId) {
        this.razorpayPaymentId = razorpayPaymentId;
    }

    public Object getDeladd() {
        return deladd;
    }

    public void setDeladd(Object deladd) {
        this.deladd = deladd;
    }

    public Object getDelTime() {
        return delTime;
    }

    public void setDelTime(Object delTime) {
        this.delTime = delTime;
    }

    public Object getDat() {
        return dat;
    }

    public void setDat(Object dat) {
        this.dat = dat;
    }

    public Object getDelAmt() {
        return delAmt;
    }

    public void setDelAmt(Object delAmt) {
        this.delAmt = delAmt;
    }

    public Object getPstatus() {
        return pstatus;
    }

    public void setPstatus(Object pstatus) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPaymentmethod() {
        return paymentmethod;
    }

    public void setPaymentmethod(String paymentmethod) {
        this.paymentmethod = paymentmethod;
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

    public Object getStatusUpdatedate() {
        return statusUpdatedate;
    }

    public void setStatusUpdatedate(Object statusUpdatedate) {
        this.statusUpdatedate = statusUpdatedate;
    }

    public Object getCancelreason() {
        return cancelreason;
    }

    public void setCancelreason(Object cancelreason) {
        this.cancelreason = cancelreason;
    }

    public Object getPaymentstatus() {
        return paymentstatus;
    }

    public void setPaymentstatus(Object paymentstatus) {
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
}
