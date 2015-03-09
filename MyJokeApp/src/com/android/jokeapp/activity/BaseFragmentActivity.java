package com.android.jokeapp.activity;

import net.youmi.android.AdManager;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.banner.AdViewListener;
import net.youmi.android.spot.SpotDialogListener;
import net.youmi.android.spot.SpotManager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.android.jokeapp.R;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

public class BaseFragmentActivity extends SherlockFragmentActivity
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
        AdManager.getInstance(this).init("73057fb81878f2ba",
                    "f2707618edb7c2a4");
     // 设置是否开启有米广告SDK Log。默认值为true，标识开启， false为关闭log
        AdManager.getInstance(this).setEnableDebugLog(true);
        
     // 设置是否在通知栏显示下载相关提示。默认为true，标识开启；设置为false则关闭。
        AdManager.setIsDownloadTipsDisplayOnNotification(false);

        // 获取是否在通知栏显示下载进度的值。
//        AdManager.isDownloadTipsDisplayOnNotification();
    }
    
    protected void showBanner() {

        
        
     // 插播接口调用
        // 开发者可以到开发者后台设置展示频率，需要到开发者后台设置页面（详细信息->业务信息->无积分广告业务->高级设置）
        // 自4.03版本增加云控制是否开启防误点功能，需要到开发者后台设置页面（详细信息->业务信息->无积分广告业务->高级设置）

        // 加载插播资源
        SpotManager.getInstance(this).loadSpotAds();
        // 设置展示超时时间，加载超时则不展示广告，默认0，代表不设置超时时间
        SpotManager.getInstance(this).setSpotTimeout(10000);// 5秒
                
            // 展示插播广告，可以不调用loadSpot独立使用
        SpotManager.getInstance(this).showSpotAds(this, new SpotDialogListener() {
            @Override
            public void onShowSuccess() {
            Log.i("SpotAd", "展示成功");
            }

            @Override
            public void onShowFailed() {
            Log.i("SpotAd", "展示失败");
            }

            @Override
            public void onSpotClosed()
            {
                // TODO Auto-generated method stub
                
            }
        });  
        
        
        
        // 广告条接口调用（适用于应用）
        // 将广告条adView添加到需要展示的layout控件中
         LinearLayout adLayout = (LinearLayout) findViewById(R.id.adLayout);
         AdView adView = new AdView(this, AdSize.FIT_SCREEN);
      // 监听广告条接口
         adView.setAdListener(new AdViewListener() {

             @Override
             public void onSwitchedAd(AdView arg0) {
                 Log.i("YoumiAdDemo", "广告条切换");
             }

             @Override
             public void onReceivedAd(AdView arg0) {
                 Log.i("YoumiAdDemo", "请求广告成功");

             }

             @Override
             public void onFailedToReceivedAd(AdView arg0) {
                 Log.i("YoumiAdDemo", "请求广告失败");
             }
         });
         adLayout.addView(adView);

        // 广告条接口调用（适用于游戏）

        // 实例化LayoutParams(重要)
//        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
//                FrameLayout.LayoutParams.WRAP_CONTENT);
//        // 设置广告条的悬浮位置
//        layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT; // 这里示例为右下角
        // 实例化广告条
//        AdView adView = new AdView(this, AdSize.FIT_SCREEN);
        // 调用Activity的addContentView函数

        // 监听广告条接口
//        adView.setAdListener(new AdViewListener() {
//
//            @Override
//            public void onSwitchedAd(AdView arg0) {
//                Log.i("YoumiAdDemo", "广告条切换");
//            }
//
//            @Override
//            public void onReceivedAd(AdView arg0) {
//                Log.i("YoumiAdDemo", "请求广告成功");
//
//            }
//
//            @Override
//            public void onFailedToReceivedAd(AdView arg0) {
//                Log.i("YoumiAdDemo", "请求广告失败");
//            }
//        });
//        this.addContentView(adView, layoutParams);
    }

    @Override
    public void onBackPressed() {
        // 如果有需要，可以点击后退关闭插播广告。
        if (!SpotManager.getInstance(this).disMiss()) {
            // 弹出退出窗口，可以使用自定义退屏弹出和回退动画,参照demo,若不使用动画，传入-1
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        // 如果不调用此方法，则按home键的时候会出现图标无法显示的情况。
        SpotManager.getInstance(this).onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        SpotManager.getInstance(this).onDestroy();
        super.onDestroy();
    }
}
