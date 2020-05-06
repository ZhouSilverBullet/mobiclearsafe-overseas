package com.mobi.clearsafe.base.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;


import java.util.List;

/**
 * author : liangning
 * date : 2019-10-22  17:29
 */
public abstract class CommonAdapter<T> extends RecyclerView.Adapter<RecycleViewHolder> {

    private Context mContext;
    private int mLayoutId;
    private List<T> mData;


    public CommonAdapter(Context context, int layoutID, List<T> Datas) {
        this.mContext = context;
        this.mLayoutId = layoutID;
        this.mData = Datas;
    }

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecycleViewHolder viewHolder = RecycleViewHolder.getRecycleViewHolder(mContext, parent, mLayoutId);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder holder, int position) {
        convert(holder, mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public abstract void convert(RecycleViewHolder holder, T t);

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void addList(List<T> list) {
        if (this.mData != null) {
            mData.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void clearList() {
        if (mData != null) {
            this.mData.clear();
        }
        notifyDataSetChanged();
    }

    public void replaceList(List<T> list) {
        if(mData!=null){
            mData.clear();
            notifyDataSetChanged();
            mData.addAll(list);
            notifyDataSetChanged();
        }

    }

    public void addPosition(T t, int positon) {
        mData.add(positon, t);
        notifyItemInserted(positon);
        notifyItemRangeChanged(positon, mData.size() - positon);
    }

    public void remove(int position) {
        mData.remove(position);
        notifyDataSetChanged();
    }
    public List<T> getList(){
        return mData;
    }
}
