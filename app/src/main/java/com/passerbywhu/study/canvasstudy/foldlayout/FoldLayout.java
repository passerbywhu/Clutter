package com.passerbywhu.study.canvasstudy.foldlayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.passerbywhu.study.common.utils.Logger;

/**
 * Created by wuwenchao3 on 2015/8/31.
 */
public class FoldLayout extends FrameLayout {
    private int divideNum = 8;
    private Matrix[] mMatrixes;
    private Rect[] src, dst;
    private Bitmap snapshot;
    private Canvas mCanvas;
    private float mFactor = 0.5f;
    private int depth = 50;
    private GestureDetector mGestureDetector;
    private boolean needUpdate = false;

    public FoldLayout(Context context) {
        this(context, null);
    }

    public FoldLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FoldLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mMatrixes = new Matrix[divideNum];
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                float augment = distanceX / getWidth();
                mFactor -= augment;
                mFactor = Math.min(1.0f, Math.max(0, mFactor));
                needUpdate = true;
                Logger.e("onScroll augment = " + augment);
                invalidate();
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        needUpdate = true;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (snapshot == null || needUpdate) {
            needUpdate = false;
            if (snapshot != null) {
                snapshot.recycle();
                snapshot = null;
            }
            snapshot = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(snapshot);
//            mCanvas.drawColor(getResources().getColor(android.R.color.holo_red_light));
            super.dispatchDraw(mCanvas);  //画到bitmap中，之后就直接画bitmap而不dispatchDraw了。

            src = new Rect[divideNum];
            dst = new Rect[divideNum];
            int partWidth = getWidth() / divideNum;
            int transWidth = (int) (getWidth() / divideNum * mFactor);
            Logger.e("transWidt = " + transWidth);
            for (int i = 0; i < divideNum; i ++) {
                src[i] = new Rect(i * partWidth, 0, i * partWidth + partWidth, getHeight());
                dst[i] = new Rect(i * transWidth, 0, i * transWidth + transWidth, getHeight());
            }
            mMatrixes = new Matrix[divideNum];
            for (int i = 0; i < divideNum; i ++) {
                mMatrixes[i] = new Matrix();
                boolean isEven = (i % 2 == 0);
                mMatrixes[i].setPolyToPoly(new float[] {
                        src[i].left, src[i].top,
                        src[i].right, src[i].top,
                        src[i].right, src[i].bottom,
                        src[i].left, src[i].bottom,
                }, 0, new float[] {
                        dst[i].left, isEven ? dst[i].top : dst[i].top + depth,
                        dst[i].right, isEven ? dst[i].top + depth : dst[i].top,
                        dst[i].right, isEven ? dst[i].bottom - depth : dst[i].bottom,
                        dst[i].left, isEven ? dst[i].bottom : dst[i].bottom - depth,
                }, 0, 4);
            }
            for (int i = 0; i < divideNum; i ++) {
                canvas.save();
                canvas.concat(mMatrixes[i]);
                canvas.drawBitmap(snapshot, src[i], src[i], null);
                canvas.restore();
            }
        } else {
            for (int i = 0; i < divideNum; i ++) {
                canvas.save();
                canvas.concat(mMatrixes[i]);
                canvas.drawBitmap(snapshot, src[i], src[i], null);
                canvas.restore();
            }
        }
    }
}
