package com.android.jokeapp.model;

import java.util.Random;

public class TextModel extends BaseModel {

    @Override
    public String toString()
    {
        return "TextModel [dingNumber=" + dingNumber + ", caiNumber="
                + caiNumber + ", hasDing=" + hasDing + ", hasCai=" + hasCai
                + ", code=" + getCode() + ", text=" + getText() + "]";
    }

    private int dingNumber, caiNumber;

    public int getDingNumber()
    {
        return dingNumber;
    }

    public void setDingNumber(int dingNumber)
    {
        this.dingNumber = dingNumber;
    }
    
    private boolean hasDing, hasCai;

    public int getCaiNumber()
    {
        return caiNumber;
    }

    public void setCaiNumber(int caiNumber)
    {
        this.caiNumber = caiNumber;
    }

    public boolean isHasDing()
    {
        return hasDing;
    }

    public void setHasDing(boolean hasDing)
    {
        this.hasDing = hasDing;
    }

    public boolean isHasCai()
    {
        return hasCai;
    }

    public void setHasCai(boolean hasCai)
    {
        this.hasCai = hasCai;
    }

    public void initDingAndCaiNumber()
    {
    	Random random = new Random();
        dingNumber = random.nextInt(1000);
        caiNumber = random.nextInt(200);
    }
    
}
