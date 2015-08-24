package com.passerbywhu.study.touchstudy.pulltozoom;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.passerbywhu.study.R;

/**
 * Created by wuwenchao3 on 2015/4/3.
 */
public class AnimatePullToZoomInterceptLayout extends RelativeLayout {
    private ImageView mHeaderView;
    private ListView mListView;
    private static final String TAG = "INTERCEPT_LAYOUT";
    private int mFirstHeight;  //第一个Height，也即原始Height，超过这个Height都需要被拦截，处于缩放状态
    private int mSecondHeight; //第二个height，即上推与下推的分界线。
    private int mMaxHeight;
    private boolean isAnimating = false;
    private Context mContext;
    private int eventX, eventY;
    private int preAction;
    private int prePhase;
    
    private static final int PHASE_ZERO = 0;
    private static final int PHASE_FIRST = 1;
    private static final int PHASE_SECOND = 2;
//    private Integer pointerId = null;  //后续屏蔽多指操作
    
    private void log(String str) {
    	if (!TextUtils.isEmpty(TAG)) {
    		Log.e(TAG, str);
    	}
    }
    
    public interface OnAnimationListener {
    	/**
    	 * 由Second Height到First Height
    	 * @param fraction
    	 * @param distance 仍然是为了防止move过大的情况。有可能在图片拉得很大的情况下猛然往上一滑，导致高度还没复原就直接进入了PHASE_FIRST。
    	 * 从而导致title 复位了。因此这里仍然要判断高度。此处distance是mHeaderView.height - mSecondHeight;
    	 * 当height < mSceondHeight的时候title保持原margin，当height > mSecondHeight的时候仍然按高度算。
    	 */
    	public void onFirstUpAnimation(float fraction, int distance);
    	/**
    	 * 由First Height到Second Height
    	 * @param fraction
    	 */
    	public void onFirstDownAnimation(float fraction, int distance);
    	
    	/**
    	 * 
    	 * @param distance 当前高度与mSecondHeight的差值。之所以用一个总的高度差而不是每次move的距离，是因为在极迅速的滑动情况下，
    	 * 会出现move的speedY达到600多的情况。此时mHeaderView的LayoutParams的height设置为这个值，但是requestLayout并没有到这个值。
    	 * 而Title的margin根据speedY来计算，从而出现了title比原始位置下移的情况。因此改为根据实际高度来算。
    	 * 
    	 * move时speedY过大同样会导致直接从PHASE_INITIAL跨到PHASE_SECOND的情况，因此需要限制。
    	 * 
    	 */
    	public void onSecondUpAnimation(int distance);
    	
    	/**
    	 * 
    	 * @param distance 同onSecondUpAnimation
    	 */
    	public void onSecondDownAnimation(int distance);
    }
    
    private OnAnimationListener mOnAnimationListener;
    
    public void setOnAnimationListener(OnAnimationListener onAnimationListener) {
    	this.mOnAnimationListener = onAnimationListener;
    }
    
    public AnimatePullToZoomInterceptLayout(Context context) {
        this(context, null);
    }

    public AnimatePullToZoomInterceptLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimatePullToZoomInterceptLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }
    
    public void init() {
    	prePhase = PHASE_FIRST; 
    	mFirstHeight = mContext.getResources().getDimensionPixelOffset(R.dimen.firstHeight);
    	mSecondHeight = mContext.getResources().getDimensionPixelOffset(R.dimen.secondHeight);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mHeaderView = (ImageView) getChildAt(0);
        mListView = (ListView) getChildAt(1);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        super.requestDisallowInterceptTouchEvent(false);
    }

    float startPosY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
    	if (isAnimating) {
    		return true;
    	}
        int action = ev.getAction();
        switch(action) {
            case MotionEvent.ACTION_DOWN:
            	startPosY = ev.getY();
                return false;
            case MotionEvent.ACTION_MOVE:
                float speedY = ev.getY() - startPosY;
                startPosY = ev.getY();
                if (mHeaderView.getBottom() > mFirstHeight) {
                    return true;
                } else {
                    if (mListView.getChildAt(0).getTop() == 0 && speedY > 0 && mListView.getFirstVisiblePosition() == 0) {
                        return true;
                    }
                    return false;
                }
            case MotionEvent.ACTION_CANCEL:
                return super.onInterceptTouchEvent(ev);
            case MotionEvent.ACTION_UP:
                return super.onInterceptTouchEvent(ev);
        }
        return super.onInterceptTouchEvent(ev);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//    	if (pointerId != null) {
