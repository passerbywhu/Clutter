package com.passerbywhu.study.dragstudy;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.passerbywhu.study.R;

/**
 * Created by wuwenchao3 on 2015/5/13.
 */
public class DragLayout extends ViewGroup {
    private ViewDragHelper mViewDragHelper;

    private Button mHeaderButton;
//    private View mDescView;

    private int mDragRange;
    private int mTop;
    private int mLeft;
    private float mDragOffset;
    private View dragBg;

    private int initialWidth;
    private int initialHeight;

    private boolean isMeasureComplete = false;

    private static final String TAG = "DRAGLAYOUT";

    public DragLayout(Context context) {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallback());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mHeaderButton = (Button) findViewById(R.id.headerBtn);
        mHeaderButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "HeaderBtn clicked", Toast.LENGTH_SHORT).show();
//                if (mDragOffset < 0.5) {
//                    smoothSlideTo(1);
//                } else {
//                    smoothSlideTo(0);
//                }
            }
        });
        dragBg = findViewById(R.id.dragBg);
        dragBg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "dragBg clicked", Toast.LENGTH_SHORT).show();
            }
        });
//        mDescView = findViewById(R.id.desc);
    }

    public void maximize() {
        smoothSlideTo(0f);
    }

    public void minimize() {  //怎么感觉1是topBound + mDragRange是maxMize。。。
        smoothSlideTo(1f);
    }

    boolean smoothSlideTo(float slideOffset) {
        final int topBound = getPaddingTop();
        int y = (int) (topBound + slideOffset * mDragRange);

        if (mViewDragHelper.smoothSlideViewTo(mHeaderButton, mHeaderButton.getLeft(), y)) {
            ViewCompat.postInvalidateOnAnimation(this);
            return true;
        }
        return false;
    }

    private class DragHelperCallback extends ViewDragHelper.Callback {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mHeaderButton;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            Log.e(TAG, "viewPositionChanged");
            mTop = top;
            mLeft = left;

            Log.e(TAG, "top = " + top);

            mDragOffset = (float) top / mDragRange;

//            LayoutParams params = mHeaderButton.getLayoutParams();
//            params.width = (int) (initialWidth * (1 - mDragOffset / 2));
//            params.height = (int) (initialHeight * (1 - mDragOffset / 2));
//            mHeaderButton.setLayoutParams(params);

//            mHeaderButton.animate().scaleX(1 - mDragOffset / 2).scaleY(1 - mDragOffset / 2).start();

//            mHeaderButton.setPivotX(mHeaderButton.getWidth());
//            mHeaderButton.setPivotY(mHeaderButton.getHeight());
//            mHeaderButton.setScaleX(1 - mDragOffset / 2);
//            mHeaderButton.setScaleY(1 - mDragOffset / 2);
//            mDescView.setAlpha(1 - mDragOffset);
//            requestLayout();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
//            int top = getPaddingTop();
//            if (yvel > 0 || (yvel == 0 && mDragOffset > 0.5f)) {
//                top += mDragRange;
//            }
//            mViewDragHelper.settleCapturedViewAt(releasedChild.getLeft(), top);
//            invalidate();
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return mDragRange;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            final int topBound = getPaddingTop();
            final int bottomBound = getHeight() - mHeaderButton.getHeight() - mHeaderButton.getPaddingBottom();

            final int newTop = Math.min(Math.max(top, topBound), bottomBound);
            return newTop;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return mDragRange;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            final int leftBound = getPaddingLeft();
            final int rightBound = getWidth() - mHeaderButton.getWidth() - mHeaderButton.getPaddingRight();

            final int newLeft = Math.min(Math.max(leftBound, left), rightBound);
            return newLeft;
        }
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean ifIntercept = mViewDragHelper.shouldInterceptTouchEvent(ev);
        Log.e(TAG, "if Intercept = " + ifIntercept);
        return ifIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "Draglayout TouchEvent");
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(
              resolveSizeAndState(maxWidth, widthMeasureSpec, 0),
              resolveSizeAndState(maxHeight, heightMeasureSpec, 0)
        );
        if (!isMeasureComplete && mHeaderButton.getMeasuredWidth() != 0) {
            isMeasureComplete = true;
            initialWidth = mHeaderButton.getMeasuredWidth();
            initialHeight = mHeaderButton.getMeasuredHeight();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        dragBg.layout(l, t, r, b);
        mDragRange = getHeight() - mHeaderButton.getHeight();
        mHeaderButton.layout(
                mLeft,
                mTop,
                mLeft + mHeaderButton.getMeasuredWidth(),
                mTop + mHeaderButton.getMeasuredHeight());

//        mDescView.layout(
//                0,
//                mTop + mHeaderButton.getMeasuredHeight(),
//                r,
//                mTop  + b);
    }
}
