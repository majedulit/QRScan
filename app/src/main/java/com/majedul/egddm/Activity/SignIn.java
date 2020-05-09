package com.majedul.egddm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.majedul.egddm.R;
import com.majedul.egddm.api.BaseApiService;
import com.majedul.egddm.api.NewUtilsApi;
import com.majedul.egddm.api.UtilsApi;
import com.majedul.egddm.model.login;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignIn extends AppCompatActivity implements Validator.ValidationListener {


    private static final int REQUEST_INTERNET = 200;
    public static final String PREFS_NAME = "LoginPrefs";
    private int user_type = 1;
    private String phone, password;
    private static final int REQUECT_CODE_SDCARD = 2;
    private static final int PERMISSION_REQUEST_CODE = 200;
    @NotEmpty
    EditText etPhone;
    @NotEmpty
    EditText etPassword;
    BaseApiService mApiService;
    private Validator validator;
    ProgressBar linlaHeaderProgress;
    AlertDialog dialog;
    Button btnsignin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        validator = new Validator(this);
        validator.setValidationListener(this);

        etPhone = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        btnsignin = findViewById(R.id.btnLogin);
        mApiService = UtilsApi.getAPIService();
        linlaHeaderProgress = (ProgressBar) findViewById(R.id.progressBar1);

        dialog = new AlertDialog.Builder(SignIn.this)
                .setTitle("Connection Failed")
                .setMessage("Please Check Your Internet Connection")
                .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Code for try again
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();
//        if (InternetConnection.checkConnection(SignIn.this)) {
//            // Its Available...
//
//        } else {
//            // Not Available...
//            // dialog.show();
//            dialogNoInternet();
//        }


        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getString("logged", "").toString().equals("logged")) {
            dialog.dismiss();
            startActivity(new Intent(SignIn.this, MainActivity.class));
            finish();
        }

        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
                phone = etPhone.getText().toString().trim();
                password = etPassword.getText().toString().trim();

                //  if(!phone.equals(null)&&!password.equals(null))
                if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password)) {
                    signin(phone, password);
                    // startActivity(new Intent(SignIn.this, MainActivity.class));
                    //  finish();
                }


            }
        });


    }

    private void signin(String phone, String password) {

        //ogin login = new login( "ramkrisnopurup@easyddm.com", "#ramKrisno987");
        login login = new login(phone, password);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", phone)
                .addFormDataPart("password", password)
                .build();

        mApiService = UtilsApi.getAPIService();
        mApiService.loginRequest(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        String access_token = (String) jsonRESULTS.get("auth_token");
                        String email = (String) jsonRESULTS.get("email");
                        String relativename = (String) jsonRESULTS.get("relativename");
                        int id = (int) jsonRESULTS.get("id");
                        int usergroup_id = (int) jsonRESULTS.get("usergroup_id");
                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("logged", "logged");
                        editor.putString("email", email);
                        editor.putString("relativename", relativename);
                        editor.putInt("Id", id);

                        editor.putString("access_token", access_token);
                        editor.commit();
                        linlaHeaderProgress.setVisibility(View.VISIBLE);
                        startActivity(new Intent(SignIn.this, MainActivity.class));
                        finish();


                    } catch (JSONException e) {
                        //Toast.makeText(SignIn.this, "Json exception :"+e.getMessage(), Toast.LENGTH_LONG).show();
                        Toast.makeText(SignIn.this, "Please provide correct phone and password", Toast.LENGTH_LONG).show();

                        e.printStackTrace();

                    } catch (IOException e) {
                        Toast.makeText(SignIn.this, "error : " + e.getMessage(), Toast.LENGTH_LONG).show();

                        e.printStackTrace();

                    }
                } else {
                    Toast.makeText(SignIn.this, "Please provide correct phone and password", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                Toast.makeText(SignIn.this, " " + t.toString(), Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_INTERNET) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //start audio recording or whatever you planned to do
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(SignIn.this, Manifest.permission.INTERNET)) {
                    //Show an explanation to the user *asynchronously*
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("This permission is important to internet access.")
                            .setTitle("Important permission required");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ActivityCompat.requestPermissions(SignIn.this, new String[]{Manifest.permission.INTERNET}, REQUEST_INTERNET);
                        }
                    });
                    ActivityCompat.requestPermissions(SignIn.this, new String[]{Manifest.permission.INTERNET}, REQUEST_INTERNET);
                } else {
                    //Never ask again and handle your app without permission.
                }
            }
        }
    }


//    private void startProcess() {
//        if (statusCheck()) {
//            dialogNoInternet();
//        } else {
//        }
//    }
//
//    private boolean statusCheck() {
//        if (InternetConnection.checkConnection(SignIn.this)) {
//            return  false;
//
//        }
//        else return true;
//
//    }


//    public void dialogNoInternet() {
//        Dialog dialog = new DialogUtils(this).buildDialogWarning(R.string.title_no_internet, R.string.msg_no_internet, R.string.TRY_AGAIN, R.string.CLOSE, R.drawable.img_no_internet, new CallbackDialog() {
//            @Override
//            public void onPositiveClick(Dialog dialog) {
//                dialog.dismiss();
//                retryOpenApplication();
//            }
//            @Override
//            public void onNegativeClick(Dialog dialog) {
//                finish();
//            }
//        });
//        dialog.show();
//    }
//    private void retryOpenApplication() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startProcess();
//            }
//        }, 1000);
//    }


    @Override
    public void onValidationSucceeded() {

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}