//    		int index = MotionEventCompat.getActionIndex(event);
//    		int id = event.getPointerId(index);
//    		if (id != pointerId) {
//    			return false;
//    		}
//    	}
    	preAction = event.getAction();
    	eventX = (int) event.getX();
    	eventY = (int) event.getY();
    	if (isAnimating) {
    		log("animating return");
    		return false;
    	}
        int action = event.getAction();
        switch(action) {
            case MotionEvent.ACTION_DOWN:
            	log("ACTION_DOWN");
//            	if (pointerId == null) {
//            		int index = MotionEventCompat.getActionIndex(event);
//                	pointerId = event.getPointerId(index);
//            	}
                break;
            case MotionEvent.ACTION_MOVE:
            	log("ACTION_MOVE");
                final float speedY = event.getY() - startPosY;
                startPosY = event.getY();
                float friction = ((float) mSecondHeight) / mHeaderView.getBottom();
                float newBottom = mHeaderView.getBottom() + speedY * friction;
                log("speedY = " + speedY + " friction = " + friction + " speedY * friction = " + (speedY * friction) + " newBottom = " + newBottom + " mSecondHeight = " + mSecondHeight);
//                if (newBottom >= mFirstHeight) {
                	if (newBottom >= mSecondHeight && (prePhase == PHASE_FIRST || prePhase == PHASE_SECOND)) {
                		prePhase = PHASE_SECOND;
                		log("second phase");
                		mHeaderView.getLayoutParams().height = (int) newBottom;
                		log("second phase height = " + newBottom);
                        requestLayout();
                        if (mOnAnimationListener != null) {
                			if (speedY > 0) {
                				mOnAnimationListener.onSecondDownAnimation(mHeaderView.getLayoutParams().height - mSecondHeight);
                			} else if (speedY < 0) {
                				mOnAnimationListener.onSecondUpAnimation(mHeaderView.getLayoutParams().height - mSecondHeight);
                			}
                		}
                        return true;
                	} else if (newBottom <= mFirstHeight && prePhase == PHASE_ZERO) {
                		 log("new Bottom < firstHeight cancel touch");
                		 prePhase = PHASE_ZERO;
                		 mHeaderView.getLayoutParams().height = mFirstHeight;
                         MotionEvent cancelEvent = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, event.getX(), event.getY(), 1);
                         dispatchTouchEvent(cancelEvent);
                         MotionEvent downEvent = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, event.getX(), event.getY(), 1);
                         dispatchTouchEvent(downEvent); 
                         cancelEvent.recycle();
                         downEvent.recycle();
                         requestLayout();
                         return true;
                	} else {  //newBottom 介于 firstHeight 和 secondHeight中间
                		if (prePhase == PHASE_SECOND) {
                			mHeaderView.getLayoutParams().height = mSecondHeight;
                			requestLayout();
                		}
                		prePhase = PHASE_FIRST;
                		log("first phase");
                		ValueAnimator animator = null;
                		if (speedY > 0) {
                			isAnimating = true;
                			animator = ValueAnimator.ofInt(mHeaderView.getHeight(), mSecondHeight);
                		} else if (speedY < 0) {
                			isAnimating = true;
                			animator = ValueAnimator.ofInt(mHeaderView.getHeight(), mFirstHeight);
                		}
                		if (animator != null) {
                			final int initialHeight = mHeaderView.getHeight();
                			animator.addUpdateListener(new AnimatorUpdateListener() {
                				int preHeight = initialHeight;
                				
								@Override
								public void onAnimationUpdate(ValueAnimator animation) {
									mHeaderView.getLayoutParams().height = (Integer) animation.getAnimatedValue();
									requestLayout();
									if (mOnAnimationListener != null) {
										preHeight = (Integer) animation.getAnimatedValue();
										if (speedY > 0) {
											mOnAnimationListener.onFirstDownAnimation(animation.getAnimatedFraction(), mHeaderView.getHeight() - mSecondHeight);
										} else if (speedY < 0) {
											mOnAnimationListener.onFirstUpAnimation(animation.getAnimatedFraction(), mHeaderView.getHeight() - mSecondHeight);
										}
									}
								}
							});
                			animator.addListener(new AnimatorListener() {
								@Override
								public void onAnimationStart(Animator animation) {
								}
								
								@Override
								public void onAnimationRepeat(Animator animation) {
								}
								
								@Override
								public void onAnimationEnd(Animator animation) {
									isAnimating = false;
									if (speedY < 0) {
										prePhase = PHASE_ZERO;
									}
									if (speedY > 0 && preAction == MotionEvent.ACTION_MOVE) {
										log("cancelTouchEvent");
										cancelTouchEvent();
									}
								}
								
								@Override
								public void onAnimationCancel(Animator animation) {
								}
							});
                			animator.setDuration(500);
                			animator.start();
                		}
                		return true;
                	}
            case MotionEvent.ACTION_CANCEL:
            	log("ACTION_CANCEL");
