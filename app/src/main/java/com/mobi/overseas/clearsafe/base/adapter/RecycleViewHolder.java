package com.mobi.overseas.clearsafe.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class RecycleViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;
    protected View contextView;

    private SparseArray<View> mViews = new SparseArray<>();

    public RecycleViewHolder(Context mContext, View contextView) {
        super(contextView);
        this.mContext = mContext;
        this.contextView = contextView;
    }


    public static RecycleViewHolder getRecycleViewHolder(Context context, ViewGroup parent, int layoutId) {

        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        RecycleViewHolder viewHolder = new RecycleViewHolder(context, itemView);
        return viewHolder;
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = contextView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 设置TextView 文字
     *
     * @param viewId
     * @param text
     * @return
     */
    public RecycleViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public RecycleViewHolder setText(int viewId, SpannableStringBuilder text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    /**
     * 设置图片
     *
     * @param viewId
     * @param resId
     * @return
     */
    public RecycleViewHolder setImageResource(int viewId, int resId) {
        ImageView iv = getView(viewId);
        iv.setImageResource(resId);
        return this;
    }


    /**
     * 事件点击
     *
     * @param viewId
     * @param listener
     * @return
     */
    private RecycleViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    /**
     * 触摸事件
     *
     * @param viewId
     * @param listener
     * @return
     */
    private RecycleViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }


}
