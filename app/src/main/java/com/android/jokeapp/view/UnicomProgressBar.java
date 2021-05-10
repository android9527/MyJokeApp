//package com.android.jokeapp.view;
//
//import com.android.jokeapp.R;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.drawable.BitmapDrawable;
//import android.util.AttributeSet;
//import android.view.View;
//
//public class UnicomProgressBar extends View
//{
//
//    private final int pictures[] = { R.drawable.p_1, R.drawable.p_2,
//            R.drawable.p_3, R.drawable.p_4, R.drawable.p_5, R.drawable.p_6,
//            R.drawable.p_7, R.drawable.p_8, R.drawable.p_9, R.drawable.p_10,
//            R.drawable.p_11, R.drawable.p_12, R.drawable.p_13, R.drawable.p_14,
//            R.drawable.p_15, R.drawable.p_16, R.drawable.p_17, R.drawable.p_18,
//            R.drawable.p_19, R.drawable.p_20, R.drawable.p_21, R.drawable.p_22,
//            R.drawable.p_23, R.drawable.p_24, R.drawable.p_25, R.drawable.p_26,
//            R.drawable.p_27, R.drawable.p_28, R.drawable.p_29, R.drawable.p_30,
//            R.drawable.p_31, R.drawable.p_32, R.drawable.p_33, R.drawable.p_34,
//            R.drawable.p_35, R.drawable.p_36, R.drawable.p_37, R.drawable.p_38,
//            R.drawable.p_39, R.drawable.p_40, R.drawable.p_41, R.drawable.p_42,
//            R.drawable.p_43, R.drawable.p_44, R.drawable.p_45, R.drawable.p_46,
//            R.drawable.p_47, R.drawable.p_48, R.drawable.p_49, R.drawable.p_50,
//            R.drawable.p_51, R.drawable.p_52,
//            R.drawable.p_53, R.drawable.p_54, R.drawable.p_55, R.drawable.p_56,
//            R.drawable.p_57, R.drawable.p_58, R.drawable.p_59, R.drawable.p_60,};
//
//    private int progress = 0;
//
//    private int tempProgress = 0;
//
//    private long duration = 0;
//
//    public UnicomProgressBar(Context context)
//    {
//        super(context);
//        // TODO Auto-generated constructor stub
//    }
//
//    public UnicomProgressBar(Context context, AttributeSet attrs,
//            int defStyleAttr)
//    {
//        super(context, attrs, defStyleAttr);
//        // TODO Auto-generated constructor stub
//    }
//
//    public UnicomProgressBar(Context context, AttributeSet attrs)
//    {
//        super(context, attrs);
//        // TODO Auto-generated constructor stub
//    }
//    Bitmap bitmap = null;
//    @Override
//    protected void onDraw(Canvas canvas)
//    {
//        // TODO Auto-generated method stub
//        super.onDraw(canvas);
//
//        //        canvas.save();
////        bitmap.recycle();
//        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(pictures[tempProgress]);
//        bitmap = bitmapDrawable.getBitmap();
//        canvas.drawBitmap(bitmap, 0, 0, null);
////        bitmapDrawable.draw(canvas);
//        canvas.restore();
////        bitmap.recycle();
//        //        bitmapDrawable.
//    }
//
//    public int getProgress()
//    {
//        return progress;
//    }
//
//    public void setProgress(int progress)
//    {
//        this.progress = progress;
//        if (progress > 100)
//        {
//            this.progress = 0;
//        }
//        tempProgress = progress / 2;
//        invalidate();
//    }
//
//    public long getDuration()
//    {
//        return duration;
//    }
//
//    public void setDuration(long duration)
//    {
//        this.duration = duration;
//        startAnim();
//    }
//
//    private void startAnim()
//    {
//        isRun = false;
//        start();
//    }
//
//    private boolean isRun = false;
//
//    private void start()
//    {
//        if (isRun)
//            return;
//        new Thread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                isRun = true;
//
//                while (isRun)
//                {
////                    setProgress(progress++);
//                    progress += 1;
//                    if (progress > 100)
//                    {
//                        progress = 0;
//                    }
//                    tempProgress = progress / 2;
//                    postInvalidate();
//
//                    try
//                    {
//                        Thread.sleep(duration);
//                    }
//                    catch (InterruptedException e)
//                    {
//                        e.printStackTrace();
//                    }
//                }
//
//                isRun = false;
//            }
//        }).start();
//    }
//
//}
