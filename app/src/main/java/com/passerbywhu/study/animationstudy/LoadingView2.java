package com.passerbywhu.study.animationstudy;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.passerbywhu.study.common.model.ShapeHolder;

public class LoadingView2 extends View {
	private ShapeHolder[] balls = new ShapeHolder[4];
	private PointF[] realPos = new PointF[4];
	private float animateRadius = 35f;
	private float centerX, centerY;
	private float ballRaidus = 25f;
	private ValueAnimator canvasRotateAnimator;
	private float rotateDegree;
	private AnimatorSet ballAnimatorSet;
	private AnimatorSet divideAnimatorSet;
	private ValueAnimator scaleAnimator;
	private long divideAnimationDuration = 500;
	private long scaleAnimationDuration = 500;
	private long ballAnimationDuration = 250;

	public LoadingView2(Context context) {
		super(context);
		init();
	}
	
	public LoadingView2(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public LoadingView2(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
			canvas.rotate(rotateDegree, centerX, centerY);
			for (int i = balls.length - 1; i >= 0; i --) {
				if (balls[i] != null) {
					canvas.save();
					canvas.translate(balls[i].getX() - ballRaidus, balls[i].getY() - ballRaidus);
					balls[i].getShape().draw(canvas);
					canvas.restore();
				}
			}
			invalidate();
	}
	
	private void init2() {
		postDelayed(new Runnable() {
			@Override
			public void run() {
				int width = getWidth();
				int height = getHeight();
				centerX = width / 2;
				centerY = height / 2;
				
				for (int i = 0; i < balls.length; i ++) {
					balls[i] = generateBall(centerX, centerY);
				}
				
				ObjectAnimator animator1 = ObjectAnimator.ofFloat(balls[0], "x", centerX + 100);
				animator1.setDuration(50);
				animator1.setInterpolator(new LinearInterpolator());
//				animator1.addUpdateListener(new AnimatorUpdateListener() {
//					@Override
//					public void onAnimationUpdate(ValueAnimator animation) {
//						invalidate();
//					}
//				});
				ObjectAnimator animator2 = ObjectAnimator.ofFloat(balls[0], "x", centerX + 200);
				animator2.setDuration(50);
				animator2.setInterpolator(new LinearInterpolator());
//				animator2.addUpdateListener(new AnimatorUpdateListener() {
//					@Override
//					public void onAnimationUpdate(ValueAnimator animation) {
//						invalidate();
//					}
//				});
				ObjectAnimator animator3 = ObjectAnimator.ofFloat(balls[0], "x", centerX + 300);
				animator3.setDuration(50);
				animator3.setInterpolator(new LinearInterpolator());
//				animator3.addUpdateListener(new AnimatorUpdateListener() {
//					@Override
//					public void onAnimationUpdate(ValueAnimator animation) {
//						invalidate();
//					}
//				});
				AnimatorSet animatorSet = new AnimatorSet();
				animatorSet.play(animator1).before(animator2);
				animatorSet.play(animator3).after(animator2);
				animatorSet.start();
			}
		}, 1000);
	}
	
	private void init() {
		post(new Runnable() {
			@Override
			public void run() {
				int width = getWidth();
				int height = getHeight();
				centerX = width / 2;
				centerY = height / 2;
				
				realPos[0] = new PointF(centerX - animateRadius, centerY - animateRadius);
				realPos[1] = new PointF(centerX + animateRadius, centerY - animateRadius);
				realPos[2] = new PointF(centerX - animateRadius, centerY + animateRadius);
				realPos[3] = new PointF(centerX + animateRadius, centerY + animateRadius);
				
				for (int i = 0; i < balls.length; i ++) {
					balls[i] = generateBall(centerX, centerY);
					balls[i].setWidth(0f);
					balls[i].setHeight(0f);
				}
				
				PropertyValuesHolder pvsWidth = PropertyValuesHolder.ofFloat("width", 0, ballRaidus * 2);
				PropertyValuesHolder pvsHeight = PropertyValuesHolder.ofFloat("height", 0, ballRaidus * 2);
				scaleAnimator = ObjectAnimator.ofPropertyValuesHolder(balls[0], pvsWidth, pvsHeight);
				scaleAnimator.setDuration(scaleAnimationDuration);
				scaleAnimator.setInterpolator(new LinearInterpolator());
//				scaleAnimator.addUpdateListener(new AnimatorUpdateListener() {
//					@Override
//					public void onAnimationUpdate(ValueAnimator animation) {
//						invalidate();
//					}
//				});
				scaleAnimator.addListener(new AnimatorListener() {
					@Override
					public void onAnimationStart(Animator animation) {
					}
					
					@Override
					public void onAnimationRepeat(Animator animation) {
					}
					
					@Override
					public void onAnimationEnd(Animator animation) {
//						for (int i = 0; i < balls.length; i ++) {
//							balls[i].setWidth(ballRaidus * 2);
//							balls[i].setHeight(ballRaidus * 2);
//						}
					}
					
					@Override
					public void onAnimationCancel(Animator animation) {
					}
				});
				
				divideAnimatorSet = new AnimatorSet();
				ValueAnimator ball1Animator = generateBallDivideAnimatior(balls[0], 0);
				ValueAnimator ball2Animator = generateBallDivideAnimatior(balls[1], 1);
				ValueAnimator ball3Animator = generateBallDivideAnimatior(balls[2], 2);
				ValueAnimator ball4Animator = generateBallDivideAnimatior(balls[3], 3);
				divideAnimatorSet.playTogether(ball1Animator, ball2Animator, ball3Animator, ball4Animator);
//				ball4Animator.addUpdateListener(new AnimatorUpdateListener() {
//					@Override
//					public void onAnimationUpdate(ValueAnimator animation) {
//						invalidate();
//					}
//				});

				
				canvasRotateAnimator = ValueAnimator.ofFloat(0f, 360f);
				canvasRotateAnimator.setDuration(5000);
				canvasRotateAnimator.setRepeatCount(ValueAnimator.INFINITE);
				canvasRotateAnimator.setInterpolator(new LinearInterpolator());
				canvasRotateAnimator.addUpdateListener(new AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						rotateDegree = (Float) animation.getAnimatedValue();
//						invalidate();
					}
				});
				
				ballAnimatorSet = new AnimatorSet();
				ValueAnimator ballAnimator1 = generateBallAnimation(balls[0], -ballRaidus, -ballRaidus);
				ValueAnimator ballAnimator2 = generateBallAnimation(balls[1], ballRaidus, -ballRaidus);
				ValueAnimator ballAnimator3 = generateBallAnimation(balls[2], -ballRaidus, ballRaidus);
				ValueAnimator ballAnimator4 = generateBallAnimation(balls[3], ballRaidus, ballRaidus);
				ballAnimatorSet.playTogether(ballAnimator1, ballAnimator2, ballAnimator3, ballAnimator4, canvasRotateAnimator);
				
				AnimatorSet animatorSet = new AnimatorSet();
//				animatorSet.playSequentially(scaleAnimator, divideAnimatorSet, ballAnimatorSet);
				animatorSet.play(scaleAnimator).before(divideAnimatorSet);
				animatorSet.play(ballAnimatorSet).after(divideAnimatorSet);
				animatorSet.start();
			}
		});
	}
	
	private ObjectAnimator generateBallDivideAnimatior(ShapeHolder shapeHolder, int index) {
		PropertyValuesHolder pvsX = PropertyValuesHolder.ofFloat("x", centerX, realPos[index].x);
		PropertyValuesHolder pvsY = PropertyValuesHolder.ofFloat("y", centerY, realPos[index].y);
		if (index != 0) {
			PropertyValuesHolder pvsAlpha = PropertyValuesHolder.ofFloat("alpha", 0.5f, 1f);
			PropertyValuesHolder pvsWidth = PropertyValuesHolder.ofFloat("width", ballRaidus * 2, ballRaidus * 2);
			PropertyValuesHolder pvsHeight = PropertyValuesHolder.ofFloat("height", ballRaidus * 2, ballRaidus * 2);
			ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(shapeHolder, pvsX, pvsY, pvsAlpha, pvsWidth, pvsHeight);
			animator.setDuration(divideAnimationDuration);
			animator.setInterpolator(new LinearInterpolator());
			return animator;
		} else {
			ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(shapeHolder, pvsX, pvsY);
			animator.setDuration(divideAnimationDuration);
			animator.setInterpolator(new LinearInterpolator());
			return animator;
		}
	}
	
	private ObjectAnimator generateBallAnimation(ShapeHolder shapeHolder, float offsetX, float offsetY) {
		PropertyValuesHolder pvhx = PropertyValuesHolder.ofFloat("x", centerX + offsetX);
		PropertyValuesHolder pvhy = PropertyValuesHolder.ofFloat("y", centerY + offsetY);
		ObjectAnimator moveAnimator = ObjectAnimator.ofPropertyValuesHolder(shapeHolder, pvhx, pvhy);
		moveAnimator.setDuration(ballAnimationDuration);
		moveAnimator.setInterpolator(new LinearInterpolator());
		moveAnimator.setRepeatCount(ValueAnimator.INFINITE);
		moveAnimator.setRepeatMode(ValueAnimator.REVERSE);
		return moveAnimator;
	}
	
	private ShapeHolder generateBall(float x, float y) {
		return generateBall(new PointF(x, y));
	}
	
	private ShapeHolder generateBall(PointF point) {
		float x = point.x;
		float y = point.y;
		OvalShape circle = new OvalShape();
		circle.resize(ballRaidus * 2, ballRaidus * 2);
		ShapeDrawable drawable = new ShapeDrawable(circle);
		ShapeHolder shapeHolder = new ShapeHolder(drawable);
		shapeHolder.setX(x);
		shapeHolder.setY(y);
		int red = (int) (Math.random() * 255);
		int green = (int) (Math.random() * 255);
		int blue = (int) (Math.random() * 255);
		int color = 0x88000000 | red << 16 | green << 8 | blue;
		Paint paint = drawable.getPaint();
		int darkColor = 0x88000000 | red/4 << 16 | green/4 << 8 | blue/4;
		RadialGradient gradient = new RadialGradient(37.5f, 12.5f, 50f, color, darkColor, Shader.TileMode.CLAMP);
		paint.setShader(gradient);
		shapeHolder.setPaint(paint);
		return shapeHolder;
	}
}
