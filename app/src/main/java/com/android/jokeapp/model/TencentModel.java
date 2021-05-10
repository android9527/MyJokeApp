package com.android.jokeapp.model;

public class TencentModel extends TextModel {

//    {"Confidence":1.0,"Reply":"“你是怎么考上省重点中学的？”   “我考的是市重点，学校自己努力升了省重点！”","RequestId":"9ea04aa1-90ab-4943-abc5-5339138f2a45"}

    private double Confidence;
    private String Reply;
    private String RequestId;

    public TencentModel (String reply) {
        this.Reply = reply;
        this.Confidence = -1;
    }

    @Override
    public String getText() {
        return Reply;
    }

    @Override
    public double getCode() {
        return Confidence;
    }
}
