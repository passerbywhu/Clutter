package com.passerbywhu.study.customviewstudy;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.passerbywhu.study.R;

public class CustomLayout extends ViewGroup {
	private int mLeftWidth;
	private int mRightWidth;
	
	private final Rect mTmpContainerRect = new Rect();
	private final Rect mTmpChildRect = new Rect();
	
	public CustomLayout(Context context) {
		super(context);
	}
	
	public CustomLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public CustomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	@Override
	public boolean shouldDelayChildPressedState() {
		return false;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int count = getChildCount();
		
		mLeftWidth = 0;
		mRightWidth = 0;
		
		int maxHeight = 0;
		int maxWidth = 0;
		int childState = 0;
		
		for (int i = 0; i < count; i ++) {
			final View child = getChildAt(i);
			if (child.getVisibility() != GONE) {
				measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
				
				final LayoutParams lp = (LayoutParams) child.getLayoutParams();
				if (lp.position == LayoutParams.POSITION_LEFT) {
					mLeftWidth += Math.max(maxWidth, 
							child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
				} else if (lp.position == LayoutParams.POSITION_RIGHT) {
					mRightWidth += Math.max(maxWidth, 
							child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
				} else {
					maxWidth = Math.max(maxWidth, 
							child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
				}
				maxHeight = Math.max(maxHeight, 
						child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
				childState = combineMeasuredStates(childState, child.getMeasuredState());
			}
		}
		
		maxWidth += mLeftWidth + mRightWidth;
		maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
		maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
		
		setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState), 
				resolveSizeAndState(maxHeight, heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT));
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int count = getChildCount();
		
		int leftPos = getPaddingLeft();
		int rightPos = r - l - getPaddingRight();
		
		final int middleLeft = leftPos + mLeftWidth;
		final int middleRight = rightPos - mRightWidth;
		
		final int parentTop = getPaddingTop();
		final int parentBottom = b - t - getPaddingBottom();
		
		for (int i = 0; i < count; i ++) {
			final View child = getChildAt(i);
			if (child.getVisibility() != GONE) {
				final LayoutParams lp = (LayoutParams) child.getLayoutParams();
				final int width = child.getMeasuredWidth();
				final int height = child.getMeasuredHeight();
				
				if (lp.position == LayoutParams.POSITION_LEFT) {
					mTmpContainerRect.left = leftPos + lp.leftMargin;
					mTmpContainerRect.right = leftPos + width + lp.rightMargin;
					leftPos = mTmpContainerRect.right;
				} else if (lp.position == LayoutParams.POSITION_RIGHT) {
					mTmpContainerRect.right = rightPos - lp.rightMargin;
					mTmpContainerRect.left = rightPos - width - lp.leftMargin;
					rightPos = mTmpContainerRect.left;
				} else {
					mTmpContainerRect.left = middleLeft + lp.leftMargin;
					mTmpContainerRect.right = middleRight - lp.rightMargin;
				}
				mTmpContainerRect.top = parentTop + lp.topMargin;
				mTmpContainerRect.bottom = parentBottom - lp.bottomMargin;
				
				Gravity.apply(lp.gravity, width, height, mTmpContainerRect, mTmpChildRect);
				
				child.layout(mTmpChildRect.left, mTmpChildRect.top, mTmpChildRect.right, mTmpChildRect.bottom);
			}
		}
	}
	
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new LayoutParams(getContext(), attrs);
	}
	
	protected LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	}
	
	protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
		return new LayoutParams(p);
	}
	
	protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
		return p instanceof LayoutParams;
	}
	
	public static class LayoutParams extends MarginLayoutParams {
		public int gravity = Gravity.TOP | Gravity.START;
		public static int POSITION_MIDDLE = 0;
		public static int POSITION_LEFT = 1;
		public static int POSITION_RIGHT = 2;
		
		public int position = POSITION_MIDDLE;
		
		public LayoutParams(Context c, AttributeSet attrs) {
			super(c, attrs);
			TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.CustomLayoutLP);
			gravity = a.getInt(R.styleable.CustomLayoutLP_android_layout_gravity, gravity);
			position = a.getInt(R.styleable.CustomLayoutLP_layout_position, position);
			a.recycle();
		}
		
		public LayoutParams(int width, int height) {
			super(width, height);
		}
		
		public LayoutParams(ViewGroup.LayoutParams source) {
			super(source);
		}
	}
}
