//package com.passerbywhu.study.databindstudy;
//
//import android.app.Activity;
//import android.databinding.DataBindingUtil;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ListView;
//
//import com.passerbywhu.study.R;
//import com.passerbywhu.study.common.model.User;
//import com.passerbywhu.study.databinding.DataBindBinding;
//import com.passerbywhu.study.databinding.DataBindItemBinding;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by wuwenchao3 on 2015/9/12.
// */
//public class DataBindActivity extends Activity {
//    ListView mList;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        DataBindBinding binding = DataBindingUtil.setContentView(this, R.layout.data_bind);
//        User user = new User("Test", "User");
//        binding.setUser(user);
//        mList = (ListView) findViewById(R.id.list);
//        DataBindAdapter mAdatper = new DataBindAdapter();
//        List<User> mDataList = new ArrayList<User>();
//        for (int i = 0; i < 100; i ++) {
//            User user_l = new User("name " + i, " lastName " + i);
//            mDataList.add(user_l);
//        }
//        mAdatper.setData(mDataList);
//        mList.setAdapter(mAdatper);
//    }
//
//    private class DataBindAdapter extends BaseAdapter {
//        List mData;
//
//        public void setData(List mData) {
//            this.mData = mData;
//        }
//
//        @Override
//        public int getCount() {
//            return mData.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return mData.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            if (convertView == null) {
//                convertView = LayoutInflater.from(DataBindActivity.this).inflate(R.layout.data_bind_item, parent, false);
//                DataBindingUtil.bind(convertView);
//            }
//            DataBindItemBinding binding = DataBindingUtil.getBinding(convertView);
//            if (binding != null) {
//                binding.setUser((User) getItem(position));
//            }
//            return convertView;
//        }
//    }
//}
