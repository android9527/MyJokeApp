package com.android.jokeapp.util;

import com.android.jokeapp.model.TencentModel;
import com.tencentcloudapi.common.Credential;

import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

import com.tencentcloudapi.nlp.v20190408.NlpClient;
import com.tencentcloudapi.nlp.v20190408.models.*;;

public class TencentUtil {
    public static TencentModel httpGet(String args) {
        try {
            Credential cred = new Credential("AKIDc1AnBK2zpmSB656CAHUS8lAST8cLb07x", "tajSCgt01wbE84k9xhMov6NO8IML9OpJ");
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("nlp.tencentcloudapi.com");
            httpProfile.setReqMethod("GET"); // get请求(默认为post请求)
//            httpProfile.setProtocol("http://");  // 在外网互通的网络环境下支持http协议(默认是https协议),请选择(https:// or http://)
//            httpProfile.setConnTimeout(30); // 请求连接超时时间，单位为秒(默认60秒)
            httpProfile.setWriteTimeout(30);  // 设置写入超时时间，单位为秒(默认0秒)
            httpProfile.setReadTimeout(30);  // 设置读取超时时间，单位为秒(默认0秒)

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            NlpClient client = new NlpClient(cred, "ap-guangzhou", clientProfile);

            ChatBotRequest req = new ChatBotRequest();
            req.setQuery(args);

            ChatBotResponse resp = client.ChatBot(req);
            String result = ChatBotResponse.toJsonString(resp);
            return ApiUtil.gson.fromJson(result, TencentModel.class);

        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
        return new TencentModel("我不知道你在说什么");
    }

}
