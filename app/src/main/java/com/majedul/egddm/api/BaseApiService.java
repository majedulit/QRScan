package com.majedul.egddm.api;

import com.majedul.egddm.model.login;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BaseApiService {


    @GET("api/qr-code/{Id}")
    Call<ResponseBody> getUserDetails(@Path("Id") int id);


    //    @POST("apis/login")
//    Call<ResponseBody> loginRequest(@Body login login);
   // @Headers("Content-Type: application/json")
    @POST("apis/login")
    Call<ResponseBody> loginRequest(@Body RequestBody body);
}
