/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.jokeapp.activity;

import static com.android.jokeapp.common.Constants.LEFT_MENU;
import net.youmi.android.AdManager;
import net.youmi.android.listener.Interface_ActivityListener;
import net.youmi.android.offers.OffersManager;
import net.youmi.android.offers.PointsManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.android.jokeapp.R;
import com.android.jokeapp.fragment.FragmentBase;
import com.android.jokeapp.fragment.FragmentCharAt;
import com.android.jokeapp.fragment.FragmentJokRecommend;
import com.android.jokeapp.fragment.FragmentMenuLeft;
import com.android.jokeapp.fragment.YoumiOffersAdsDemo;
import com.android.jokeapp.util.DeviceUtils;
import com.android.jokeapp.util.SystemBarTintUtil;
import com.android.jokeapp.util.SystemBarTintManager;
import com.nineoldandroids.view.ViewHelper;

/**
 * This example illustrates a common usage of the DrawerLayout widget in the
 * Android support library.
 * 
 * <p>
 * A DrawerLayout should be positioned at the top of your view hierarchy,
 * placing it below the action bar but above your content views. The primary
 * content should match_parent in both dimensions. Each drawer should define a
 * reasonable width and match_parent for height. Drawer views should be
 * positioned after the content view in your layout to preserve proper ordering.
 * </p>
 * 
 * <p>
 * When a navigation (left) drawer is present, the host activity should detect
 * presses of the action bar's Up affordance as a signal to open and close the
 * navigation drawer. Items within the drawer should fall into one of two
 * categories.
 * </p>
 * 
 * <ul>
 * <li><strong>View switches</strong>. A view switch follows the same basic
 * policies as list or tab navigation in that a view switch does not create
 * navigation history. This pattern should only be used at the root activity of
 * a task, leaving some form of Up navigation active for activities further down
 * the navigation hierarchy.</li>
 * <li><strong>Selective Up</strong>. The drawer allows the user to choose an
 * alternate parent for Up navigation. This allows a user to jump across an
 * app's navigation hierarchy at will. The application should treat this as it
 * treats Up navigation from a different task, replacing the current task stack
 * using TaskStackBuilder or similar. This is the only form of navigation drawer
 * that should be used outside of the root activity of a task.</li>
 * </ul>
 * 
 * <p>
 * Right side drawers should be used for actions, not navigation. This follows
 * the pattern established by the Action Bar that navigation should be to the
 * left and actions to the right. An action should be an operation performed on
 * the current contents of the window, for example enabling or disabling a data
 * overlay on top of the current content.
 * </p>
 */
public class DrawerLayoutActivity extends BaseFragmentActivity
{
    private DrawerLayout mDrawerLayout;
    
    private FrameLayout frameContent;
    
    private ActionBarHelper mActionBarHelper;
    
    private ActionBarDrawerToggle mDrawerToggle;
    
    private ActionBar mActionBar;
    
    private FragmentMenuLeft fragmentMenuLeft;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initAdManager();
        SystemBarTintUtil.setSystemBarTintColor(this);
        setContentView(R.layout.drawer_layout);
        fragmentManager = getSupportFragmentManager();
        mActionBar = getSupportActionBar();
        int width = DeviceUtils.getScreenWidth(getApplicationContext());
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.left_drawer);
        LayoutParams params = (LayoutParams) frameLayout.getLayoutParams();
        params.width = (width / 4) * 3;
        
        fragmentMenuLeft = (FragmentMenuLeft)fragmentManager
                .findFragmentById(R.id.fragment_menu_left);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        frameContent = (FrameLayout) findViewById(R.id.frame_content);
        
        mDrawerLayout.setDrawerListener(new DemoDrawerListener());
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.END);
        
        // The drawer title must be set in order to announce state changes when
        // accessibility is turned on. This is typically a simple description,
        // e.g. "Navigation".
        //        mDrawerLayout.setDrawerTitle(GravityCompat.START, getString(R.string.drawer_title));
        
        mActionBarHelper = createActionBarHelper();
        mActionBarHelper.init();
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
                Gravity.LEFT);
        // ActionBarDrawerToggle provides convenient helpers for tying together the
        // prescribed interactions between a top-level sliding drawer and the action bar.
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close);
        initEvents();
        
        
        initInsetTop(frameContent);
        //        initTint();
        switchContent(0);
        
