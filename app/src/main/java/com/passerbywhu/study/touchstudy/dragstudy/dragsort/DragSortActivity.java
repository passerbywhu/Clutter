package com.passerbywhu.study.touchstudy.dragstudy.dragsort;

import android.os.Bundle;

import com.passerbywhu.study.R;
import com.passerbywhu.study.common.activity.BaseActivity;

/**
 * Created by wuwenchao3 on 2015/7/6.
 */
public class DragSortActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            DragSortFragment dragSortFragment = new DragSortFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.container, dragSortFragment, "DragSort").commit();
        }
    }
}
