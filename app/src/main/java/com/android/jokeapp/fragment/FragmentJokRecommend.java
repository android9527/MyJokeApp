package com.android.jokeapp.fragment;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.android.jokeapp.R;
import com.android.jokeapp.adapter.MyJokeListAdapter;
import com.android.jokeapp.common.Constants;
import com.android.jokeapp.model.TextModel;
import com.android.jokeapp.util.ApiUtil;
import com.android.jokeapp.util.UiUtil;
import com.android.jokeapp.view.ListViewScrollObserver;
import com.android.jokeapp.view.ListViewScrollObserver.OnListViewScrollListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.SwipeDismissAdapter;

public class FragmentJokRecommend extends FragmentBase implements MyJokeListAdapter.IOnClickCallback, OnDismissCallback {
    @ViewInject(id = R.id.listview)
    private PullToRefreshListView mRefreshListView;

    @ViewInject(id = R.id.loading)
    private ImageView loading;

    private MyJokeListAdapter mJokeListAdapter;

    private List<TextModel> list;

    private int tag;

    private Toolbar mQuickReturnView;

    AnimationDrawable anim;// 

    private ListView mListView;

    private boolean isonPullDownToRefresh = false;

    private static final int INITIAL_DELAY_MILLIS = 300;

    public FragmentJokRecommend(int tag) {
        this.tag = tag;
    }

    public FragmentJokRecommend() {
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            mRefreshListView.onRefreshComplete();
            switch (msg.what) {
                case Constants.MESSAGE_WHAT_SUCCESS:
                    // TODO
                    List<TextModel> list = (List<TextModel>) msg.obj;
                    if (isonPullDownToRefresh) {
                        mJokeListAdapter.addToTop(list);
                        mListView.setSelection(Constants.LOADING_NUMBER);
                    } else
                        mJokeListAdapter.addToBottom(list);
                    stopAnim();
                    break;
                case Constants.MESSAGE_WHAT_EXCEPTION:
                    // TODO
                    showTip(getString(R.string.loading_failed));
                    stopAnim();
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };

    private void stopAnim() {
        if (!mListView.isShown()) {
            anim.stop();
            loading.setVisibility(View.GONE);
            mRefreshListView.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * The Fragment's UI is just a simple text view showing its instance number.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_joke_layout, container, false);
        FinalActivity.initInjectedView(this, v);

        // mListView = (ListView) v.findViewById(R.id.listview);
        // mListView.setItemsCanFocus(true);

        // mQuickReturnTextView = (TextView)
        // v.findViewById(R.id.quick_return_tv);

        // if (whichFragment instanceof FragmentJokeContent)
        // {
        // mQuickReturnView = (View) ((FragmentJokeContent)
        // whichFragment).getPagerTabStrip();
        // }

        mAdView = v.findViewById(R.id.adView);
        mQuickReturnView = drawerLayoutActivity.getmActionBar();
        initInsetTop(v);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSpeechSynthesis();
        showBanner(Constants.BANNER_CONTENT_1);

        mListView = mRefreshListView.getRefreshableView();
        mListView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        stopSpeaking();
                        break;

                    default:
                        break;
                }
                return false;
            }

        });

        anim = (AnimationDrawable) loading.getBackground();
        anim.start();
        list = new ArrayList<TextModel>();
        mJokeListAdapter = new MyJokeListAdapter(getActivity(), list);
        mJokeListAdapter.registeIOnClickCallback(this);

        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(/*new SwipeDismissAdapter(*/mJokeListAdapter/*, this)*/);
        swingBottomInAnimationAdapter.setAbsListView(mRefreshListView.getRefreshableView());

        assert swingBottomInAnimationAdapter.getViewAnimator() != null;
        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);

        mRefreshListView.setAdapter(swingBottomInAnimationAdapter);


//        mRefreshListView.setAdapter(mJokeListAdapter);
        ApiUtil.executeJokeMessageTask(Constants.LOADING_NUMBER, mHandler, tag);

        mRefreshListView.setMode(Mode.BOTH);
        // Set a listener to be invoked when the list should be refreshed.
        mRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                isonPullDownToRefresh = true;
                String label =
                        DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                                | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                // Do work to refresh the list here.
                ApiUtil.executeJokeMessageTask(Constants.LOADING_NUMBER, mHandler, tag);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                isonPullDownToRefresh = false;
                ApiUtil.executeJokeMessageTask(Constants.LOADING_NUMBER, mHandler, tag);
            }
        });
        initScrollListener();
    }

    private void initScrollListener() {
        final int tranY = UiUtil.dip2px(getActivity(), 48);

        int statusBarHeight = UiUtil.getStatusBarHeight(getActivity());

        final int max_tranY = tranY + statusBarHeight;

        ListViewScrollObserver observer = new ListViewScrollObserver(mListView);
        observer.setOnScrollUpAndDownListener(new OnListViewScrollListener() {

            @Override
            public void onScrollUpDownChanged(int delta, int scrollPosition, boolean exact) {
                // TODO Auto-generated method stub
                if (exact) {
                    float tran_y = mQuickReturnView.getHeight() + delta;
                    if (tran_y >= 0) {
                        // mQuickReturnView.set
                    } else if (tran_y < -max_tranY) {
                        // mQuickReturnView.setTranslationY(-max_tranY);
                    } else {
                        // mQuickReturnView.setTranslationY(tran_y);
                    }
                }

            }

            @Override
            public void onScrollIdle() {
                // TODO Auto-generated method stub
            }
        });

    }

    @Override
    public void onClickCallback(int position) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSpeakClickCallback(int position) {
        final TextModel model = mJokeListAdapter.getItem(position);
        startSpeaking(model.getText());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseTTS();
    }

    @Override
    public void onPause() {
        Log.e("FragmentJokRecommend", "onPause()");
        stopSpeaking();
        super.onPause();
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {
        for (int position : reverseSortedPositions) {
//            mJokeListAdapter.remove(position);
        }
    }
}