//        showBanner();
    }
    
    public void initInsetTop(View rootView)
    {
        SystemBarTintManager tintManager = new SystemBarTintManager(
                this);
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        rootView.setPadding(0,
                config.getPixelInsetTop(true),
                config.getPixelInsetRight(),
                config.getPixelInsetBottom());
        rootView.requestLayout();
    }
    
    private void initTint()
    {
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        //        tintManager.setStatusBarTintEnabled(true);
        //        tintManager.setNavigationBarTintEnabled(true);
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        //      tintManager.setStatusBarTintEnabled(true);
        //      tintManager.setStatusBarTintDrawable(new ColorDrawable(getResources().getColor(R.color.mxx_item_theme_color_alpha)));
        //        mPagerSlidingTabStrip.setPadding(0, config.getPixelInsetTop(true), config.getPixelInsetRight(), 0);
        //      View rootView = findViewById(R.id.main_layout_root);
        //        DrawerLayout.LayoutParams layoutParams = (DrawerLayout.LayoutParams) frameContent.getLayoutParams();
        //        layoutParams.topMargin = config.getPixelInsetTop(true);
        ////      layoutParams.height = MxxUiUtil.dip2px(this, 48) + config.getPixelInsetTop(true);
        //        frameContent.requestLayout();
        
        //        frameContent.setPadding(0, config.getPixelInsetTop(true), 0, config.getPixelInsetBottom());
        
    }
    
