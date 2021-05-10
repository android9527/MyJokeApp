package com.android.jokeapp.model;

import java.io.Serializable;
import java.util.List;

public class BaseModel implements Serializable {

    String globalId;
    IntentModel intent;
    List<ResultModel> results;

    public double getCode() {
        if (intent != null) {
            return intent.code;
        }
        return -1;
    }

    public String getText() {
        if (results != null && results.size() > 0 && results.get(0).values != null) {
            ValueModel valueModel = results.get(0).values;
            return valueModel.text;
        }
        return "";
    }

    @Override
    public String toString() {
        return "BaseModel [code=" + getCode() + ", text=" + getText() + "]";
    }

    private static class IntentModel {
        double code;
    }

    private static class ResultModel {
        int groupType;
        String resultType;
        ValueModel values;
    }

    private static class ValueModel {
        String text;
    }

}
