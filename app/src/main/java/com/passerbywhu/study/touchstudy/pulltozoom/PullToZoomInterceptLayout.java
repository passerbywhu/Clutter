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
//    private ListView mListView;
    private ImageView mHeaderView;
    private ListView mListView;
//    private View mListView;
    private static int COUNT = 0;
    private int thisCount = COUNT ++;

    private static final String TAG = "INTERCEPT_LAYOUT";

    public PullToZoomInterceptLayout(Context context) {
        super(context);
        init();
    }

    public PullToZoomInterceptLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PullToZoomInterceptLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        post(new Runnable() {
            @Override
            public void run() {
                mHeaderView = (ImageView) getChildAt(0);
                mListView = (ListView) getChildAt(1);
//                mListView = getChildAt(1);
            }
        });
    }

//    int moveNum = 0;
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        if (thisCount == 2) {
//            int action = ev.getAction();
//            if (action == MotionEvent.ACTION_DOWN) {
//                Log.e(TAG, "count 2 onIntercept Down");
//                return false;
//            }
//            if (action == MotionEvent.ACTION_MOVE) {
//                Log.e(TAG, "count 2 onIntercept MOVE");
//                moveNum ++;
//                if (moveNum == 100) {
//                    Log.e(TAG, "count 2 intercepted Move return true");
//                    moveNum ++;
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        } else {
//            return super.onInterceptTouchEvent(ev);
//        }
//        return super.onInterceptTouchEvent(ev);
//    }
//
//    int count2Num = 0;
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (thisCount == 2) {
//            Log.e(TAG, "count 2 onTouch");
//            count2Num ++;
//            if (count2Num == 100) {
//                count2Num ++;
//                Log.e(TAG, "count2 100 停止拦截");
//                MotionEvent cancelEvent = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, event.getX(), event.getY(), 1);
//                dispatchTouchEvent(cancelEvent);
//                MotionEvent downEvent = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, event.getX(), event.getY(), 1);
//                dispatchTouchEvent(downEvent);  //这里是否可以super.onTouchEvent还是应该调用super.onInterceptTouch?
//                return true;
//            } else {
//                return true;
//            }
//        }
//        if (thisCount == 3) {
//            Log.e(TAG, "count 3 onTouch");
//            return true;
//        }
//        return super.onTouchEvent(event);
//    }

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
                if (mHeaderView.getBottom() > 0) {  //只要mHeaderView可见，那么都要拦截
                    Log.e(TAG, "intercept Bottom > 0 拦截");
                    return true;
                } else {   //mHeaderView == 0
//                    Log.e(TAG, "intercept mListView.getChildAt(0).getTop() = " + mListView.getChildAt(0).getTop() + " speedY = " + speedY);
//                    if (mListView.getChildAt(0).getTop() == 0 && speedY > 0) {   //把mHeaderView拉成可见
//                        Log.e(TAG, "intercept 进入临界点，拦截");
//                        return true;
//                    }
                    Log.e(TAG, "intercept ACTION_MOVE mListView.getScrollY = " + mListView.getScrollY() + " speedY = " + speedY);
                    if (mListView.getScrollY() <= 0 && speedY > 0) {  //mHeaderView被拉可见，也就是scrollY应该是小于等于0的情况
                        mListView.scrollBy(0, -mListView.getScrollY()); //置为0
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
                if (newBottom >= 0) {  //修改大小就好了
                    mHeaderView.getLayoutParams().height = (int) newBottom;
                    requestLayout();
                    return true;
                } else {
                    mHeaderView.getLayoutParams().height = 0; //停止拦截
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
