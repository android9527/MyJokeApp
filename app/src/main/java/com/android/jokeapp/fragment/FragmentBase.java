package com.android.jokeapp.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.jokeapp.R;
import com.android.jokeapp.activity.DrawerLayoutActivity;
import com.android.jokeapp.common.Constants;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.speech.setting.IatSettings;
import com.openmediation.sdk.banner.AdSize;
import com.openmediation.sdk.banner.BannerAd;
import com.openmediation.sdk.banner.BannerAdListener;
import com.openmediation.sdk.splash.SplashAd;
import com.openmediation.sdk.splash.SplashAdListener;

public class FragmentBase extends Fragment {

    private static final String TAG = "FragmentBase";

    protected RelativeLayout mAdView;
    BannerAd mBannerAd;

    public void initInsetTop(View rootView) {
        // SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
        // SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        // rootView.setPadding(0,
        // config.getPixelInsetTop(true),
        // config.getPixelInsetRight(),
        // config.getPixelInsetBottom());
        // rootView.requestLayout();
    }

    public FragmentBase() {
    }

    protected DrawerLayoutActivity drawerLayoutActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawerLayoutActivity = (DrawerLayoutActivity) getActivity();
    }

    protected void initSharedPreferences() {
        mSharedPreferences = getActivity().getSharedPreferences(IatSettings.PREFER_NAME, Activity.MODE_PRIVATE);
        mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
    }

    protected void releaseTTS() {
        if (mTts != null) {
            // 退出时释放连接
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }
    }

    /**
     * 讯飞语音合成
     */
    // 语音合成对象
    private SpeechSynthesizer mTts;

    // 默认发音人
    private String voicer = "xiaoyan";

    private String[] cloudVoicersEntries;

    private String[] cloudVoicersValue;

    // 缓冲进度
    protected int mPercentForBuffering = 0;

    // 播放进度
    protected int mPercentForPlaying = 0;

    protected SharedPreferences mSharedPreferences;

    private Toast mToast;

    // 引擎类型
    protected String mEngineType = SpeechConstant.TYPE_CLOUD;

    /**
     * 语音合成
     */
    protected void initSpeechSynthesis() {
        initSharedPreferences();
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(getActivity(), mTtsInitListener);

        // 云端发音人名称列表
        cloudVoicersEntries = getResources().getStringArray(R.array.voicer_cloud_entries);
        cloudVoicersValue = getResources().getStringArray(R.array.voicer_cloud_values);

        setSpeechSynthesisParam();
    }

    /**
     * 初期化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败,错误码：" + code);
            }
        }
    };

    protected void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    /**
     * 设置语音合成参数
     */
    protected void setSpeechSynthesisParam() {
        if (mTts == null) {
            return;
        }
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            //支持实时音频返回，仅在synthesizeToUri条件下支持
            mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY, "1");
            //	mTts.setParameter(SpeechConstant.TTS_BUFFER_TIME,"1");

            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, mSharedPreferences.getString("speed_preference", "60"));
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "50"));
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");

        }
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "3"));
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "false");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "pcm");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.pcm");

    }

    /**
     * 语音合成
     *
     * @param
     */
    protected void startSpeaking(String text) {
        if (mTts == null) {
            return;
        }
        stopSpeaking();
        int code = mTts.startSpeaking(text, mTtsListener);
        if (code != ErrorCode.SUCCESS) {
            if (code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
                // 未安装则跳转到提示安装页面
                // mInstaller.install();
            } else {
                showTip("语音合成失败,错误码: " + code);
            }
        }
    }

    /**
     * 停止语音合成
     */
    public void stopSpeaking() {
        if (mTts != null && mTts.isSpeaking())
            mTts.stopSpeaking();
    }


    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {
            showTip("开始播放");
        }

        @Override
        public void onSpeakPaused() {
            showTip("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            showTip("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
            mPercentForBuffering = percent;
            showTip(String.format(getString(R.string.tts_toast_format), mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            mPercentForPlaying = percent;
            showTip(String.format(getString(R.string.tts_toast_format), mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                showTip("播放完成");
            } else if (error != null) {
                showTip(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (null != mTts) {
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }
        if (null != mAdView) {
            mAdView.removeAllViews();
        }
        if (null != mBannerAd) {
            mBannerAd.destroy();
        }
    }

    protected void showBanner(String pid) {
        if (mAdView != null) {
            mBannerAd = new BannerAd(getActivity(), pid, new BannerAdListener() {

                /**
                 * Invoked when Banner Ad are available.
                 */
                @Override
                public void onAdReady(View view) {
                    try {
                        if (null != view.getParent()) {
                            ((ViewGroup) view.getParent()).removeView(view);
                        }
                        mAdView.removeAllViews();
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                        mAdView.addView(view, layoutParams);
                    } catch (Exception e) {
                        Log.e("AdtDebug", e.getLocalizedMessage());
                    }
                }

                /**
                 * Invoked when the end user clicked on the Banner Ad
                 */
                @Override
                public void onAdClicked() {
                    // bannerAd clicked
                }

                /**
                 * Invoked when the call to load a Banner Ad has failed
                 * String error contains the reason for the failure.
                 */
                @Override
                public void onAdFailed(String error) {
                    // bannerAd fail
                }
            });
            mBannerAd.setAdSize(AdSize.SMART);
            mBannerAd.loadAd();
        }
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

    @Override
    public void onResume() {
        super.onResume();
        loadSplash(Constants.SPLASH_PID);
    }
}
