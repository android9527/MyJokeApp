package com.android.jokeapp.activity;

import com.android.jokeapp.common.Constants;
import com.openmediation.sdk.InitCallback;
import com.openmediation.sdk.InitConfiguration;
import com.openmediation.sdk.OmAds;
import com.openmediation.sdk.interstitial.InterstitialAd;
import com.openmediation.sdk.splash.SplashAd;
import com.openmediation.sdk.splash.SplashAdListener;
import com.openmediation.sdk.utils.error.Error;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class BaseFragmentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSDK();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void requestPermissions() {
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                int permission = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.LOCATION_HARDWARE, Manifest.permission.READ_PHONE_STATE,
                                    Manifest.permission.WRITE_SETTINGS, Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_CONTACTS}, 0x0010);
                }

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION}, 0x0010);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        OmAds.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        OmAds.onPause(this);
    }

    private void initSDK() {
        InitConfiguration configuration = new InitConfiguration.Builder()
                .appKey(Constants.APP_KEY)
                .preloadAdTypes(OmAds.AD_TYPE.INTERSTITIAL)
                .build();
        OmAds.init(this, configuration, new InitCallback() {

            // Invoked when the initialization is successful.
            @Override
            public void onSuccess() {
            }

            // Invoked when the initialization is failed.
            @Override
            public void onError(Error error) {
            }
        });
    }

    protected void showIsAd() {
        InterstitialAd.showAd();
    }

    protected void loadSplash(String pid) {
        SplashAd.setSplashAdListener(pid, new SplashAdListener() {
            @Override
            public void onSplashAdLoad(String s) {

            }

            @Override
            public void onSplashAdFailed(String s, String s1) {

            }

            @Override
            public void onSplashAdClicked(String s) {

            }

            @Override
            public void onSplashAdShowed(String s) {

            }

            @Override
            public void onSplashAdShowFailed(String s, String s1) {

            }

            @Override
            public void onSplashAdTick(String s, long l) {

            }

            @Override
            public void onSplashAdDismissed(String s) {
                SplashAd.loadAd(s);
            }
        });
        SplashAd.loadAd(pid);
    }
}
