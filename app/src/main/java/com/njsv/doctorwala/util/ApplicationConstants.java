package com.njsv.doctorwala.util;

public enum ApplicationConstants {

    INSTANCE;
    public String SITE_URL = "https://doctorwala.in/";
    public String API_URL = SITE_URL + "API/";
    public String CATEGORY_IMAGES = SITE_URL + "assets/img/cat_img/";
    public String PROFILE_VIEW_IMAGES = SITE_URL + "assets/img/enquiry/";
    public String OFFER_IMAGES = SITE_URL + "assets/img/offer_img/";

    public String escapeMetaCharacters(String inputString) {
        final String[] metaCharacters = {"\\", "^", "$", "{", "}", "[", "]", "(", ")", ".", "*", "+", "?", "|", "<", ">", "-", "&", "%", "'"};

        for (int i = 0; i < metaCharacters.length; i++) {
            if (inputString.contains(metaCharacters[i])) {
                inputString = inputString.replace(metaCharacters[i], "\\" + metaCharacters[i]);
            }
        }
        return inputString;
    }


}
