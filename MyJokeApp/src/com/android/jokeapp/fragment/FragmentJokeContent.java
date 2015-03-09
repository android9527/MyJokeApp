package com.android.jokeapp.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.android.jokeapp.R;
import com.android.jokeapp.adapter.DepthPageTransformer;
import com.android.jokeapp.common.Constants;
import com.android.jokeapp.util.SystemBarTintManager;
import com.android.jokeapp.view.PagerSlidingTabStrip;


public class FragmentJokeContent extends FragmentBase
{
    
    private ViewPager pager;
    
    private com.android.jokeapp.view.PagerSlidingTabStrip mTabStrip;
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_joke_layout, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        initViewPager(view);
    }
    
    private void initViewPager(View rootView)
    {
        // pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.lightskyblue));
        // // pagerTabStrip.setTextSpacing(50);
        pager = (ViewPager) rootView.findViewById(R.id.pager);
//        pager.setOffscreenPageLimit(3);
        // java.lang.IllegalStateException: Recursive entry to executePendingTransactions
        final ArrayList<Fragment> fragments = new ArrayList<Fragment>();
//        for (int i = 0; i < Constants.TITLE_JOKE.length; i++)
//        {
//            fragments.add(new FragmentJokRecommend(this,
//                    Constants.TITLE_JOKE[i]));
//        }
        fragments.add(new FragmentJokRecommend(0));
        fragments.add(new FragmentJokRecommend(1));
        final MyPagerAdapter adapter = new MyPagerAdapter(this,
                getChildFragmentManager(), fragments);
        pager.setAdapter(adapter);
        
        pager.setPageTransformer(true, new DepthPageTransformer());
        // initInsetTop(pagerTabStrip);
        // SystemBarTintManager tintManager = SystemBarTintManager.newInstance(getActivity());
        // SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        // ViewPager.LayoutParams layoutParams = (ViewPager.LayoutParams) pagerTabStrip.getLayoutParams();
        // layoutParams.topMargin = config.getPixelInsetTop(true);
        
        // TODO ActionBar �߶�Ҫ����
        // layoutParams.height = /*MxxUiUtil.dip2px(this, 48) + */config.getPixelInsetTop(true) + 96;
        // pagerTabStrip.requestLayout();
        
        mTabStrip = (PagerSlidingTabStrip) rootView.findViewById(R.id.main_tab);
        
        mTabStrip.setViewPager(pager);
        
        mTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            
            @Override
            public void onPageSelected(int arg0)
            {
                // TODO Auto-generated method stub
            }
            
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {
                // TODO Auto-generated method stub
                if (mTabStrip.getTranslationY() != 0)
                {
                    mTabStrip.setTranslationY(0);
                }
            }
            
            @Override
            public void onPageScrollStateChanged(int arg0)
            {
                // TODO Auto-generated method stub
                
            }
        });
                initTint();
    }
    
    private void initTint()
    {
        SystemBarTintManager tintManager = new SystemBarTintManager(
                getActivity());
        ;
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        // tintManager.setStatusBarTintEnabled(true);
        // tintManager.setStatusBarTintDrawable(new
        // ColorDrawable(getResources().getColor(R.color.mxx_item_theme_color_alpha)));
        // mPagerSlidingTabStrip.setPadding(0, config.getPixelInsetTop(true), config.getPixelInsetRight(), 0);
        // View rootView = findViewById(R.id.main_layout_root);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mTabStrip.getLayoutParams();
        layoutParams.topMargin = config.getPixelInsetTop(true);
        // layoutParams.height = MxxUiUtil.dip2px(this, 48) + config.getPixelInsetTop(true);
        mTabStrip.requestLayout();
    }
    
    private class MyPagerAdapter extends FragmentPagerAdapter
    {
        
        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        
        FragmentManager fm;
        
        public MyPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }
        
        public MyPagerAdapter(FragmentBase fragment, FragmentManager fm,
                ArrayList<Fragment> fragments)
        {
            this(fm);
            this.fragments = fragments;
            this.fm = fm;
        }
        
        @Override
        public Fragment getItem(int arg0)
        {
            return fragments.get(arg0);
        }
        
        @Override
        public int getCount()
        {
            return Constants.TITLE_JOKE.length;
        }
        
        @Override
        public CharSequence getPageTitle(int position)
        {
            return Constants.TITLE_JOKE[position];
        }
         
    }
    
    public ViewPager getPager()
    {
        return pager;
    }
    
    public View getPagerTabStrip()
    {
        return mTabStrip;
    }
    
}
