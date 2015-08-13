package com.passerbywhu.study.touchstudy.dragstudy.dragsort;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.Region;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.passerbywhu.study.common.adapter.AdapterBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuwenchao3 on 2015/7/6.
 */
public class DragSortGridView extends GridView {
    private ViewDragHelper mViewDragHelper;
    private boolean beginSort = false;
    private static final int shrinkValue = 30;
    private static final int animDuration = 200;
//    private int childWidth, childHeight;
    private static final String TAG = "DRAGSORT";
    private int curDragposition = -1;
    private int topLayerIndex = -1;
    private View dragView;
    private Rect lastSpacePosition;
    private List dataListCopy;  //副本，用来临时保存顺序，在放手之后才真正更新数据
    private List<Rect> childPos = new ArrayList<Rect>();
    private List<View> children = new ArrayList<View>();  //用来在交换的过程中来保存顺序
    private boolean moveAnimStarted;
    private boolean delayEndCall;

    public DragSortGridView(Context context) {
        this(context, null);
    }

    public DragSortGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragSortGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void setChildrenDrawingOrderEnabled(boolean enabled) {
        super.setChildrenDrawingOrderEnabled(enabled);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        curDragposition = getChildCount() - 1;
    }

    private void printPos(View view) {
        if (view != null) {
            Log.e(TAG, "left =" + view.getLeft() + " top = " + view.getTop());
        }
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (topLayerIndex != -1) {
            if (i == topLayerIndex) {
                return childCount - 1;
            }
            if (i == childCount - 1) {
                return topLayerIndex;
            }
            return i;
        } else {
            if (i == 0) {
                return childCount - 1;
            }
            if (i == childCount - 1) {
                return 0;
            }
            return i;
        }
    }

