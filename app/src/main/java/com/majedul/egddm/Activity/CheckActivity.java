package com.majedul.egddm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.majedul.egddm.R;
import com.majedul.egddm.api.BaseApiService;
import com.majedul.egddm.api.UtilsApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckActivity extends AppCompatActivity {
    TextView tv_data;
    BaseApiService mApiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);


        Intent mIntent = getIntent();
        String data = mIntent.getStringExtra("data");
        tv_data = findViewById(R.id.data);
        tv_data.setText(data);

        String string = data;
        String[] parts = string.split(",");
        String snid_number = parts[0];
        String sname = parts[1];
        String scard_number = parts[2];
        String sref = parts[3];


        String[] nid_parts = snid_number.split(": ");
        final String nid_number = nid_parts[1];
        String[] card_parts = scard_number.split(": ");
        final String card_number = card_parts[1];
        String[] ref_parts = sref.split(": ");
        String sref_number = ref_parts[1];
        int ref_number = Integer.parseInt(sref_number);

      //  Toast.makeText(getApplicationContext(), "reference number :" + ref_number, Toast.LENGTH_SHORT).show();


        mApiService = UtilsApi.getAPIService();
        Call<ResponseBody> call = mApiService.getUserDetails(ref_number);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if (response.isSuccessful()) {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());

                        int Nid = jsonObject.getInt("nid");
                        String jsonnid = Integer.toString(Nid);
                        if (jsonnid.equals(nid_number)) {
                            Toast.makeText(getApplicationContext(), "nid match " + nid_number, Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getApplicationContext(), "nid not match" + nid_number, Toast.LENGTH_SHORT).show();

                        String card = jsonObject.getString("card_no");
                        if (card.equals(card_number)) {
                            Toast.makeText(getApplicationContext(), "card match " + card_number, Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getApplicationContext(), "card not match" + card_number, Toast.LENGTH_SHORT).show();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } else

                    Toast.makeText(getApplicationContext(), "problem in api", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }
}
