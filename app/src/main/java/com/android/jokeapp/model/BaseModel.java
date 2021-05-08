package com.android.jokeapp.model;

import java.io.Serializable;

public class BaseModel implements Serializable{

	int code;
	String text;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "BaseModel [code=" + code + ", text=" + text + "]";
	}

}
