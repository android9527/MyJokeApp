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
import com.android.jokeapp.util.SystemBarTintManager;

public class FragmentMenuLeft extends FragmentBase
{
    
private ListView mListView;
    
    //	private List<HashMap<String, Integer>> menuList = new ArrayList<HashMap<String, Integer>>();
    
    private int mCurrentPosition = 0;
    
    private MenuAdapter menuAdapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.layout_left_menu, container, false);
//        initInsetTop(rootView); 
        showBanner(rootView);
        return rootView;
    }
    
    
    protected void showBanner(View view) {

        
        
        // 插播接口调用
           // 开发者可以到开发者后台设置展示频率，需要到开发者后台设置页面（详细信息->业务信息->无积分广告业务->高级设置）
           // 自4.03版本增加云控制是否开启防误点功能，需要到开发者后台设置页面（详细信息->业务信息->无积分广告业务->高级设置）

           // 加载插播资源
//           SpotManager.getInstance(getActivity()).loadSpotAds();
           // 设置展示超时时间，加载超时则不展示广告，默认0，代表不设置超时时间
//           SpotManager.getInstance(getActivity()).setSpotTimeout(10000);// 5秒
                   
               // 展示插播广告，可以不调用loadSpot独立使用
//           SpotManager.getInstance(getActivity()).showSpotAds(getActivity(), new SpotDialogListener() {
//               @Override
//               public void onShowSuccess() {
//               Log.i("SpotAd", "展示成功");
//               }
//
//               @Override
//               public void onShowFailed() {
//               Log.i("SpotAd", "展示失败");
//               }
//
//               @Override
//               public void onSpotClosed()
//               {
//                   // TODO Auto-generated method stub
//                   
//               }
//           });  
           
           
           
           // 广告条接口调用（适用于应用）
           // 将广告条adView添加到需要展示的layout控件中
//            LinearLayout adLayout = (LinearLayout) view.findViewById(R.id.adLayout);
//            AdView adView = new AdView(getActivity(), AdSize.FIT_SCREEN);
//         // 监听广告条接口
//            adView.setAdListener(new AdViewListener() {
//
//                @Override
//                public void onSwitchedAd(AdView arg0) {
//                    Log.i("YoumiAdDemo", "广告条切换");
//                }
//
//                @Override
//                public void onReceivedAd(AdView arg0) {
//                    Log.i("YoumiAdDemo", "请求广告成功");
//
//                }
//
//                @Override
//                public void onFailedToReceivedAd(AdView arg0) {
//                    Log.i("YoumiAdDemo", "请求广告失败");
//                }
//            });
//            adLayout.addView(adView);

           // 广告条接口调用（适用于游戏）

           // 实例化LayoutParams(重要)
//           FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
//                   FrameLayout.LayoutParams.WRAP_CONTENT);
//           // 设置广告条的悬浮位置
//           layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT; // 这里示例为右下角
           // 实例化广告条
//           AdView adView = new AdView(this, AdSize.FIT_SCREEN);
           // 调用Activity的addContentView函数

           // 监听广告条接口
//           adView.setAdListener(new AdViewListener() {
   //
//               @Override
//               public void onSwitchedAd(AdView arg0) {
//                   Log.i("YoumiAdDemo", "广告条切换");
//               }
   //
//               @Override
//               public void onReceivedAd(AdView arg0) {
//                   Log.i("YoumiAdDemo", "请求广告成功");
   //
//               }
   //
//               @Override
//               public void onFailedToReceivedAd(AdView arg0) {
//                   Log.i("YoumiAdDemo", "请求广告失败");
//               }
//           });
//           this.addContentView(adView, layoutParams);
       }
    
    private DrawerItemClickListener clickListener;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) view.findViewById(R.id.left_listview);
        menuAdapter = new MenuAdapter();
        mListView.setAdapter(menuAdapter);
        clickListener = new DrawerItemClickListener();
        mListView.setOnItemClickListener(clickListener);
    }
    
    public void initInsetTop(View rootView)
    {
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
            ListView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id)
        {
            mCurrentPosition = position;
            menuAdapter.notifyDataSetChanged();
            drawerLayoutActivity.switchContent(position);
        }
    }
    
    
    /**
     * 切换fragment
     */
    public void switchContent(int position)
    {
        mListView.performItemClick(mListView.getChildAt(position), position, mListView.getChildAt(position).getId());
    }
    
    private class MenuAdapter extends BaseAdapter
    {
        
        private ViewHolder holder;
        
        @Override
        public int getCount()
        {
            return LEFT_MENU.length;
        }
        
        @Override
        public Object getItem(int position)
        {
            return position;
        }
        
        @Override
        public long getItemId(int position)
        {
            return position;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                holder = new ViewHolder();
                convertView = View.inflate(getActivity(),
                        R.layout.left_menu_item,
                        null);
                holder.menuTitle = (TextView) convertView.findViewById(R.id.tv_menu_title);
                holder.menuIcon = (ImageView) convertView.findViewById(R.id.iv_menu_icon);
                convertView.setTag(holder);
            }
            else
            {
                //				view = convertView;
                holder = (ViewHolder) convertView.getTag();
            }
            holder.menuTitle.setText(LEFT_MENU[position]);
            //			holder.menuIcon.setBackgroundResource(mMenuicons[position]);
            if (mCurrentPosition == position)
            {
                holder.menuIcon.setSelected(true);
                holder.menuTitle.setSelected(true);
            }
            else
            {
                holder.menuIcon.setSelected(false);
                holder.menuTitle.setSelected(false);
            }
            
            return convertView;
        }
        
    }
    
    class ViewHolder
    {
        TextView menuTitle;
        
        ImageView menuIcon;
    }
    
}
