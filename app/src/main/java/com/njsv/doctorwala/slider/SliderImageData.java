package com.njsv.doctorwala.slider;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SliderImageData {
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("id")
    @Expose
    private Object id;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }
}