//    private Fragment contentJokeFragment = new FragmentJokeContent();
    private FragmentBase contentJokeFragment = new FragmentJokRecommend(0);
    
    private FragmentBase contentColdJokeFragment = null;
    private FragmentBase contentChatFragment = null;
    
    private FragmentBase mCurrentContent, tempFragment;
    
    private FragmentManager fragmentManager;
    
    private int currPosition = 0;
    
	public void switchContent(int position) {
		getmActionBar().setTitle(LEFT_MENU[position]);
		getmDrawerLayout().closeDrawer(Gravity.LEFT);
		switch (position) {
		case 0: // ContentJokeFragment
			currPosition = 0;
			if (contentJokeFragment == null) {
				// contentJokeFragment = new FragmentJokeContent();
				contentJokeFragment = new FragmentJokRecommend(position);
			}
			contentJokeFragment.stopSpeaking();
			tempFragment = contentJokeFragment;
			if (mCurrentContent == contentJokeFragment) {
				return;
			}
			break;
		case 1:
			currPosition = 1;
			if (contentColdJokeFragment == null) {
				contentColdJokeFragment = new FragmentJokRecommend(position);
			}
			contentColdJokeFragment.stopSpeaking();
			tempFragment = contentColdJokeFragment;
			if (mCurrentContent == contentColdJokeFragment) {
				return;
			}
			break;
		case 2:
			currPosition = 2;
			if (contentChatFragment == null) {
				contentChatFragment = new FragmentCharAt();
			}
			tempFragment = contentChatFragment;
			if (mCurrentContent == contentChatFragment) {
				return;
			}
			break;
		case 3:
			if (currPosition == 3)
				return;
//			currPosition = 3;
//			startActivity(new Intent(DrawerLayoutActivity.this,
//					YoumiOffersAdsDemo.class));
			OffersManager.getInstance(this).showOffersWall(
					new Interface_ActivityListener() {

						/**
						 * 但积分墙销毁的时候，即积分墙的Activity调用了onDestory的时候回调
						 */
						@Override
						public void onActivityDestroy(Context context) {
							fragmentMenuLeft.switchContent(currPosition);
						}
					});
			return;

		default:
			if (contentJokeFragment == null) {
				// contentJokeFragment = new FragmentJokeContent();
				contentJokeFragment = new FragmentJokRecommend(position);
			}
			contentJokeFragment.stopSpeaking();
			tempFragment = contentJokeFragment;
			if (mCurrentContent == contentJokeFragment) {
				return;
			}
			break;
		}

		if (mCurrentContent != tempFragment) {
			FragmentTransaction transaction = fragmentManager
					.beginTransaction().setCustomAnimations(
							android.R.anim.fade_in, android.R.anim.fade_out);
			if (!tempFragment.isAdded()) { // 先判断是否被add过
				transaction.add(R.id.frame_content, tempFragment)
						.show(tempFragment).commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else {
				transaction.hide(mCurrentContent).show(tempFragment).commit(); // 隐藏当前的fragment，显示下一个
			}
			mCurrentContent = tempFragment;
		}
		tempFragment = null;
	}
	
	private void initAdManager(){
	    AdManager.getInstance(this).init("93348c2ae3be5483", "a0481c3c65f79729", false);
		// (可选)开启用户数据统计服务,(sdk v4.08之后新增功能)默认不开启，传入false值也不开启，只有传入true才会调用
//				AdManager.getInstance(this).setUserDataCollect(true);
		// 设置是否开启有米广告SDK Log。默认值为true，标识开启， false为关闭log
		AdManager.getInstance(this).setEnableDebugLog(false);
				// 如果使用积分广告，请务必调用积分广告的初始化接口:
				OffersManager.getInstance(this).onAppLaunch();

				// （可选）注册积分监听-随时随地获得积分的变动情况
//				PointsManager.getInstance(this).registerNotify(this);

				// （可选）注册积分订单赚取监听（sdk v4.10版本新增功能）
//				PointsManager.getInstance(this).registerPointsEarnNotify(this);
	}
    
    private void initEvents()
    {
        mDrawerLayout.setDrawerListener(new DrawerListener()
        {
            @Override
            public void onDrawerStateChanged(int newState)
            {
                mDrawerToggle.onDrawerStateChanged(newState);
            }
            
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset)
            {
                mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
                //  				View frameContent = mDrawerLayout.getChildAt(0);
                View mMenu = drawerView;
                float scale = 1 - slideOffset;
                float rightScale = 0.8f + scale * 0.2f;
                
                if (drawerView.getTag().equals("LEFT"))
                {
                    float leftScale = 1 - 0.3f * scale;
                    ViewHelper.setScaleX(mMenu, leftScale);
                    ViewHelper.setScaleY(mMenu, leftScale);
                    ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
                    ViewHelper.setTranslationX(frameContent,
                            mMenu.getMeasuredWidth() * (1 - scale));
                    ViewHelper.setPivotX(frameContent, 0);
                    ViewHelper.setPivotY(frameContent,
                            frameContent.getMeasuredHeight() / 2);
                    frameContent.invalidate();
                    ViewHelper.setScaleX(frameContent, rightScale);
                    ViewHelper.setScaleY(frameContent, rightScale);
                }
                else
                {
                    ViewHelper.setTranslationX(frameContent,
                            -mMenu.getMeasuredWidth() * slideOffset);
                    ViewHelper.setPivotX(frameContent,
                            frameContent.getMeasuredWidth());
                    ViewHelper.setPivotY(frameContent,
                            frameContent.getMeasuredHeight() / 2);
                    frameContent.invalidate();
                    ViewHelper.setScaleX(frameContent, rightScale);
                    ViewHelper.setScaleY(frameContent, rightScale);
                }
                
            }
            
            @Override
            public void onDrawerOpened(View drawerView)
            {
                mDrawerToggle.onDrawerOpened(drawerView);
                mActionBarHelper.onDrawerOpened();
            }
            
            @Override
            public void onDrawerClosed(View drawerView)
            {
                //  				mDrawerLayout.setDrawerLockMode(
                //  						DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
                mDrawerToggle.onDrawerClosed(drawerView);
                mActionBarHelper.onDrawerClosed();
            }
        });
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        /*
         * The action bar home/up action should open or close the drawer.
         * mDrawerToggle will take care of this.
         */
        //        if (mDrawerToggle.onOptionsItemSelected(item) {
        //            return true;
        //        }
        
        switch (item.getItemId())
        {
            case android.R.id.home:
                
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT))
                    mDrawerLayout.closeDrawers();
                else
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                break;
            
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
    /**
     * A drawer listener can be used to respond to drawer events such as
     * becoming fully opened or closed. You should always prefer to perform
     * expensive operations such as drastic relayout when no animation is
     * currently in progress, either before or after the drawer animates.
     * 
     * When using ActionBarDrawerToggle, all DrawerLayout listener methods
     * should be forwarded if the ActionBarDrawerToggle is not used as the
     * DrawerLayout listener directly.
     */
    private class DemoDrawerListener implements DrawerLayout.DrawerListener
    {
        @Override
        public void onDrawerOpened(View drawerView)
        {
            mDrawerToggle.onDrawerOpened(drawerView);
            mActionBarHelper.onDrawerOpened();
        }
        
        @Override
        public void onDrawerClosed(View drawerView)
        {
            mDrawerToggle.onDrawerClosed(drawerView);
            mActionBarHelper.onDrawerClosed();
        }
        
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset)
        {
            mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
        }
        
        @Override
        public void onDrawerStateChanged(int newState)
        {
            mDrawerToggle.onDrawerStateChanged(newState);
        }
    }
    
    /**
     * Create a compatible helper that will manipulate the action bar if
     * available.
     */
    private ActionBarHelper createActionBarHelper()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        {
            return new ActionBarHelperICS();
        }
        else
        {
            return new ActionBarHelper();
        }
    }
    
    /**
     * Stub action bar helper; this does nothing.
     */
    private class ActionBarHelper
    {
        public void init()
        {
        }
        
        public void onDrawerClosed()
        {
        }
        
        public void onDrawerOpened()
        {
        }
        
        public void setTitle(CharSequence title)
        {
        }
    }
    
    /**
     * Action bar helper for use on ICS and newer devices.
     */
    private class ActionBarHelperICS extends ActionBarHelper
    {
        private CharSequence mDrawerTitle;
        
        private CharSequence mTitle;
        
        ActionBarHelperICS()
        {
        }
        
        @Override
        public void init()
        {
            Drawable myDrawable = getResources().getDrawable(R.color.mxx_item_theme_color_alpha);
            mActionBar.setBackgroundDrawable(myDrawable);
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
            mTitle = mDrawerTitle = getTitle();
        }
        
        /**
         * When the drawer is closed we restore the action bar state reflecting
         * the specific contents in view.
         */
        @Override
        public void onDrawerClosed()
        {
            super.onDrawerClosed();
            //            mActionBar.setTitle(mTitle);
        }
        
        /**
         * When the drawer is open we set the action bar to a generic title. The
         * action bar should only contain data relevant at the top level of the
         * nav hierarchy represented by the drawer, as the rest of your content
         * will be dimmed down and non-interactive.
         */
        @Override
        public void onDrawerOpened()
        {
            super.onDrawerOpened();
            //            mActionBar.setTitle(mDrawerTitle);
        }
        
        @Override
        public void setTitle(CharSequence title)
        {
            mTitle = title;
        }
    }
    
    public DrawerLayout getmDrawerLayout()
    {
        return mDrawerLayout;
    }
    
    public ActionBarHelper getmActionBarHelper()
    {
        return mActionBarHelper;
    }
    
    public ActionBar getmActionBar()
    {
        return mActionBar;
    }

    long mExitTime = 0;
    @Override
    public void onBackPressed()
    {
        if (mCurrentContent != contentJokeFragment)
        {
            fragmentMenuLeft.switchContent(0);
            return;
        }
     // 退出
        if ((System.currentTimeMillis() - mExitTime) > 3000)
        {// 如果两次按键时间间隔大于3000毫秒，则不退出
            Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
            return;
        }
        else{
            android.os.Process.killProcess(android.os.Process.myPid());// 杀死进程
            android.os.Process.killProcess(android.os.Process.myTid());// 杀死当前UI线程
            android.os.Process.killProcess(android.os.Process.myUid());// 杀死当前用户
            System.exit(0);
        }
        super.onBackPressed();
    }
}
