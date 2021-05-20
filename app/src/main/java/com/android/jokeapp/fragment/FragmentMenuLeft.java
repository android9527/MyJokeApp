package com.android.jokeapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import static com.android.jokeapp.common.Constants.*;

import com.android.jokeapp.R;
import com.android.jokeapp.activity.DrawerLayoutActivity;
import com.android.jokeapp.common.Constants;
import com.android.jokeapp.util.SystemBarTintManager;

public class FragmentMenuLeft extends FragmentBase {

    private ListView mListView;

    //	private List<HashMap<String, Integer>> menuList = new ArrayList<HashMap<String, Integer>>();

    private int mCurrentPosition = 0;

    private MenuAdapter menuAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_left_menu, container, false);
//        initInsetTop(rootView);
//        mAdView = rootView.findViewById(R.id.banner_left);
//        showBanner(BANNER_LEFT);
        return rootView;
    }

    private DrawerItemClickListener clickListener;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) view.findViewById(R.id.left_listview);
        menuAdapter = new MenuAdapter();
        mListView.setAdapter(menuAdapter);
        clickListener = new DrawerItemClickListener();
        mListView.setOnItemClickListener(clickListener);
    }

    public void initInsetTop(View rootView) {
        SystemBarTintManager tintManager = new SystemBarTintManager(
                getActivity());
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        rootView.setPadding(0,
                config.getPixelInsetTop(true),
                config.getPixelInsetRight(),
                config.getPixelInsetBottom());
        rootView.requestLayout();
    }

    /**
     * This list item click listener implements very simple view switching by
     * changing the primary content text. The drawer is closed when a selection
     * is made.
     */
    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            mCurrentPosition = position;
            menuAdapter.notifyDataSetChanged();
            drawerLayoutActivity.switchContent(position);
        }
    }


    /**
     * 切换fragment
     */
    public void switchContent(int position) {
        mListView.performItemClick(mListView.getChildAt(position), position, mListView.getChildAt(position).getId());
    }

    private class MenuAdapter extends BaseAdapter {

        private ViewHolder holder;

        @Override
        public int getCount() {
            return LEFT_MENU.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(getActivity(),
                        R.layout.left_menu_item,
                        null);
                holder.menuTitle = (TextView) convertView.findViewById(R.id.tv_menu_title);
                holder.menuIcon = (ImageView) convertView.findViewById(R.id.iv_menu_icon);
                convertView.setTag(holder);
            } else {
                //				view = convertView;
                holder = (ViewHolder) convertView.getTag();
            }
            holder.menuTitle.setText(LEFT_MENU[position]);
            //			holder.menuIcon.setBackgroundResource(mMenuicons[position]);
            if (mCurrentPosition == position) {
                holder.menuIcon.setSelected(true);
                holder.menuTitle.setSelected(true);
            } else {
                holder.menuIcon.setSelected(false);
                holder.menuTitle.setSelected(false);
            }

            return convertView;
        }

    }

    class ViewHolder {
        TextView menuTitle;

        ImageView menuIcon;
    }

}
