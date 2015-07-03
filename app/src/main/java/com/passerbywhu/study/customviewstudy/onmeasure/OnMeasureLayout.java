package com.passerbywhu.study.customviewstudy.onmeasure;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.passerbywhu.study.R;

import java.util.ArrayList;

import static com.passerbywhu.study.customviewstudy.onmeasure.MeasureUtils.getLayoutParamName;
import static  com.passerbywhu.study.customviewstudy.onmeasure.MeasureUtils.getModeName;

/**
 * Created by wuwenchao3 on 2015/7/1.
 */
public class OnMeasureLayout extends ViewGroup {
    public String getMeasureInfo() {
        StringBuilder tempStr = new StringBuilder();
        tempStr.append(sb.toString());
        tempStr.append("getWidth = " + getWidth() + "\n");
        tempStr.append("getHeight = " + getHeight() + "\n\n");

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i ++) {
            View child = getChildAt(i);
            if (child instanceof OnMeasureLayout) {
                tempStr.append(((OnMeasureLayout) child).getMeasureInfo() + "\n");
            }
        }
        return tempStr.toString();
    }
    protected StringBuilder sb = new StringBuilder();

    public String getName() {
        return name;
    }

    private String name;
    private final ArrayList<View> mMatchParentChildren = new ArrayList<View>(1);
    private static String TAG = "OnMeasureLayout";

    public OnMeasureLayout(Context context) {
        super(context);
    }

    public OnMeasureLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OnMeasureLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.OnMeasureLayout, defStyleAttr, 0);
        name = a.getString(R.styleable.OnMeasureLayout_layout_name);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        sb = new StringBuilder();
        sb.append("name = " + name + "\n");
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        sb.append("width = " + width + "\n");
        sb.append("widthMode = " + getModeName(widthMode) + "\n");
        sb.append("height = " + height + "\n");
        sb.append("heightMode = " + getModeName(heightMode) + "\n");
        sb.append("layoutParamWidth = " + getLayoutParamName(getLayoutParams().width) + "\n");
        sb.append("layoutParamHeight = " + getLayoutParamName(getLayoutParams().height) + "\n");

        int childState = 0;

        int count = getChildCount();

//        sb.append("childCount = " + count + "\n");   //我们这里只演示为1的情况

        int maxWidth = 0, maxHeight = 0;

        final boolean measureMatchParentChildren =
                widthMode != MeasureSpec.EXACTLY || heightMode != MeasureSpec.EXACTLY;
        mMatchParentChildren.clear();

        for (int i = 0; i < count; i ++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
//                sb.append("child " + i + "\n");
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                final MarginLayoutParams param = (MarginLayoutParams) child.getLayoutParams();
                int measuredWidth = child.getMeasuredWidth();
                int measuredHeight = child.getMeasuredHeight();
                maxWidth = Math.max(maxWidth, measuredWidth + param.leftMargin + param.rightMargin);
                maxHeight = Math.max(maxHeight, measuredHeight + param.topMargin + param.bottomMargin);
                childState = combineMeasuredStates(childState, child.getMeasuredState());
                if (measureMatchParentChildren) {
                    if (param.width == LayoutParams.MATCH_PARENT || param.height == LayoutParams.MATCH_PARENT) {
                        mMatchParentChildren.add(child);
                    }
                }
            }
        }
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT));

        Log.d(TAG, sb.toString());
        count = mMatchParentChildren.size();
        if (count > 1) {
            for (int i = 0; i < count; i ++) {
                final View child = mMatchParentChildren.get(i);

                final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int childWidthMeasureSpec;
                int childHeightMeasureSpec;

                if (lp.width == LayoutParams.MATCH_PARENT) {
                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth() - lp.leftMargin - lp.rightMargin, MeasureSpec.EXACTLY);
                } else {
                    childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, lp.leftMargin + lp.rightMargin, lp.width);
                }

                if (lp.height == LayoutParams.MATCH_PARENT) {
                    childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight() - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY);
                } else {
                    childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, lp.topMargin + lp.bottomMargin, lp.height);
                }
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }
        sb.append("measuredWidth = " + maxWidth + "\n");
        sb.append("measuredHeight = " + maxHeight + "\n");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        Rect container = new Rect();
        Rect result = new Rect();
        for (int i = 0; i < count; i ++) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                MarginLayoutParams param = (MarginLayoutParams) child.getLayoutParams();
                int left = l + param.leftMargin;
                int right = r - param.rightMargin;
                int top = t + param.topMargin;
                int bottom = b - param.bottomMargin;
                container.left = left;
                container.top = top;
                container.right = right;
                container.bottom = bottom;

                int measuredWidth = child.getMeasuredWidth();
                int measuredHeight = child.getMeasuredHeight();

                int parentWidth = right - left;
                int parentHeight = bottom - top;

                int posX = parentWidth / 2 - measuredWidth / 2;
                int posY = parentHeight / 2 - measuredHeight / 2;

                child.layout(posX, posY, posX + measuredWidth, posY + measuredHeight);


//                Gravity.apply(Gravity.CENTER, child.getMeasuredWidth(), child.getMeasuredHeight(), container, result);
//                child.layout(result.left, result.top, result.right, result.bottom);
            }
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }
}
