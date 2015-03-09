package com.android.jokeapp.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.android.jokeapp.R;
import com.android.jokeapp.activity.DrawerLayoutActivity;
import com.android.jokeapp.util.UiUtil;
import com.android.jokeapp.util.SystemBarTintManager;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.speech.setting.IatSettings;

public class FragmentBase extends SherlockFragment
{
    
private static final String TAG = "FragmentBase";
    
    public void initInsetTop(View rootView)
    {
        // SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
        // SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        // rootView.setPadding(0,
        // config.getPixelInsetTop(true),
        // config.getPixelInsetRight(),
        // config.getPixelInsetBottom());
        // rootView.requestLayout();
    }
    
    public FragmentBase()
    {
    }
    
    protected DrawerLayoutActivity drawerLayoutActivity;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        drawerLayoutActivity = (DrawerLayoutActivity)getActivity();
    }
    
    protected void initSharedPreferences()
    {
        mSharedPreferences = getActivity().getSharedPreferences(IatSettings.PREFER_NAME, Activity.MODE_PRIVATE);
        mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
    }
    
    protected void releaseTTS(){
     // 退出时释放连接
        mTts.stopSpeaking();
        // 退出时释放连接
        mTts.destroy();
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
    protected void initSpeechSynthesis()
    {
        initSharedPreferences();
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(getActivity(), mTtsInitListener);
        
        // 云端发音人名称列表
        cloudVoicersEntries = getResources().getStringArray(R.array.voicer_cloud_entries);
        cloudVoicersValue = getResources().getStringArray(R.array.voicer_cloud_values);
        
        // TODO
        setSpeechSynthesisParam();
    }
    
    /**
     * 初期化监听。
     */
    private InitListener mTtsInitListener = new InitListener()
    {
        @Override
        public void onInit(int code)
        {
            if (code != ErrorCode.SUCCESS)
            {
                showTip("初始化失败,错误码：" + code);
            }
        }
    };
    
    protected void showTip(final String str)
    {
        mToast.setText(str);
        mToast.show();
    }
    
    /**
     * 设置语音合成参数
     */
    protected void setSpeechSynthesisParam()
    {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 设置合成
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD))
        {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 设置发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            
            // 设置语速
            mTts.setParameter(SpeechConstant.SPEED, mSharedPreferences.getString("speed_preference", "50"));
            
            // 设置音调
            mTts.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));
            
            // 设置音量
            mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "50"));
            
            // 设置播放器音频流类型
            mTts.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "3"));
        }
        else
        {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            // 设置发音人 voicer为空默认通过语音+界面指定发音人。
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");
        }
        
    }
    
    /**
     * 语音合成
     * 
     * @param position
     */
    protected void startSpeaking(String text)
    {
        stopSpeaking();
        int code = mTts.startSpeaking(text, mTtsListener);
        if (code != ErrorCode.SUCCESS)
        {
            if (code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED)
            {
                // 未安装则跳转到提示安装页面
                // mInstaller.install();
            }
            else
            {
                showTip("语音合成失败,错误码: " + code);
            }
        }
    }
    
    /**
     * 停止语音合成
     */
    public void stopSpeaking(){
        if(mTts != null && mTts.isSpeaking())
            mTts.stopSpeaking();
    }
    
    
    protected boolean isSpeaking = false;
    
    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener()
    {
        @Override
        public void onSpeakBegin()
        {
            showTip("开始播放");
        }
        
        @Override
        public void onSpeakPaused()
        {
            showTip("暂停播放");
        }
        
        @Override
        public void onSpeakResumed()
        {
            showTip("继续播放");
        }
        
        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos, String info)
        {
            mPercentForBuffering = percent;
            showTip(String.format(getString(R.string.tts_toast_format), mPercentForBuffering, mPercentForPlaying));
        }
        
        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos)
        {
            mPercentForPlaying = percent;
            showTip(String.format(getString(R.string.tts_toast_format), mPercentForBuffering, mPercentForPlaying));
        }
        
        @Override
        public void onCompleted(SpeechError error)
        {
            if (error == null)
            {
                showTip("播放完成");
            }
            else if (error != null)
            {
                showTip(error.getPlainDescription(true));
            }
        }
        
        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj)
        {
            
        }
    };
    
}
