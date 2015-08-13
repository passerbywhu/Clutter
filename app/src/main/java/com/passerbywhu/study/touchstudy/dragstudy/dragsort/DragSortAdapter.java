package com.passerbywhu.study.touchstudy.dragstudy.dragsort;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.passerbywhu.study.common.adapter.AdapterBase;

/**
 * Created by wuwenchao3 on 2015/7/6.
 */
public class DragSortAdapter extends AdapterBase {
    public DragSortAdapter(Context context) {
        super(context);
    }

    public static class ViewHolder {
        public TextView textView;
        public int position;

        public ViewHolder(TextView textView) {
            this.textView = textView;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
//            convertView = LayoutInflater.from(mContext).inflate(R.layout.drag_sort_item, parent, false);
//            viewHolder = new ViewHolder((TextView) convertView.findViewById(R.id.textView));
            TextView textView = new TextView(mContext);
            textView.setLayoutParams(new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300));
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundColor(mContext.getResources().getColor(android.R.color.darker_gray));
            textView.setTextColor(mContext.getResources().getColor(android.R.color.black));
            convertView = textView;
            viewHolder = new ViewHolder((TextView) convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(getItem(position) + " Block");
        viewHolder.position = position;
        return convertView;
    }
}
