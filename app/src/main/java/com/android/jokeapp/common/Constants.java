package com.android.jokeapp.common;

public class Constants {
	public static final int LOADING_NUMBER = 10;

	/**
	 * 左侧菜单
	 */
	public static final String[] LEFT_MENU = new String[] { "笑话", "冷笑话", "聊天",
			"应用推荐" };

	public static final String[] TITLE_JOKE = new String[] { "推荐", "排行" };

	public enum JOKETYPE {
		RECOMMEND, // 推荐
		RANKING; // 排行
	}

	public static final int MESSAGE_WHAT_SUCCESS = 0x01;

	public static final int MESSAGE_WHAT_EXCEPTION = 0x02;

}
