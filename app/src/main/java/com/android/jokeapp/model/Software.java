package com.android.jokeapp.model;

public class Software {
	
	/**
	 * 参数	 说明
code	 状态码
text	 文字内容
name	 软件名称
count	 下载量
detailurl	 详情
icon	 图标地址

	 */
	private String name;
	private String count;
	private String detailurl;
	private String icon;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getDetailurl() {
		return detailurl;
	}
	public void setDetailurl(String detailurl) {
		this.detailurl = detailurl;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
}
