package com.android.jokeapp.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.android.jokeapp.common.Constants;
import com.android.jokeapp.model.TencentModel;
import com.android.jokeapp.model.TextModel;
import com.google.gson.Gson;

import android.os.Handler;
import android.os.Message;
import android.security.keystore.KeyInfo;

public class ApiUtil {
    public static final String APIKEY = "215f991c74c93189913fc5177d5906c5";

    public static final String URL = "http://api.turingos.cn/turingos/api/v2"
            + APIKEY + "&info=";

    private static final String CHARSET_NAME = "utf-8";

    private static final ExecutorService sExecutor = Executors
            .newFixedThreadPool(2);

    public static Gson gson = new Gson();

    public static HttpGet getHttpGet(String url) {
        HttpGet request = new HttpGet(url);
        return request;
    }

    public static String queryStringForGet(String url)
            throws ClientProtocolException, IOException {
        HttpGet request = getHttpGet(url);
        String result = null;
        HttpResponse response = getHttpResponse(request);
        if (200 == response.getStatusLine().getStatusCode()) {
            result = EntityUtils.toString(response.getEntity());
            return result;
        }
        return null;
    }

    public static HttpResponse getHttpResponse(HttpGet request)
            throws ClientProtocolException, IOException {
        // HttpResponse response = new DefaultHttpClient().execute(request);
        HttpClient defaultHttpClient = new DefaultHttpClient();
        // 请求超时
        defaultHttpClient.getParams().setParameter(
                CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
        // 读取超时

        defaultHttpClient.getParams().setParameter(
                CoreConnectionPNames.SO_TIMEOUT, 30000);
        HttpResponse response = defaultHttpClient.execute(request);
        return response;
    }

    public static void executeJokeMessageTask(final int count,
                                              final Handler handler, final int tag) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                getJokeMessages(count, handler, tag);
            }
        };
        sExecutor.execute(runnable);
    }

    public static void getJokeMessages(int count, Handler handler, int tag) {
        String info = null;
        try {
            if (tag == 0) {
//                info = URLEncoder.encode("讲个笑话", CHARSET_NAME);
                info = "讲个笑话";
            } else {
                info = URLEncoder.encode("冷笑话", CHARSET_NAME);
                info = "冷笑话";
            }
            List<TextModel> textModels = new ArrayList<TextModel>();
//            String requestUrl = URL + info;
            for (int i = 0; i < count; i++) {
//                String result = queryStringForGet(requestUrl);

                String result = TuLingUtils.httpPost(info);
                TextModel textModel = gson.fromJson(result, TextModel.class);
                textModel.initDingAndCaiNumber();
                textModels.add(textModel);
            }
            // return textModels;
            Message message = handler
                    .obtainMessage(Constants.MESSAGE_WHAT_SUCCESS);
            message.obj = textModels;
            handler.sendMessage(message);
        } catch (UnsupportedEncodingException e) {
            handler.sendEmptyMessage(Constants.MESSAGE_WHAT_EXCEPTION);
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            handler.sendEmptyMessage(Constants.MESSAGE_WHAT_EXCEPTION);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            handler.sendEmptyMessage(Constants.MESSAGE_WHAT_EXCEPTION);
        }
        // return null;
    }

    public static void getMessage(Handler handler, String tag) {
        String info = null;
        try {
            info = URLEncoder.encode(tag, CHARSET_NAME);
            String requestUrl = URL + info;
//            String result = queryStringForGet(requestUrl);
//            String result = TuLingUtils.httpPost(requestUrl);
//            String result = TencentUtil.httpGet(tag);
//            TencentModel textModel = gson.fromJson(result, TencentModel.class);
            TencentModel textModel = TencentUtil.httpGet(tag);
            Message message = handler
                    .obtainMessage(Constants.MESSAGE_WHAT_SUCCESS);
            message.obj = textModel;
            handler.sendMessage(message);
        } catch (Exception e) {
            handler.sendEmptyMessage(Constants.MESSAGE_WHAT_EXCEPTION);
            e.printStackTrace();
        }
    }

    public static void executeMessageTask(final Handler handler,
                                          final String tag) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getMessage(handler, tag);
            }
        };
        sExecutor.execute(runnable);
    }

    private void parseModel(int code, String result) {
        switch (code) {
            case 100000: // 文本类

                break;
            case 200000: // 链接类

                break;
            case 302000: // 新闻类
                break;
            case 304000: // 软件下载
                break;
            case 305000:

                break;
        }
    }
}
