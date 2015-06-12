package com.passerbywhu.study.dragstudy;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.nineoldandroids.view.ViewHelper;
import com.passerbywhu.study.common.consts.Const;

/**
 * Created by wuwenchao3 on 2015/5/14.
 */
public class BesideDragLayout extends FrameLayout {
    private GestureDetectorCompat mGestureDetectorCompat;
    private ViewDragHelper mDragerHelper;
    private int range;
    private int width;
    private int height;
    private int splitLine;
    private Context context;
    private RelativeLayout leftLayout;
    private MyRelativeLayout rightLayout;
    private GestureDetector.SimpleOnGestureListener YScrollListener;

    private boolean isYScrolled = false;

    public enum Status {
        Drag, Open, Close
    }

    public Status getStatus() {
        if (splitLine == 0) {
            mStatus = Status.Close;
        } else if (splitLine == range) {
            mStatus = Status.Open;
        } else {
            mStatus = Status.Drag;
        }
        return mStatus;
    }

    private Status mStatus = Status.Close;

    public interface DragListener {
        public void onOpen();
        public void onClose();
        public void onDrag(float percent);
    }

    public void setDragListener(DragListener dragListener) {
        mDragListener = dragListener;
    }

    private DragListener mDragListener;


    public BesideDragLayout(Context context) {
        this(context, null);
    }

    public BesideDragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public BesideDragLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        YScrollListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                //当检测到是Y滑动手势的时候，说明用户在滑动rightLayout的列表数据或者在下拉刷新。不需要ViewDragHelper去拦截。
                //否则的话，可能在竖直滑动列表的时候，整个rightLayout被左右移动了。结果就滑动不了列表了。
                //不是如上述的原因。 因为假设在滑动ListView。此时distanY >= distanceX,不需要拦截，但是只要一左右滑动，就被拦截了。然后就进入onTouch，不会
                //再进入interceptTouch了。
                //但是原Demo是可以的。因为一旦distanceY > distanceX了。那么Listview会reqeustDisallowIntercept，就达到了上述效果
//                return Math.abs(distanceY) >= Math.abs(distanceX);

                //套了SwipeRefreshLayout后的解决方案
//                if (Math.abs(distanceY) > 0) {
//                    isYScrolled = true;
//                }
                return Math.abs(distanceY) >= Math.abs(distanceX);
            }
        };
        mGestureDetectorCompat = new GestureDetectorCompat(context, YScrollListener);
        mDragerHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
//            @Override
//            public int clampViewPositionVertical(View child, int top, int dy) {
//                return 0;  //这样在垂直方向就不会intercept。bug就是如果竖直滑动一段距离。然后放开会触发click
//            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if (splitLine + dx < 0) {
                    return 0;
                } else if (splitLine + dx > range) {
                    return range;
                } else {
                    return left;
                }
            }

            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

//            @Override
//            public int getViewVerticalDragRange(View child) {
//                return 0;
//            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return width;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                if (xvel > 0) {
                    open();
                } else if (xvel < 0) {
                    close();
                } else if (releasedChild == rightLayout && splitLine > range * 0.3) {
                    open();
                } else if (releasedChild == leftLayout && splitLine > range * 0.7) {
                    open();
                } else {
                    close();
                }
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                if (changedView == rightLayout) {
                    splitLine = left;
                } else {
                    splitLine = splitLine + left;
                }
                if (splitLine < 0) {
                    splitLine = 0;
                } else if (splitLine > range) {
                    splitLine = range;
                }

                if (changedView == leftLayout) {
                    requestLayout();
                }

                dispatchDragEvent(splitLine);
            }
        });
    }

    private void dispatchDragEvent(int splitLine) {
        if (mDragListener == null) {
            return;
        }
        float percent = splitLine / (float) range;
        animateView(percent);
        mDragListener.onDrag(percent);
        Status lastStatus = mStatus;
        if (lastStatus != getStatus() && mStatus == Status.Close) {
            mDragListener.onClose();;
        } else if (lastStatus != getStatus() && mStatus == Status.Open) {
            mDragListener.onOpen();
        }
    }

    private void animateView(float percent) {
        float f1 = 1 - percent * 0.3f;
        ViewHelper.setScaleX(rightLayout, f1);
        ViewHelper.setScaleY(rightLayout, f1);
        ViewHelper.setTranslationX(leftLayout, -leftLayout.getWidth() / 2.3f + leftLayout.getWidth() / 2.3f * percent);
        ViewHelper.setScaleX(leftLayout, 0.5f + 0.5f * percent);
        ViewHelper.setScaleY(leftLayout, 0.5f + 0.5f * percent);
        ViewHelper.setAlpha(leftLayout, percent);
        getBackground().setColorFilter(evaluate(percent, Color.BLACK, Color.TRANSPARENT), PorterDuff.Mode.SRC_OVER);
    }

    private Integer evaluate(float fraction, Object startValue, Integer endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;
        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;
        return (int) ((startA + (int) (fraction * (endA - startA))) << 24)
                | (int) ((startR + (int) (fraction * (endR - startR))) << 16)
                | (int) ((startG + (int) (fraction * (endG - startG))) << 8)
                | (int) ((startB + (int) (fraction * (endB - startB))));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        leftLayout = (RelativeLayout) getChildAt(0);
        rightLayout = (MyRelativeLayout) getChildAt(1);
        rightLayout.setBesideDragLayout(this);
        leftLayout.setClickable(true);
        rightLayout.setClickable(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = leftLayout.getMeasuredWidth();
        height = leftLayout.getMeasuredHeight();
        range = (int) (width * 0.6);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        leftLayout.layout(0, 0, width, height);
        rightLayout.layout(splitLine, 0, splitLine + rightLayout.getMeasuredWidth(), rightLayout.getMeasuredHeight());
//        rightLayout.layout(splitLine, 0, splitLine + width, height);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_CANCEL || ev.getAction() == MotionEvent.ACTION_UP) {
//            isYScrolled = false;
//        }
        boolean shouldIntercept = mDragerHelper.shouldInterceptTouchEvent(ev);
        boolean ygx = mGestureDetectorCompat.onTouchEvent(ev);
        Log.e(Const.TAG, "shouldIntercept = " + shouldIntercept + " ygx = " + ygx + " isYScrolled = " + isYScrolled);
        return mDragerHelper.shouldInterceptTouchEvent(ev) && !ygx; // && !isYScrolled;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragerHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (mDragerHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void open() {
        open(true);
    }

    public void open(boolean animate) {
        if (animate) {
            if (mDragerHelper.smoothSlideViewTo(rightLayout, range, 0)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            splitLine = range;
            requestLayout();
        }
    }

    public void close() {
        close(true);
    }

    public void close(boolean animate) {
        if (animate) {
            if (mDragerHelper.smoothSlideViewTo(rightLayout, 0, 0)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            splitLine = 0;
            requestLayout();
        }
    }
}
