package com.nhaarman.listviewanimations.itemmanipulation;

import android.view.MotionEvent;

import androidx.annotation.NonNull;

public interface TouchEventHandler {

    boolean onTouchEvent(@NonNull MotionEvent event);

    boolean isInteracting();

}
