package com.example.myapplication;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.databinding.ActivitySignBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SignActivity extends AppCompatActivity implements ExafeBioAuthInterface {

    private ActivitySignBinding binding;
    private ExafeBioAuth exafeBioAuth;
    private String authTitle;
    private String authSubTitle;
    private String authExplanation;
    private String authCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authTitle = "Biometric title";
        authSubTitle = "Biometric sub title";
        authExplanation = "Biometric explanation";
        authCancel = "cancel";



        exafeBioAuth = ExafeBioAuth.getInstance();
        exafeBioAuth.setText(authTitle, authSubTitle, authExplanation, authCancel);

        try{
            EventBus.getDefault().register(this);
        } catch (Exception e){}

        binding.btn.setOnClickListener(view ->{
            AlertDialog.Builder menu = new AlertDialog.Builder(SignActivity.this);
            menu.setIcon(R.mipmap.ic_launcher);
            menu.setTitle("지문 사용"); // 제목
            menu.setMessage("로그인시 지문인식 사용 하시겠습니까?"); // 문구

            // 확인 버튼
            menu.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(exafeBioAuth.check()){
                        exafeBioAuth.authenticate(SignActivity.this , getApplicationContext());
                    } else{
                        AlertDialog.Builder menu = new AlertDialog.Builder(SignActivity.this);
                        menu.setIcon(R.mipmap.ic_launcher);
                        menu.setTitle("지문 변경"); // 제목
                        menu.setMessage("지문이 변경되었습니다. 다시 로그인해주세요"); // 문구

                        // 확인 버튼
                        menu.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // dialog 제거
                                exafeBioAuth.authenticate(SignActivity.this , getApplicationContext());
                                dialog.dismiss();
                            }
                        });
                        // 취소 버튼
                        menu.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // dialog 제거
                                dialog.dismiss();
                            }
                        });

                        menu.show();
                    }

                    // dialog 제거
                    dialog.dismiss();
                }
            });

            // 취소 버튼
            menu.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // dialog 제거
                    dialog.dismiss();
                }
            });

            menu.show();
        });

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void bioEvent(BioEvent event) {
        switch(event.helloEventBus){
            case 0: //생체 인증 성공
                Log.d("MY_APP_MAIN","success");
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
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