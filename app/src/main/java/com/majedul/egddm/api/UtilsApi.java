package com.majedul.egddm.api;


public class UtilsApi {


    //public static final String BASE_URL_API = "http://api.dev.stickerdriver.com/";
    // public static final String BASE_URL_API = "http://api.dev.stickerdriver.com/v1/";
    public static final String BASE_URL_API = "http://api.dev.stickerdriver.com/";
//public static final String BASE_URL_API = BuildConfig.BASE_URL;
// public static final String TEST_URL ="https://api.androidhive.info/";


    // Mendeklarasikan Interface BaseApiService
    public static BaseApiService getAPIService() {

        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);


    }

}
