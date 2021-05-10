package com.android.jokeapp.fragment;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.jokeapp.R;
import com.android.jokeapp.common.Constants;
import com.android.jokeapp.model.TextModel;
import com.android.jokeapp.util.ApiUtil;
import com.android.jokeapp.util.JsonParser;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.speech.setting.IatSettings;
import com.iflytek.sunflower.FlowerCollector;

public class FragmentCharAt extends FragmentBase implements View.OnClickListener,
        ChatMsgViewAdapter.OnClickContentCallback {
    private static final String TAG = "FragmentCharAt";

    // 语音听写对象
    private SpeechRecognizer mIat;

    // 语音听写UI
    private RecognizerDialog iatDialog;

    @ViewInject(id = R.id.btn_send)
    private Button mBtnSend;

    @ViewInject(id = R.id.et_sendmessage)
    private EditText mEditTextContent;

    @ViewInject(id = R.id.listview)
    private ListView mListView;

    private ChatMsgViewAdapter mAdapter;

    private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();

    @ViewInject(id = R.id.ivPopUp)
    private ImageView chattingModeBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 启动activity时不自动弹出软键盘
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        FinalActivity.initInjectedView(this, v);
        initInsetTop(v);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSpeechRecognizer();
        initSpeechSynthesis();
        initView();
        initData();
    }

    /**
     * 语音识别
     */
    private void initSpeechRecognizer() {
        // 初始化识别对象
        mIat = SpeechRecognizer.createRecognizer(getActivity(), mInitListener);
        // 初始化听写Dialog,如果只使用有UI听写功能,无需创建SpeechRecognizer
        iatDialog = new RecognizerDialog(getActivity(), mInitListener);
        initSharedPreferences();
        // 设置参数
        setParam();
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败,错误码：" + code);
            }
        }
    };

    public void initView() {
        mBtnSend.setOnClickListener(this);
        chattingModeBtn.setOnClickListener(this);
    }

    public void initData() {
        mAdapter = new ChatMsgViewAdapter(getActivity(), mDataArrays);
        mListView.setAdapter(mAdapter);
        mAdapter.setOnClickContentCallback(this);
    }

    private Handler mGetMessageHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case Constants.MESSAGE_WHAT_SUCCESS:
                    TextModel model = (TextModel) msg.obj;
                    ChatMsgEntity entity = new ChatMsgEntity();
                    entity.setName("小薇");
                    entity.setMsgType(true);
                    entity.setText(model.getText());
                    mAdapter.addItem(entity);
                    mListView.setSelection(mListView.getCount() - 1);
                    break;
                case Constants.MESSAGE_WHAT_EXCEPTION:
                    // TODO
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_send:
                send();
                break;
            case R.id.ivPopUp:
                // 显示听写对话框
                iatDialog.setListener(recognizerDialogListener);
                iatDialog.show();
                showTip(getString(R.string.text_begin));
                break;
        }
    }

    private void send() {
        String contString = mEditTextContent.getText().toString();
        if (contString.length() > 0) {
            ChatMsgEntity entity = new ChatMsgEntity();
            entity.setName("我");
            entity.setMsgType(false);
            entity.setText(contString);
            mDataArrays.add(entity);
            mAdapter.notifyDataSetChanged();
            ApiUtil.executeMessageTask(mGetMessageHandler, contString);
            mEditTextContent.setText("");
            mListView.setSelection(mListView.getCount() - 1);
        }

        View currentFocus = getActivity().getCurrentFocus();
        if (currentFocus != null) {
            ((InputMethodManager) getActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(currentFocus.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 参数设置
     *
     * @return
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        String lag = mSharedPreferences.getString("iat_language_preference", "mandarin");
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);
        }
        // 设置语音前端点
        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));
        // 设置语音后端点
        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));
        // 设置标点符号
        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));
        // 设置音频保存路径
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()
                + "/iflytek/wavaudio.pcm");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 退出时释放连接
        mIat.cancel();
        mIat.destroy();
        releaseTTS();
    }

    @Override
    public void onResume() {
        // if(mIat != null){
        // ret = mIat.startListening(recognizerListener);
        // }
        // 移动数据统计分析
        FlowerCollector.onResume(getActivity());
        FlowerCollector.onPageStart("FragmentCharAt");
        super.onResume();
    }

    @Override
    public void onPause() {
        // if(mIat != null){
        // mIat.stopListening();
        // }
        // 移动数据统计分析
        FlowerCollector.onPageEnd("FragmentCharAt");
        FlowerCollector.onPause(getActivity());
        super.onPause();
    }

    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener recognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            String text = JsonParser.parseIatResult(results.getResultString());
            mEditTextContent.append(text);
            mEditTextContent.setSelection(mEditTextContent.length());
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            showTip(error.getPlainDescription(true));
        }

    };

    @Override
    public void onClickContentCallback(int position) {
        final ChatMsgEntity chatMsgEntity = mAdapter.getItem(position);
        startSpeaking(chatMsgEntity.getText());
    }


}
