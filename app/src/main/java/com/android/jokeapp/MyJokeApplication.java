package com.android.jokeapp;

import android.app.Application;

import com.android.jokeapp.util.FakeX509TrustManager;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.iflytek.cloud.SpeechUtility;

public class MyJokeApplication extends Application {

    @Override
    public void onCreate() {
        FakeX509TrustManager.allowAllSSL();
        // 应用程序入口处调用,避免手机内存过小，杀死后台进程,造成SpeechUtility对象为null
        // 注意：此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，请增加参数：SpeechConstant.FORCE_LOGIN+"=true" 参数间使用“,”分隔。
        // 设置你申请的应用appid
        SpeechUtility.createUtility(this, "appid=" + getString(R.string.app_id));
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        super.onCreate();
    }
}