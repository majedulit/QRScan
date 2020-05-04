package com.majedul.egddm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.majedul.egddm.R;

public class SignIn extends AppCompatActivity {

    EditText et_email,et_password;
    Button bt_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        et_email=findViewById(R.id.email);
        et_password=findViewById(R.id.password);
        bt_login=findViewById(R.id.btnLogin);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}
