package com.passerbywhu.study.dragstudy;

import android.animation.ValueAnimator;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuwenchao3 on 2015/5/13.
 */
public class SimpleDragLayout extends ViewGroup {
    private static final String TAG = "SIMPLEDRAGLAYOUT";
    private List<Button> children;
    private Toast toast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);

    private int mDragRange;

    private Button btn1;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        children = new ArrayList<Button>();
        for (int i = 0; i < childCount; i++) {
            children.add((Button) getChildAt(i));
            final int which = i + 1;
            children.get(i).setClickable(false);
            children.get(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    toast.setText(which + " button clicked");
                    toast.show();
                }
            });
        }
        btn1 = (Button) findViewById(R.id.btn1);
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void restore() {
        int height = 0;
//        LayoutParams params = getLayoutParams();
//        if (getParent() instanceof View) {
//            params.height = ((View) getParent()).getHeight();
//            setLayoutParams(params);
//        } else {
//            params.height = getContext().getResources().getDisplayMetrics().heightPixels;
//            setLayoutParams(params);
//        }
        if (getParent() instanceof View) {
            height = ((View) getParent()).getHeight();
        } else {
            height = getContext().getResources().getDisplayMetrics().heightPixels;
        }
        ValueAnimator valueAnimator = ValueAnimator.ofInt(getHeight(), height);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                LayoutParams params = getLayoutParams();
                params.height = (int) animation.getAnimatedValue();
                setLayoutParams(params);
                invalidate();
            }
        });
        valueAnimator.start();
    }

    private class MyDragHelperCallback extends ViewDragHelper.Callback {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            Log.e(TAG, "view PositionChanged");
//            Log.e(TAG, "left = " + left + " top = " + top + " dx = " + dx + " dy = " + dy);
            if (changedView != btn1) {
                changedView.setLeft(left);
                changedView.setTop(top);
            } else {
                Log.e(TAG, "left = " + left + " top = " + top + " dx = " + dx + " dy = " + dy);
//                changedView.setLeft(left);
//                changedView.setTop(top);
//                Log.e(TAG, "btn1 top = " + top + " getHeight = " + getHeight());
//                setPivotX((getRight() - getLeft()) / 2);
//                setPivotY((getBottom() - getTop()) / 2);
//                setScaleY(1.0f - (float)top / (float)getHeight());
                LayoutParams params = getLayoutParams();
                params.height = getHeight() - top;
                setLayoutParams(params);

                requestLayout();
            }
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            super.onEdgeDragStarted(edgeFlags, pointerId);
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return mDragRange;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return mDragRange;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
//            if (child == btn1) {
//                return 0;
//            }
//            Log.e(TAG, "clampViewHorizontal");
//            Log.e(TAG, "left = " + left + " dx = " + dx);
            int leftBound = getPaddingLeft();
            int rightBound = getWidth() - getPaddingRight() - child.getWidth();
            return Math.min(Math.max(left, leftBound), rightBound);
//            return left;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
//            if (child == btn1) {
//                return 0;
//            }
//            Log.e(TAG, "clampViewVertical");
//            Log.e(TAG, "top = " + top + " dy = " + dy);
            if (child != btn1) {
                int topBound = getPaddingTop();
                int bottomBound = getHeight() - getPaddingBottom() - child.getHeight();
                return Math.min(Math.max(top, topBound), bottomBound);
            } else {
                return top;
            }
        }
    }

    private ViewDragHelper mDragHelper;

    public SimpleDragLayout(Context context) {
        this(context, null);
    }

    public SimpleDragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleDragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDragHelper = ViewDragHelper.create(this, 1.0f, new MyDragHelperCallback());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean ifIntercept = mDragHelper.shouldInterceptTouchEvent(ev);
        Log.e(TAG, "ifIntercept = " + ifIntercept);
        return ifIntercept;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, 0), resolveSizeAndState(maxHeight, heightMeasureSpec, 0));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mDragRange = b - t;
        for (Button button : children) {
            if (button != btn1) {
                button.layout(button.getLeft(), button.getTop(), button.getLeft() + button.getMeasuredWidth(), button.getTop() + button.getMeasuredHeight());
            } else {
                int left = getMeasuredWidth() / 2 - button.getMeasuredWidth() / 2;
                int right = left + button.getMeasuredWidth();
                button.layout(left, 0, right, button.getMeasuredHeight());
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "simpleDragLayout onTouchEvent");
        mDragHelper.processTouchEvent(event);
        return true;
    }
}
