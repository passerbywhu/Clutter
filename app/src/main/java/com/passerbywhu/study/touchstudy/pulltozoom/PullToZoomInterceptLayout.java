package com.passerbywhu.study.touchstudy.pulltozoom;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Created by wuwenchao3 on 2015/4/3.
 */
public class PullToZoomInterceptLayout extends LinearLayout {
    private ImageView mHeaderView;
    private ListView mListView;
    private static final String TAG = "INTERCEPT_LAYOUT";
//    private boolean hasDispatched = false;
    private int mHeight;

    public PullToZoomInterceptLayout(Context context) {
        super(context);
    }

    public PullToZoomInterceptLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToZoomInterceptLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mHeaderView = (ImageView) getChildAt(0);
        mListView = (ListView) getChildAt(1);
        mHeaderView.post(new Runnable() {
            @Override
            public void run() {
                mHeight = mHeaderView.getLayoutParams().height;
            }
        });
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        super.requestDisallowInterceptTouchEvent(false);
    }

    float startPosY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e(TAG, "come in onInterceptTouchEvent");
        int action = ev.getAction();
        switch(action) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "intercept ACTION_DOWN");
                startPosY = ev.getY();
                return false;
            case MotionEvent.ACTION_MOVE:
                float speedY = ev.getY() - startPosY;
                startPosY = ev.getY();
                Log.e(TAG, "intercept ACTION_MOVE mHeaderView.getBottom() = " + mHeaderView.getBottom());
                if (mHeaderView.getBottom() > mHeight) {  //只要mHeaderView可见，那么都要拦截
                    Log.e(TAG, "intercept Bottom > 0 拦截");
                    return true;
                } else {   //mHeaderView.getBottom() <= 0  实际上不会有小于0的情况
                    Log.e(TAG, "intercept mListView.getChildAt(0).getTop() = " + mListView.getChildAt(0).getTop() + " speedY = " + speedY);
                    if (mListView.getChildAt(0).getTop() == 0 && speedY > 0 && mListView.getFirstVisiblePosition() == 0) {   //把mHeaderView拉成可见
                        Log.e(TAG, "intercept 进入临界点，拦截");
                        return true;
                    }
                    Log.e(TAG, "intercept 不拦截");
                    return false; //不拦截
                }
            case MotionEvent.ACTION_CANCEL:
                Log.e(TAG, "intercept ACTION_CANCEL **********");
                return super.onInterceptTouchEvent(ev);
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "intercept ACTION_UP **********");
                return super.onInterceptTouchEvent(ev);
        }
        Log.e(TAG, "intercept final place");
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "come in onTouchEvent");
        int action = event.getAction();
        switch(action) {
            case MotionEvent.ACTION_DOWN:
                //理论上不会进到这里。不拦截ACTION_DOWN;
                Log.e(TAG, "touch ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                float speedY = event.getY() - startPosY;
                startPosY = event.getY();
                float newBottom = mHeaderView.getBottom() + speedY;
                Log.e(TAG, "touch ACTION_MOVE speedY = " +  speedY + " newBottom = " + newBottom);
                if (newBottom >= mHeight) {  //修改大小就好了
//                    hasDispatched = false;
                    mHeaderView.getLayoutParams().height = (int) newBottom;
                    requestLayout();
                    return true;
                } else {  //newBottom < 0
                        mHeaderView.getLayoutParams().height = mHeight; //停止拦截
                        Log.e(TAG, "touch 进入临界点，取消touch事件");
                        MotionEvent cancelEvent = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, event.getX(), event.getY(), 1);
                        dispatchTouchEvent(cancelEvent);
                        MotionEvent downEvent = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, event.getX(), event.getY(), 1);
                        dispatchTouchEvent(downEvent);  //这里是否可以super.onTouchEvent还是应该调用super.onInterceptTouch?
                        requestLayout();
                        return true;
                }
            case MotionEvent.ACTION_CANCEL:
                Log.e(TAG, "onTouch cancel");
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "onTouch UP");
                break;
        }
        Log.e(TAG, "onTouch final");
        return super.onTouchEvent(event);
    }
}
