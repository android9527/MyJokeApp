package com.android.jokeapp.view;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

import androidx.collection.LruCache;

public class TypefaceSpan extends MetricAffectingSpan{
	
	private static LruCache<String, Typeface> sTypefaceCache = new LruCache<String, Typeface>(12);
	private Typeface mTypeface;
	private boolean isBold = false;
	
	public TypefaceSpan(String typefaceName, Typeface typeface){
		mTypeface = sTypefaceCache.get(typefaceName);
		if(mTypeface==null){
			mTypeface = typeface;
			sTypefaceCache.put(typefaceName, typeface);
		}
	}
	public TypefaceSpan(String typefaceName, Typeface typeface, boolean bold){
		mTypeface = sTypefaceCache.get(typefaceName);
		if(mTypeface==null){
			mTypeface = typeface;
			sTypefaceCache.put(typefaceName, typeface);
		}
		this.isBold = bold;
	}

	@Override
	public void updateMeasureState(TextPaint p) {
		// TODO Auto-generated method stub
		p.setTypeface(mTypeface);
		p.setFakeBoldText(isBold);
		p.setFlags(p.getFlags()| Paint.SUBPIXEL_TEXT_FLAG);
	}

	@Override
	public void updateDrawState(TextPaint tp) {
		// TODO Auto-generated method stub
		tp.setTypeface(mTypeface);
		tp.setFakeBoldText(isBold);
		tp.setFlags(tp.getFlags()| Paint.SUBPIXEL_TEXT_FLAG);
	}

}
