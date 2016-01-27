package com.android.jokeapp.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jokeapp.R;
import com.android.jokeapp.model.TextModel;

public class MyJokeListAdapter extends BaseAdapter
{
    
private List<TextModel> list;
    
    private ViewHolder holder;
    
    private Context context;
    
    private IOnClickCallback iOnClickCallback; // 点击回调
    
    private int commentColor, normalColor;
    
    public MyJokeListAdapter(Context context, List<TextModel> list)
    {
        this.context = context;
        this.list = list;
        commentColor = context.getResources().getColor(R.color.comment_color);
        normalColor = context.getResources().getColor(R.color.black);
    }
    
    /**
     * 底部添加数据
     * 
     * @param list
     */
    public void addToBottom(List<TextModel> list)
    {
        this.list.addAll(list);
        notifyDataSetChanged();
    }
    
    /**
     * 头部添加数据
     * @param list
     */
    public void addToTop(List<TextModel> list)
 {
		this.list.addAll(0, list);
		notifyDataSetChanged();
	}
    
    @Override
    public int getCount()
    {
        return list.size();
    }
    
    @Override
    public TextModel getItem(int position)
    {
        return list.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    
    @Override
    public int getItemViewType(int position)
    {
        return position;
    }
    
    @Override
    public View getView(final int position, View convertView,
            final ViewGroup parent)
    {
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.list_item_joke, null);
            holder.menuTitle = (TextView) convertView.findViewById(R.id.item_text_content);
            holder.llDingLayout = (LinearLayout) convertView.findViewById(R.id.ll_ding);
            
            holder.llCaiLayout = (LinearLayout) convertView.findViewById(R.id.ll_cai);
            holder.llShare = (LinearLayout) convertView.findViewById(R.id.ll_share);
            holder.itemTextDing = (TextView) convertView.findViewById(R.id.item_text_ding);
            holder.itemTextCai = (TextView) convertView.findViewById(R.id.item_text_cai);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        
        final TextModel textModel = list.get(position);
        String text = textModel.getText();
		if (!TextUtils.isEmpty(text)) {
			holder.menuTitle.setText(Html.fromHtml(text).toString().trim());
		}
        
        holder.itemTextDing.setText(String.valueOf(textModel.getDingNumber()));
        holder.itemTextCai.setText(String.valueOf(textModel.getCaiNumber()));
        
        holder.llDingLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onClickView(v, textModel);
            }
        });
        
        holder.llCaiLayout.setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                onClickView(v, textModel);
            }
        });
        holder.llShare.setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if(iOnClickCallback != null){
                    iOnClickCallback.onSpeakClickCallback(position);
                }
            }
        });
        
        /**
         * 被ding过，设置ding按钮和cai按钮不可点击
         */
        if (textModel.isHasDing())
        {
            holder.llDingLayout.setClickable(false);
            holder.llCaiLayout.setClickable(false);
            holder.llDingLayout.setSelected(true);
            holder.llCaiLayout.setSelected(false);
            holder.itemTextDing.setTextColor(commentColor);
            holder.itemTextCai.setTextColor(normalColor);
        }
     // 被cai过
        else if (textModel.isHasCai())
        {
            holder.llDingLayout.setClickable(false);
            holder.llCaiLayout.setClickable(false);
            holder.llCaiLayout.setSelected(true);
            holder.llDingLayout.setSelected(false);
            holder.itemTextDing.setTextColor(normalColor);
            holder.itemTextCai.setTextColor(commentColor);
        }
        else
        {
            holder.llDingLayout.setClickable(true);
            holder.llCaiLayout.setClickable(true);
            holder.llCaiLayout.setSelected(false);
            holder.llDingLayout.setSelected(false);
            holder.itemTextDing.setTextColor(normalColor);
            holder.itemTextCai.setTextColor(normalColor);
        }
        
        return convertView;
    }
    
    class ViewHolder
    {
        private TextView menuTitle;
        
        private LinearLayout llDingLayout, llCaiLayout, llShare;
        
        private TextView itemTextDing, itemTextCai;
    }
    
    /**
     * ding按钮和cai按钮点击事件
     * 
     * @param v
     * @param position
     */
    private void onClickView(View v, TextModel textModel)
    {
//        if (iOnClickCallback != null)
            //            iOnClickCallback.onClickCallback(position);
            dianZanAnim(v);
        //        final TextModel model = list.get(position);
        switch (v.getId())
        {
            case R.id.ll_ding:
                textModel.setDingNumber(textModel.getDingNumber() + 1);
                textModel.setHasDing(true);
                break;
            case R.id.ll_cai:
                textModel.setCaiNumber(textModel.getCaiNumber() + 1);
                textModel.setHasCai(true);
                break;
            
            default:
                break;
        }
        notifyDataSetChanged();
    }
    
    /**
     * 点击按钮 +1 动画平移 + 渐变
     * 
     * @param dianZanView
     */
    private void dianZanAnim(View dianZanView)
    {
        Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.dianzan);
        switch (dianZanView.getId())
        {
            case R.id.ll_ding:
                final View view = dianZanView.findViewById(R.id.tv_ding_one);
                animation.setAnimationListener(new AnimationListener()
                {
                    
                    @Override
                    public void onAnimationStart(Animation animation)
                    {
                    }
                    
                    @Override
                    public void onAnimationRepeat(Animation animation)
                    {
                    }
                    
                    @Override
                    public void onAnimationEnd(Animation animation)
                    {
                        view.setVisibility(View.INVISIBLE);
                    }
                });
                view.setVisibility(View.VISIBLE);
                view.startAnimation(animation);
                break;
            case R.id.ll_cai:
                final View caiView = dianZanView.findViewById(R.id.tv_cai_one);
                animation.setAnimationListener(new AnimationListener()
                {
                    
                    @Override
                    public void onAnimationStart(Animation animation)
                    {
                    }
                    
                    @Override
                    public void onAnimationRepeat(Animation animation)
                    {
                    }
                    
                    @Override
                    public void onAnimationEnd(Animation animation)
                    {
                        caiView.setVisibility(View.INVISIBLE);
                        //                        notifyDataSetChanged();
                    }
                });
                caiView.setVisibility(View.VISIBLE);
                caiView.startAnimation(animation);
                break;
        }
    }
    
    public interface IOnClickCallback
    {
        void onClickCallback(int position);
        void onSpeakClickCallback(int position);
    }
    
    public void registeIOnClickCallback(IOnClickCallback iOnClickCallback)
    {
        this.iOnClickCallback = iOnClickCallback;
    }
    
    public void unRegisteIOnClickCallback()
    {
        this.iOnClickCallback = null;
    }
}
