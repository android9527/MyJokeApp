package com.android.jokeapp.activity;

import com.android.jokeapp.R;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class BaseFragmentActivity extends AppCompatActivity
{
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // 初始化接口，应用启动的时候调用
        // 参数：appId, appSecret, 调试模式
//        AdManager.getInstance(this).init("93348c2ae3be5483", "a0481c3c65f79729", true);
     // 初始化接口，应用启动的时候调用
        // 参数：appId, appSecret, 调试模式
//        AdManager.getInstance(this).init("73057fb81878f2ba",
//                    "f2707618edb7c2a4");
     // 设置是否开启有米广告SDK Log。默认值为true，标识开启， false为关闭log
//        AdManager.getInstance(this).setEnableDebugLog(true);
        
     // 设置是否在通知栏显示下载相关提示。默认为true，标识开启；设置为false则关闭。
//        AdManager.setIsDownloadTipsDisplayOnNotification(false);

        // 获取是否在通知栏显示下载进度的值。
//        AdManager.isDownloadTipsDisplayOnNotification();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
