package com.majedul.egddm.api;


public class UtilsApi {


   // public static final String BASE_URL_API = "https://qrcode.easyddm.com/";
     //public static final String BASE_URL_API = BuildConfig.BASE_URL;

   public static final String BASE_URL_API = "https://easyddm.com/";
    //public static final String BASE_URL_API = "http://api.dev.stickerdriver.com/";


    public static BaseApiService getAPIService() {

        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);


    }

}
