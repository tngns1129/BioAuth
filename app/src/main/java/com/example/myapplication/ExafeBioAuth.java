package com.example.myapplication;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.Executor;

public class ExafeBioAuth {

    Context context;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private CharSequence NegativeButtonText = "modify";

        public static final int BIOMETRIC_AVAILABLE = 1;
    public static final int BIOMETRIC_ERROR_NO_HARDWARE = 2;
    public static final int BIOMETRIC_ERROR_HW_UNAVAILABLE = 3;
    public static final int BIOMETRIC_ERROR_NONE_ENROLLED = 4;

    public static final int BIOMETRIC_SUCCESS = 11;
    public static final int BIOMETRIC_FAIL = 12;
    public ExafeBioAuth(Context context){
        this.context = context;
    }

    public void init() {
        executor = ContextCompat.getMainExecutor(context);


        //안드로이드 11 이상
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setConfirmationRequired(true)
                .setNegativeButtonText(NegativeButtonText)
                .build();


        //안드로이드 10 이하

        biometricPrompt = new BiometricPrompt((FragmentActivity) context,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Log.d("MY_APP_TAG","error" + Integer.toString(errorCode));
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Log.d("MY_APP_TAG","succeed");
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Log.d("MY_APP_TAG","fail");
            }

        });


    }
    public int auth() {

        //authcheck();
        biometricPrompt.authenticate(promptInfo);
        return 0;

    }
    public int authcheck() {
        /*
         * int	BIOMETRIC_STRONG
         * Android CDD에서 정의한 클래스 3 (이전의 Strong ) 요구 사항을 충족하거나 초과하는 기기의 모든 생체 인식(예: 지문, 홍채 또는 얼굴) .

         * int	BIOMETRIC_WEAK
         * Android CDD에서 정의한 클래스 2 (이전의 Weak ) 요구 사항을 충족하거나 초과하는 기기의 모든 생체 인식(예: 지문, 홍채 또는 얼굴)입니다 .

         * int   DEVICE_CREDENTIAL
         * 장치를 보호하는 데 사용되는 비생체인식 자격 증명(예: PIN, 패턴 또는 비밀번호)입니다.
         *
         * */
        // 생체 인식 인증을 사용할 수 있는지 확인
        BiometricManager biometricManager = BiometricManager.from(context);
        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                return BIOMETRIC_AVAILABLE;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                return BIOMETRIC_ERROR_NO_HARDWARE;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                return BIOMETRIC_ERROR_HW_UNAVAILABLE;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // Prompts the user to create credentials that your app accepts.
                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BIOMETRIC_STRONG);
                context.startActivity(enrollIntent);
                return BIOMETRIC_ERROR_NONE_ENROLLED;
        }
        return 0;
    }

}