//            	if (pointerId != null) {
//            		int index = MotionEventCompat.getActionIndex(event);
//            		
//            		int pointerIndex = event.findPointerIndex(pointerId);
//            		if (index == pointerIndex) {
//            			pointerId = null;
//            		}
//            	}
                break;
            case MotionEvent.ACTION_UP:
            	log("ACTION_UP");
            	log("headerHeight = " + mHeaderView.getHeight() + " mSecondHeight = " + mSecondHeight);
//            	if (pointerId != null) {
//            		int index = MotionEventCompat.getActionIndex(event);
//            		int id = event.getPointerId(index);
//            		if (id == pointerId) {
//            			pointerId = null;
//            		}
//            	}
            	if (mHeaderView.getLayoutParams().height > mSecondHeight) {
            		isAnimating = true;
            		ValueAnimator animator = ValueAnimator.ofInt(mHeaderView.getHeight(), mSecondHeight);
            		final int initalHeight = mHeaderView.getHeight();
            		animator.addUpdateListener(new AnimatorUpdateListener() {
            			int preHeight = initalHeight;
						@Override
						public void onAnimationUpdate(ValueAnimator animation) {
							preHeight = (Integer) animation.getAnimatedValue();
							mHeaderView.getLayoutParams().height = (Integer) animation.getAnimatedValue();
							requestLayout();
							if (mOnAnimationListener != null) {
								mOnAnimationListener.onSecondUpAnimation(mHeaderView.getHeight() - mSecondHeight);
							}
						}
					});
            		animator.addListener(new AnimatorListener() {
						@Override
						public void onAnimationStart(Animator animation) {
							
						}
						
						@Override
						public void onAnimationRepeat(Animator animation) {
							
						}
						
						@Override
						public void onAnimationEnd(Animator animation) {
							isAnimating = false;
						}
						
						@Override
						public void onAnimationCancel(Animator animation) {
						}
					});
            		animator.setDuration(500);
            		animator.start();
            	}
                break;
        }
        return super.onTouchEvent(event);
    }
    
    private void cancelTouchEvent() {
    	 MotionEvent cancelEvent = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, eventX, eventY, 1);
         dispatchTouchEvent(cancelEvent);
         MotionEvent downEvent = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, eventX, eventY, 1);
         dispatchTouchEvent(downEvent); 
         cancelEvent.recycle();
         downEvent.recycle();
    }
}
