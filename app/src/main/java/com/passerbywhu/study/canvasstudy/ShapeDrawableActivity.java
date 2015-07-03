package com.passerbywhu.study.canvasstudy;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wuwenchao3 on 2015/6/30.
 */
public class ShapeDrawableActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomDrawableView customDrawableView = new CustomDrawableView(this);
        customDrawableView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(customDrawableView);
    }

    private class CustomDrawableView extends View {
        private ShapeDrawable mDrawable;

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            setMeasuredDimension(MeasureSpec.makeMeasureSpec(width, widthMode), MeasureSpec.makeMeasureSpec(height, heightMode));
        }

        public CustomDrawableView(Context context) {
            super(context);

//            int x = 10;
//            int y = 10;
//            int width = 300;
//            int height = 50;

            mDrawable = new ShapeDrawable(new OvalShape());
            mDrawable.getPaint().setColor(0xff74AC23);
//            mDrawable.setBounds(x, y, x + width, y + height);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mDrawable.setBounds(getLeft(), getTop(), getLeft() + w, getTop() + h);
        }

        protected void onDraw(Canvas canvas) {
            mDrawable.draw(canvas);
        }
    }
}
