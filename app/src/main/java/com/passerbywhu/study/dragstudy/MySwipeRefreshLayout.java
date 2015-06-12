package com.passerbywhu.study.dragstudy;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * Created by wuwenchao3 on 2015/5/18.
 */
public class MySwipeRefreshLayout extends SwipeRefreshLayout {
    public MySwipeRefreshLayout(Context context) {
        super(context);
    }

    public MySwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean b) {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(b);
        }
    }
}
