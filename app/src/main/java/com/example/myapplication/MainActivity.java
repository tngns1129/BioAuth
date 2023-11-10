package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity implements ExafeBioAuthInterface {

    private ExafeBioAuth exafeBioAuth;
    private LinearLayout layout;
    private Button button;
    private TextView title;
    private String authTitle;
    private String authSubTitle;
    private String authExplanation;
    private String authCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = new LinearLayout(this);
        title = new TextView(this);
        button = new Button(this);

        ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        layout.setLayoutParams(layoutParams);
        layout.setOrientation(LinearLayout.VERTICAL);

        title.setText("생체 인증 테스트 앱");

        button.setText("생체 인증");

        layout.addView(title);
        layout.addView(button);

        setContentView(layout);

        authTitle = "Biometric title";
        authSubTitle = "Biometric sub title";
        authExplanation = "Biometric explanation";
        authCancel = "cancel";

        exafeBioAuth = ExafeBioAuth.getInstance();
        exafeBioAuth.setText(authTitle, authSubTitle, authExplanation, authCancel);

        try{
            EventBus.getDefault().register(this);
        } catch (Exception e){}

        button.setOnClickListener(view -> {
            exafeBioAuth.authenticate(this , getApplicationContext());
        });

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void bioEvent(BioEvent event) {
        switch(event.helloEventBus){
            case 0: //생체 인증 성공
                Log.d("MY_APP_MAIN","success");
                break;
            case 1: //생체 인증 실패
                Log.d("MY_APP_MAIN","fail");
                break;
            case 2: //생체 인증 취소
                Log.d("MY_APP_MAIN","cancel");
                break;
            case 99: //지문 추가됨
                Log.d("MY_APP_MAIN","detect biometric changes");

        }
    }
}