package com.mobi.overseas.clearsafe.base.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecycleViewHolder> {

    private Context mContext;

    private ArrayList<T> mDates;

    private OnItemClickListener onItemClickListener;

    private LayoutInflater mInflater;

    public BaseAdapter(Context mContext,ArrayList<T> mDates) {
        this.mContext = mContext;
        this.mDates=mDates;
        mInflater=LayoutInflater.from(mContext);
    }

    public ArrayList<T> getmDates() {
        return mDates;
    }

    public LayoutInflater getmInflater() {
        return mInflater;
    }

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = onLoadItemView(parent, viewType);
        RecycleViewHolder recyclerViewHoder=new RecycleViewHolder(mContext,view);
        return recyclerViewHoder;
    }

     abstract View onLoadItemView(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(@NonNull final RecycleViewHolder holder, final int position) {

        if(mDates!=null){
            convert(holder,mDates.get(position),position);
        }

        holder.contextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick(view,holder,position);
                }
            }
        });
    }

     abstract void convert(RecycleViewHolder holder, T t, int position);



    interface OnItemClickListener {
        void onItemClick(View view, RecycleViewHolder holder, int position);
    }

    void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public int getItemCount() {
        return mDates.size();
    }


    void addList(List<T> list){
        if(this.mDates!=null){
            mDates.addAll(list);
        }
        notifyDataSetChanged();
    }

    void clearList(){
        if(mDates!=null){
            this.mDates.clear();
        }
        notifyDataSetChanged();
    }

    void replaceList(List<T> list){
        mDates.clear();
        notifyDataSetChanged();
        mDates.addAll(list);
        notifyDataSetChanged();
    }

    void addPosition(T t,int positon){
        mDates.add(positon,t);
        notifyItemInserted(positon);
        notifyItemRangeChanged(positon,mDates.size()-positon);
    }

    void remove(int position){
        mDates.remove(position);
        notifyDataSetChanged();
    }
}