    private void collisionDetect() {
        if (dragView == null) {
            return;
        }
        if (moveAnimStarted) {
            return;
        }
        int width = dragView.getWidth();
        int height = dragView.getHeight();
        float area = width * height;
        Rect rect_drag = new Rect(dragView.getLeft(), dragView.getTop(), dragView.getRight(), dragView.getBottom());
        int intersectIndex = -1;
        for (int i = 0; i < childPos.size(); i ++) {
            if (i == curDragposition - getFirstVisiblePosition()) continue;
            Rect rect = childPos.get(i);
            Region region = new Region(rect);
            Region region_drag = new Region(rect_drag);
            boolean intersected = region_drag.op(region, Region.Op.INTERSECT);
            if (intersected) {
                Rect intersect = region_drag.getBounds();
                float area_intersect = (intersect.right - intersect.left) * (intersect.bottom - intersect.top);
                if (area_intersect > area / 3) {  //相交
                    intersectIndex = i;
                    break;
                }
            }
        }
        if (intersectIndex != -1) {  //交换
            moveAnimStarted = true;
            View child = children.get(intersectIndex);
            Rect temp = new Rect(childPos.get(intersectIndex));
            int desLeft = lastSpacePosition.left;
            int desTop = lastSpacePosition.top;
            ViewPropertyAnimator animator = child.animate().x(desLeft).y(desTop);
            final int index = intersectIndex;
            animator.setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    moveAnimStarted = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    Object a = dataListCopy.get(curDragposition);
                    Object b  = dataListCopy.get(index + getFirstVisiblePosition());
                    dataListCopy.set(curDragposition, b);
                    dataListCopy.set(index + getFirstVisiblePosition(), a);
                    View ca = children.get(curDragposition - getFirstVisiblePosition());
                    View cb = children.get(index);
                    children.set(curDragposition - getFirstVisiblePosition(), cb);
                    children.set(index, ca);
                    curDragposition = index + getFirstVisiblePosition();
                    moveAnimStarted = false;
                    if (delayEndCall) {
                        delayEndCall = false;
                        dragSortEnd();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.start();
//            Log.e(TAG, "left from " + child.getLeft() + " to " + lastSpacePosition.left);
//            Log.e(TAG, "top from " + child.getTop() + " to " + lastSpacePosition.top);
            lastSpacePosition = temp;
        }
    }

    public void printChildren() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getChildCount(); i ++) {
            TextView child = (TextView) getChildAt(i);
            sb.append(child.getText() + " [" + child.getX() / child.getWidth() + " " + child.getY() / child.getHeight() + " ]  ");
        }
        sb.append("\n");
        for (int i = 0; i < getChildCount(); i ++) {
            TextView child = (TextView) getChildAt(i);
            sb.append(child.getText() + " [" + child.getLeft() / child.getWidth() + " " + child.getTop() / child.getHeight() + " ]  ");
        }
        sb.append("\n");
        sb.append("-------------------------------------------------------------\n");
        Log.e(TAG, "children sort" + sb.toString());
    }

    public void init() {
        setChildrenDrawingOrderEnabled(true);
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
//                if (!children.isEmpty()) {
//                    if (child == children.get(0)) {
//                        Log.e(TAG, "captured View " + ((TextView) child).getText());
//                        printPos(child);
//                        return true;
//                    }
//                }
//                return false;
                dragView = child;
                lastSpacePosition = new Rect(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
                Log.e(TAG, "captureView lastSpacePosition = " + lastSpacePosition);
                return true;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                dragView.setX(lastSpacePosition.left);
                dragView.setY(lastSpacePosition.top);
                printChildren();
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                collisionDetect();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return getHeight();
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return getWidth();
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return top;
            }
        });
        setOnItemLongClickListener(mOnItemLongClickListener);
    }

    private OnItemLongClickListener mOnItemLongClickListener = new OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            setOnItemLongClickListener(null);
            beginSort = true;
            curDragposition = position;
            topLayerIndex = position - getFirstVisiblePosition();
            dragView = view;
            dataListCopy = new ArrayList(((AdapterBase) getAdapter()).getList());
            int count = getChildCount();
            for (int i = 0; i < count; i ++) {
                View child = getChildAt(i);
                childPos.add(new Rect(child.getLeft(), child.getTop(), child.getRight(), child.getBottom()));
                children.add(child);
            }
            shrinkAnimate();
            return true;
        }
    };

    private void restoreChildPosition() {
        Log.e(TAG, "restore begin");
        printChildren();
        int count = getChildCount();
        for (int i = 0; i < count; i ++) {
            View view = getChildAt(i);
            Rect pos = childPos.get(i);
            view.setLeft(pos.left);
            view.setTop(pos.top);
            view.setRight(pos.right);
            view.setBottom(pos.bottom);
            view.setX(pos.left);
            view.setY(pos.top);
            Log.e(TAG, ((TextView) view).getText() + " " + view.getLeft() / view.getWidth() + " " + view.getTop() / view.getHeight());
            Log.e(TAG, "post = " + pos.left / view.getWidth() + " " + pos.top / view.getHeight());
        }
        printChildren();
        Log.e(TAG, "restore end");
    }

    private void printCopyList() {
        StringBuffer sb = new StringBuffer();
        for (Object s : dataListCopy) {
            sb.append(s + " ");
        }
        Log.e(TAG, "dragEnd List = " + sb.toString());
    }

    private void dragSortEnd() {
        if (moveAnimStarted) {
            delayEndCall = true;
            return;
        }
        printCopyList();
        restoreChildPosition();
        invalidate();
        requestLayout();
        Log.e(TAG, "after requestLayout");
        printChildren();
        ((AdapterBase) getAdapter()).setList(dataListCopy);
        childPos.clear();
        children.clear();
        setOnItemLongClickListener(mOnItemLongClickListener);
        beginSort = false;
        curDragposition = -1;
        topLayerIndex = -1;
        dragView = null;
    }

    private void restoreAnimate() {
        AnimatorSet animatorSet = new AnimatorSet();
        ArrayList<Animator> animations = new ArrayList<Animator>();
        for (int i = 0; i < getChildCount(); i ++) {
            if (topLayerIndex == i) {
                continue;
            }
            View child = getChildAt(i);
            DragSortAdapter.ViewHolder viewHolder = (DragSortAdapter.ViewHolder) child.getTag();
//            viewHolder.textView.setPivotX(1.0f);
//            viewHolder.textView.setPivotX(1.0f);
            ObjectAnimator oaX = ObjectAnimator.ofFloat(viewHolder.textView, "scaleX", 0.8f, 1.0f);
            ObjectAnimator oaY = ObjectAnimator.ofFloat(viewHolder.textView, "scaleY", 0.8f, 1.0f);
            animations.add(oaX);
            animations.add(oaY);
        }
        animatorSet.setDuration(animDuration);
        animatorSet.playTogether(animations);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                dragSortEnd();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    private void shrinkAnimate() {
        AnimatorSet animatorSet = new AnimatorSet();
        ArrayList<Animator> animations = new ArrayList<Animator>();
        for (int i = 0; i < getChildCount(); i ++) {
            if (i == topLayerIndex) {
                continue;
            }
            View child = getChildAt(i);
            DragSortAdapter.ViewHolder viewHolder = (DragSortAdapter.ViewHolder) child.getTag();
//            viewHolder.textView.setPivotX(1.0f);
//            viewHolder.textView.setPivotX(1.0f);
            ObjectAnimator oaX = ObjectAnimator.ofFloat(viewHolder.textView, "scaleX", 1f, 0.8f);
            ObjectAnimator oaY = ObjectAnimator.ofFloat(viewHolder.textView, "scaleY", 1f, 0.8f);
            animations.add(oaX);
            animations.add(oaY);
        }
        animatorSet.setDuration(animDuration);
        animatorSet.playTogether(animations);
        animatorSet.start();

        //看起来这种实现方式不怎么好
//        ValueAnimator shrinkAnimator = ValueAnimator.ofInt(0, shrinkValue);
//        shrinkAnimator.setDuration(animDuration);
//        shrinkAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                for (View child : children) {
//                    int value = (int) animation.getAnimatedValue();
//                    GridView.LayoutParams params = (LayoutParams) child.getLayoutParams();
//                    params.width = childWidth - value;
//                    params.height = childHeight - value;
//                    child.setLayoutParams(params);
//                }
//            }
//        });
//        shrinkAnimator.start();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean shouldIntercept = mViewDragHelper.shouldInterceptTouchEvent(ev);
        if (beginSort) {
            return shouldIntercept;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (beginSort) {
            mViewDragHelper.processTouchEvent(ev);
            int action = MotionEventCompat.getActionMasked(ev);
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                restoreAnimate();
            }
            return true;
        } else {
            return super.onTouchEvent(ev);
        }
    }
}
