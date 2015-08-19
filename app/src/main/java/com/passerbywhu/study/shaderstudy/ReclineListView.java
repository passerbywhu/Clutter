package com.passerbywhu.study.shaderstudy;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ListView;

public class ReclineListView extends ListView {
	private Camera mCamera;
	private Matrix mMatrix;

	public ReclineListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mCamera = new Camera();
		mMatrix = new Matrix();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		mCamera.save();
		mCamera.rotate(30, 0, 0);
		mCamera.getMatrix(mMatrix);
		mMatrix.preTranslate(-getWidth() / 2, -getHeight() / 2);
		mMatrix.postTranslate(getWidth() / 2, getHeight() / 2);
		canvas.concat(mMatrix);
		super.onDraw(canvas);
		mCamera.restore();
	}
}