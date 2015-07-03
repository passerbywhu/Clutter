package com.passerbywhu.study.widgetstudy.edgeeffect;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.passerbywhu.study.common.activity.BaseActivity;

/**
 * Created by wuwenchao3 on 2015/6/30.
 */
public class EdgeEffectStudy extends BaseActivity {
    EdgeEffectListView mEdgeEffectListView;
    private String s = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mEdgeEffectListView != null) {
            mEdgeEffectListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, s.split(" ")));
        }
    }

    @Override
    protected View createChildView() {
        mEdgeEffectListView = new EdgeEffectListView(this);
        mEdgeEffectListView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return mEdgeEffectListView;
    }

    @Override
    protected String createTitle() {
        return "EdgeEffectStudy";
    }
}
