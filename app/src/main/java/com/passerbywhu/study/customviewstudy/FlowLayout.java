package com.passerbywhu.study.customviewstudy;

import android.content.Context;
import android.text.style.LineHeightSpan.WithDensity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

//不支持child MatchParent版本
//下一步的任务是写一个支持MatchParent的版本。自动占用满剩下的宽度。
public class FlowLayout extends ViewGroup {
	private static final String TAG = "FLOWLAYOUT";

	public FlowLayout(Context context) {
		super(context);
	}
	
	public FlowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		
		switch(widthMode) {
		case MeasureSpec.AT_MOST:
			Log.w("widthMode", "AT_MOST");
			break;
		case MeasureSpec.EXACTLY:
			Log.w("widthMode", "EXACTLY");
			break;
		case MeasureSpec.UNSPECIFIED:
			Log.w("widthMode", "UNSPECIFIED");
			break;
		}
		
		switch(heightMode) {
		case MeasureSpec.AT_MOST:
			Log.w("heightMode", "AT_MOST");
			break;
		case MeasureSpec.EXACTLY:
			Log.w("heightMode", "EXACTLY");
			break;
		case MeasureSpec.UNSPECIFIED:
			Log.w("heightMode", "UNSPECIFIED");
			break;
		}
		Log.w(TAG, "width = " + width + " height = " + height);
		
		int childState = 0;
		int childCount = getChildCount();
		
		int needHeight = 0;
		int rowMaxHeight = 0;
		int mLeftWidth = 0;
		
		for (int i = 0; i < childCount; i ++) {
			View child = getChildAt(i);
			measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
			final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
			childState = combineMeasuredStates(childState, child.getMeasuredState());
			Log.w(TAG, "childWidth = " + child.getMeasuredWidth() + " childHeight = " + child.getMeasuredHeight());
			if (mLeftWidth + child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin <= width) {
				mLeftWidth += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
				rowMaxHeight = Math.max(rowMaxHeight, child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
				if (i == childCount - 1) {
					needHeight += rowMaxHeight;
				}
			} else {
				mLeftWidth = 0;
				mLeftWidth += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
				needHeight += rowMaxHeight;
				rowMaxHeight = 0;
				rowMaxHeight = Math.max(rowMaxHeight, child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
				if (i == childCount - 1) {
					needHeight += rowMaxHeight;
				}
			}
		}
		
		Log.w(TAG, "needHeight = " + needHeight);
		needHeight = Math.max(needHeight, getSuggestedMinimumHeight());
		setMeasuredDimension(resolveSizeAndState(width, widthMeasureSpec, childState), resolveSizeAndState(needHeight, heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childCount = getChildCount();
		int mWidth = getMeasuredWidth();
		int mHeight = getMeasuredHeight();
		int startX = 0, startY = 0;
		int rowMaxHeight = 0;
		for (int i = 0; i < childCount; i ++) {
			View child = getChildAt(i);
			int cWidth = child.getMeasuredWidth();
			int cHeight = child.getMeasuredHeight();
			MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
			if (startX + cWidth + lp.leftMargin + lp.rightMargin <= mWidth && startY + cHeight + lp.topMargin + lp.bottomMargin <= mHeight) {
				child.layout(startX + lp.leftMargin, startY + lp.topMargin, startX + cWidth + lp.leftMargin, startY + cHeight + lp.topMargin);
				startX += cWidth + lp.leftMargin + lp.rightMargin;
				if (cHeight + lp.topMargin + lp.bottomMargin > rowMaxHeight) {
					rowMaxHeight = cHeight;
				}
			} else if (startX + cWidth + lp.leftMargin + lp.rightMargin > mWidth && startY + rowMaxHeight + cHeight + lp.topMargin + lp.bottomMargin <= mHeight) {
				startX = 0;
				startY = startY + rowMaxHeight;
				rowMaxHeight = 0;
				child.layout(startX + lp.leftMargin, startY + lp.topMargin, startX + cWidth + lp.leftMargin, startY + cHeight + lp.topMargin);
				startX += cWidth + lp.leftMargin + lp.rightMargin;
				if (cHeight > rowMaxHeight) {
					rowMaxHeight = cHeight;
				}
			} else {
				continue;
			}
		}
	}
	
	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new MarginLayoutParams(getContext(), attrs);
	}
	
	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}
	
	@Override
	protected LayoutParams generateLayoutParams(LayoutParams p) {
		return new MarginLayoutParams(p);
	}
	
	@Override
	protected boolean checkLayoutParams(LayoutParams p) {
		return p instanceof MarginLayoutParams;
	}
}
