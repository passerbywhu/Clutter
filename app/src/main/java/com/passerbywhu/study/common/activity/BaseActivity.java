package com.passerbywhu.study.common.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.passerbywhu.study.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by wuwenchao3 on 2015/6/30.
 */
public class BaseActivity extends ActionBarActivity {
    @InjectView(R.id.toolbar)
    protected Toolbar mToolbar;

    protected FrameLayout container;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        container = (FrameLayout) findViewById(R.id.container);
        int childViewId = createChildViewId();
        if (childViewId != -1) {
            LayoutInflater.from(this).inflate(createChildViewId(), container, true);
        } else {
            View childView = createChildView();
            if (childView != null) {
                container.addView(childView);
            }
        }
        ButterKnife.inject(this);
        mToolbar.setTitle(createTitle());
        setSupportActionBar(mToolbar);
    }


    protected int createChildViewId() {
        return -1;
    }

    protected View createChildView() {
        return null;
    }

    protected String createTitle() {
        return "BaseActivity";
    }
}
