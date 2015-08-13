package com.passerbywhu.study.touchstudy.dragstudy;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.passerbywhu.study.MyApplication;
import com.passerbywhu.study.R;
import com.passerbywhu.study.common.consts.Const;

/**
 * Created by wuwenchao3 on 2015/4/29.
 */
public class GetViewTestActivity extends Activity {
    private ListView mListView;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getview_test);
        mListView = (ListView) findViewById(R.id.testList);
        mListView.setAdapter(new MyAdapter());
        toast = Toast.makeText(this, "", Toast.LENGTH_LONG);
    }

    static class ViewHolder {
        NetworkImageView netImageView;
    }

    class MyAdapter extends BaseAdapter {
        private String[] urls = Const.imgUrls;

        @Override
        public int getCount() {
            return urls.length;
        }

        @Override
        public Object getItem(int position) {
            return urls[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(GetViewTestActivity.this).inflate(R.layout.getview_test_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.netImageView = (NetworkImageView) convertView.findViewById(R.id.netImageView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            toast.setText("getView executed");
            toast.show();
            viewHolder.netImageView.setErrorImageResId(R.drawable.default_place_holder);
            viewHolder.netImageView.setDefaultImageResId(R.drawable.default_place_holder);
            viewHolder.netImageView.setImageUrl(urls[position], MyApplication.imageLoader);
            return convertView;
        }
    }
}
