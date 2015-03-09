package com.android.jokeapp.util;


import com.android.jokeapp.R;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

public class SystemBarTintUtil {
	public static void setSystemBarTintColor(Activity activity){
		if(SystemBarTintManager.isKitKat()){
			SystemBarTintManager tintManager = new SystemBarTintManager(activity);  
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setNavigationBarTintEnabled(true);
			Drawable myDrawable = activity.getResources().getDrawable(R.color.mxx_item_theme_color_alpha);
			tintManager.setNavigationBarTintDrawable(myDrawable);
//			tintManager.setStatusBarTintResource(R.color.mxx_item_theme_color_alpha);
			tintManager.setStatusBarTintDrawable(myDrawable);
		}
	}
}
