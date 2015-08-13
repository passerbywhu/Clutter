package com.passerbywhu.study.touchstudy.pulltozoom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.passerbywhu.study.R;

/**
 * Created by wuwenchao3 on 2015/3/26.
 */
public class MyPullToZoomListView extends ListView implements AbsListView.OnScrollListener {
    private Context context;
    private View mHeaderView;
    private ImageView mHeaderImg;
    private int minHeight;
    private int initialHeight;
    private static final String TAG = "PullZoom";
    private ListView listView;

    public MyPullToZoomListView(Context context) {
        super(context);
        init();
    }

    public MyPullToZoomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public MyPullToZoomListView(Context context, AttributeSet attrs, int defStyleRes) {
        super(context, attrs, defStyleRes);
        this.context = context;
        init();
    }

    float startPosY;
    boolean hasDispatchTouchEvent = false;
    int maxHeight = getResources().getDisplayMetrics().heightPixels;

    private void init() {
        listView = this;
        mHeaderView = LayoutInflater.from(context).inflate(R.layout.pull_header_view, null ,false);
        mHeaderImg = (ImageView) mHeaderView.findViewById(R.id.headerImage);
        Bitmap bm = BitmapFactory.decodeResource(this.getResources(), R.drawable.splash02);
        BitmapDrawable drawable = new BitmapDrawable(this.getResources(), bm);
        mHeaderImg.setImageDrawable(drawable);
//        mHeaderImg.setImageResource(R.drawable.splash01);
//        BitmapDrawable drawable = (BitmapDrawable) mHeaderImg.getDrawable();
        minHeight = drawable.getMinimumHeight();
        initialHeight = minHeight / 3;
        mHeaderView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, initialHeight));
        addHeaderView(mHeaderView);

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch(action) {
                    case MotionEvent.ACTION_DOWN:
                        mLastEventY = event.getY();
                        Log.e(TAG, "return true ACTION_DOWN");
                        return false;
                    case MotionEvent.ACTION_MOVE:
                        float speedY = event.getY() - mLastEventY;
                        mLastEventY = event.getY();
                        int bottom = mHeaderView.getBottom();
                        if (bottom >= initialHeight) {
                            int newBottom = (int) (bottom + speedY);
                            if (newBottom < initialHeight) {
                                if (!hasDispatched) {
                                    mHeaderView.getLayoutParams().height = initialHeight;
                                    requestLayout();
                                    hasDispatched = true;
                                    Log.e(TAG, "return false ACTION_MOVE ThresHold");
                                    //这里的关键就是要伪造一个正常的cancel和down事件。方法1
                                    MotionEvent cancel = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, event.getX(), event.getY(), 1);
                                    dispatchTouchEvent(cancel);
                                    MotionEvent down = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, event.getX(), event.getY(), 1);
                                    dispatchTouchEvent(down);
                                    return true;  //这里不能简单的return false

                                    //方法2
//                                    MotionEvent cancel = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, event.getX(), event.getY(), 1);
//                                    onTouchEvent(cancel);
//                                    MotionEvent down = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, event.getX(), event.getY(), 1);
//                                    onTouchEvent(down);
//                                    return true;

//                                    return false;
                                } else {
                                    return false;
                                }
                            }
                            hasDispatched = false;
                            mHeaderView.getLayoutParams().height = newBottom;
                            requestLayout();
                            Log.e(TAG, "return true ACTION_MOVE SCALE");
                            return true;
                        }
                        Log.e(TAG, "return false ACTION_MOVE BY LIST " + mHeaderView.getBottom() + " " + initialHeight);
                        return false;
                    case MotionEvent.ACTION_UP:
                        Log.e(TAG, "ACTION_UP");
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        Log.e(TAG, "ACTION_CANCEL");
                        break;
                    case MotionEvent.ACTION_OUTSIDE:
                        Log.e(TAG, "ACTION_OUTSIDE");
                        break;
                    case MotionEvent.ACTION_SCROLL:
                        Log.e(TAG, "ACTION_SCROLL");
                        break;
                    default:
                        Log.e(TAG, "return false DEFAULT");
                        return false;
                }
                Log.e(TAG, "return false break");
                return false;
            }
        });

        setOnScrollListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        String s = "";
        if (ev.getAction() == ev.ACTION_DOWN) {
            s = "ACTION_DOWN";
        }
        if (ev.getAction() == ev.ACTION_UP) {
            s = "ACTION_UP";
        }
        if (ev.getAction() == ev.ACTION_MOVE) {
            s = "ACTION_MOVE";
        }
        Log.e(TAG, "onTouchEvent executed " + s);
        return super.onTouchEvent(ev);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    private float mLastEventY;
//    private boolean hasDispatched = false;


//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        int action = ev.getAction();
//        switch(action) {
//            case MotionEvent.ACTION_DOWN:
//                mLastEventY = ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float bottom = mHeaderView.getBottom();
//                float speedY = ev.getY() - mLastEventY;
//                mLastEventY = ev.getY();
//                if (bottom >= initialHeight) {  //当bottom >= height的时候，也即是在缩放状态，自己处理。
//                    processScale(speedY, ev);
//                    return true;
//                }
//                return super.onTouchEvent(ev);
//            case MotionEvent.ACTION_UP:
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                break;
//            default:
//                return super.onTouchEvent(ev);
//        }
//        return super.onTouchEvent(ev);
//    }

    private boolean hasDispatched = false;

    //bottom >= Height的情况
    private boolean processScale(float speedY, MotionEvent e) {
        int headerBottom = mHeaderView.getBottom();
        int newBottom = (int) (headerBottom + speedY);
        Log.e(TAG, "speedY = " + speedY + " newBottom = " + newBottom + " bottom = " + headerBottom);

        if (newBottom < initialHeight) {  //滑过了临界点，已经非缩放状态了。这个时候交回给listView处理 //应该同时恢复状态
            if (!hasDispatched) {
                Log.e(TAG, "从下往上进入临界点"); //不能简单的return super.onTouchEvent();
                hasDispatched = true;
                LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
                params.height = initialHeight;
                mHeaderView.setLayoutParams(params);
                requestLayout();
                MotionEvent event = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL,
                        e.getX(), e.getY(), 1);
                super.onTouchEvent(event);
                event = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, e.getX(), e.getY(), 1);
                super.onTouchEvent(event);
                return true;
            } else {
                return super.onTouchEvent(e);
            }
        }
        hasDispatched = false;
        LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
        params.height = newBottom;
        mHeaderView.setLayoutParams(params);
        requestLayout();
        return true;
    }
}
