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
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executor;

import javax.crypto.KeyGenerator;

public class MainActivity extends AppCompatActivity {

    private ExafeBioAuth exafeBioAuth;

    private LinearLayout layout;
    private Button button;
    private TextView title;
    private Button checkBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        exafeBioAuth = ExafeBioAuth.getInstance();

        layout = new LinearLayout(this);
        title = new TextView(this);
        button = new Button(this);
        checkBtn = new Button(this);

        ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        layout.setLayoutParams(layoutParams);
        layout.setOrientation(LinearLayout.VERTICAL);

        title.setText("생체 인증 테스트 앱");

        button.setText("생체 인증");
        checkBtn.setText("변화감지");

        layout.addView(title);
        layout.addView(button);
        layout.addView(checkBtn);

        setContentView(layout);

        button.setOnClickListener(view -> {
            exafeBioAuth.authenticate(this , getApplicationContext());
        });

        checkBtn.setOnClickListener(view -> {
            if(exafeBioAuth.check()){
                Log.d("MY_APP", "인증 변화 감지안됨");
            }else{
                Log.d("MY_APP", "인증 변화 감지됨");
            }
        });
    }

}