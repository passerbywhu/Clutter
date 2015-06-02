package com.passerbywhu.study.dragLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by wuwenchao3 on 2015/5/18.
 */
public class MyRelativeLayout extends RelativeLayout {
    public void setBesideDragLayout(BesideDragLayout besideDragLayout) {
        mBesideDragLayout = besideDragLayout;
    }

    private BesideDragLayout mBesideDragLayout;

    public MyRelativeLayout(Context context) {
        super(context);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mBesideDragLayout.getStatus() == BesideDragLayout.Status.Open) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mBesideDragLayout.getStatus() != BesideDragLayout.Status.Close) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                mBesideDragLayout.close();
            }
        }
        return super.onTouchEvent(event);
    }
}
