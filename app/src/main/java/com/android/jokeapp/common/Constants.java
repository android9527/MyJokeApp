package com.android.jokeapp.common;

public class Constants {
	public static final int LOADING_NUMBER = 10;

	/**
	 * 左侧菜单
	 */
	public static final String[] LEFT_MENU = new String[] { "笑话", "冷笑话", "聊天" };

	public static final String[] TITLE_JOKE = new String[] { "推荐", "排行" };

	public enum JOKETYPE {
		RECOMMEND, // 推荐
		RANKING; // 排行
	}

	public static final int MESSAGE_WHAT_SUCCESS = 0x01;

	public static final int MESSAGE_WHAT_EXCEPTION = 0x02;

	public static final String APP_KEY = "ULJ9akd1hECZqbJjtt04c1OcDX1EdHUY";
	public static final String BANNER_LEFT = "9489";
	public static final String BANNER_CONTENT_1 = "9490";
	public static final String BANNER_CONTENT_2 = "9491";
	public static final String BANNER_CONTENT_3 = "9492";
	public static final String IS_PID = "9494";
	public static final String SPLASH_PID = "9495";

}
