package com.passerbywhu.study.animationstudy;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.passerbywhu.study.common.model.ShapeHolder;

public class LoadingView extends View {
	private ShapeHolder[] balls = new ShapeHolder[4];
	private float animateRadius = 50f;
	private float centerX, centerY;
	private float ballRaidus = 25f;
	private ValueAnimator canvasRotateAnimator;
	private float rotateDegree;

	public LoadingView(Context context) {
		super(context);
		init();
	}
	
	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.rotate(rotateDegree, centerX, centerY);
		for (int i = 0; i < balls.length; i ++) {
			if (balls[i] != null) {
				canvas.save();
				canvas.translate(balls[i].getX(), balls[i].getY());
				balls[i].getShape().draw(canvas);
				canvas.restore();
			}
		}
	}
	
	private void init() {
		post(new Runnable() {
			@Override
			public void run() {
				int width = getWidth();
				int height = getHeight();
				centerX = width / 2;
				centerY = height / 2;
				balls[0] = generateBall(centerX - animateRadius, centerY - animateRadius);
				startBallAnimation(balls[0], -ballRaidus, -ballRaidus);
				balls[1] = generateBall(centerX + animateRadius, centerY - animateRadius);
				startBallAnimation(balls[1], ballRaidus, -ballRaidus);
				balls[2] = generateBall(centerX - animateRadius, centerY + animateRadius);
				startBallAnimation(balls[2], -ballRaidus, ballRaidus);
				balls[3] = generateBall(centerX + animateRadius, centerY + animateRadius);
				startBallAnimation(balls[3], ballRaidus, ballRaidus);
			}
		});
		canvasRotateAnimator = ValueAnimator.ofFloat(0f, 360f);
		canvasRotateAnimator.setDuration(5000);
		canvasRotateAnimator.setRepeatCount(ValueAnimator.INFINITE);
		canvasRotateAnimator.setInterpolator(new LinearInterpolator());
		canvasRotateAnimator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				invalidate();
				rotateDegree = (Float) animation.getAnimatedValue();
			}
		});
		canvasRotateAnimator.start();
	}
	
	private void startBallAnimation(ShapeHolder shapeHolder, float offsetX, float offsetY) {
		PropertyValuesHolder pvhx = PropertyValuesHolder.ofFloat("x", centerX + offsetX - 25);
		PropertyValuesHolder pvhy = PropertyValuesHolder.ofFloat("y", centerY + offsetY - 25);
		ObjectAnimator moveAnimator = ObjectAnimator.ofPropertyValuesHolder(shapeHolder, pvhx, pvhy);
		moveAnimator.setDuration(2000);
		moveAnimator.setInterpolator(new LinearInterpolator());
		moveAnimator.setRepeatCount(ValueAnimator.INFINITE);
		moveAnimator.setRepeatMode(ValueAnimator.REVERSE);
		moveAnimator.start();
	}
	
	private ShapeHolder generateBall(float x, float y) {
		OvalShape circle = new OvalShape();
		circle.resize(ballRaidus * 2, ballRaidus * 2);
		ShapeDrawable drawable = new ShapeDrawable(circle);
		ShapeHolder shapeHolder = new ShapeHolder(drawable);
		shapeHolder.setX(x - ballRaidus);
		shapeHolder.setY(y - ballRaidus);
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
