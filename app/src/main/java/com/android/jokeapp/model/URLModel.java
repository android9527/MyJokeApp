package com.android.jokeapp.model;

public class URLModel extends BaseModel {
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "URLModel [url=" + url + ", code=" + getCode() + ", text=" + getText()
				+ "]";
	}

}
