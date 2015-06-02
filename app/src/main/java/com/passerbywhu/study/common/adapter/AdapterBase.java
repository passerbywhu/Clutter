package com.passerbywhu.study.common.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuwenchao3 on 2015/5/18.
 */
public abstract class AdapterBase<T> extends BaseAdapter{
    protected Context mContext;

    public List<T> getList() {
        return mList;
    }

    public void setList(List<T> list) {
        mList = list;
        notifyDataSetChanged();
    }

    private List<T> mList;

    public AdapterBase(Context context) {
        mContext = context;
        if (context == null) {
            throw new NullPointerException("Context could not be null");
        }
    }

    @Override
    public int getCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mList == null) {
            return null;
        }
        if(position < 0 || position >= mList.size()) {
            return null;
        }
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void appendList(List<T> _list) {
        if (_list == null) {
            return;
        }
        if (mList == null) {
            mList = new ArrayList<T>();
        }
        mList.addAll(_list);
        notifyDataSetChanged();
    }

    public void clearList() {
        if (mList != null) {
            mList.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

}
