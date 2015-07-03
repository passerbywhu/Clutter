package com.passerbywhu.study.common.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.passerbywhu.study.R;

import butterknife.InjectView;

/**
 * Created by wuwenchao3 on 2015/6/30.
 */
public class BaseListActivity extends BaseActivity {
    @InjectView(R.id.listView)
    protected ListView mListView;
    private String s = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, s.split(" ")));
    }

    @Override
    protected int createChildViewId() {
        return R.layout.base_list_activity;
    }

    @Override
    protected String createTitle() {
        return "BaseListActivity";
    }
}
