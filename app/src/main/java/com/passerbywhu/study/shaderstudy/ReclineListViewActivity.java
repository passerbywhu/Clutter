package com.passerbywhu.study.shaderstudy;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.passerbywhu.study.R;
import com.passerbywhu.study.common.activity.BaseActivity;

import butterknife.InjectView;

/**
 * Created by wuwenchao3 on 2015/8/14.
 */
public class ReclineListViewActivity extends BaseActivity {
    @InjectView(R.id.reclineList)
    protected ReclineListView mList;

    @Override
    protected int createChildViewId() {
        return R.layout.activity_recline_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, "a b c d e f g h i j k l m n o p q r s t u v w x y z".split(" ")));
    }
}
