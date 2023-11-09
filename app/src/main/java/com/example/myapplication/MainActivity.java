package com.example.myapplication;


import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    Button btn;
    Button checkBtn;
    ExafeBioAuth exafeBioAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        exafeBioAuth = new ExafeBioAuth(this);
        exafeBioAuth.init();

        this.InitView();
        this.SetListener();



    }

    public void InitView(){
        btn = (Button)findViewById(R.id.btn);
    }

    public void SetListener(){

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int result = exafeBioAuth.authcheck();
                Log.d("MY_APP_TAG_MAIN", Integer.toString(result));
                if(result == exafeBioAuth.BIOMETRIC_AVAILABLE){
                    Log.d("MY_APP_TAG_MAIN", "생체 인증 가능");
                }
                else if(result == exafeBioAuth.BIOMETRIC_ERROR_NO_HARDWARE){
                    Log.d("MY_APP_TAG_MAIN", "생체 인증 불가 단말");
                }
                else if(result == exafeBioAuth.BIOMETRIC_ERROR_HW_UNAVAILABLE){
                    Log.d("MY_APP_TAG_MAIN", "생체 인증 현재 불가능");
                }
                else if(result == exafeBioAuth.BIOMETRIC_ERROR_NONE_ENROLLED){
                    Log.d("MY_APP_TAG_MAIN", "생체 인증 등록 필요");
                }

                exafeBioAuth.auth();



            }
        });

    }
}