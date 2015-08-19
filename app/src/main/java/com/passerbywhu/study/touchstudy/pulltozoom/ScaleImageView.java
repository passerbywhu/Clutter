package com.passerbywhu.study.touchstudy.pulltozoom;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by wuwenchao3 on 2015/8/13.
 */
public class ScaleImageView extends ImageView {
    public ScaleImageView(Context context) {
        this(context, null);
    }

    public ScaleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Drawable bg = getDrawable();
        if (bg != null) {
            Log.e("hehe", "bg != null");
            int bgWidth = bg.getIntrinsicWidth();
            int bgHeight = bg.getIntrinsicHeight();
            int sWidth = w;
            int sHeight = h;
            int scaleWidth = (int) (bgWidth * ((float) (sHeight / bgHeight)));
            Log.e("hehe", "bgWidth = " + bgWidth + " scaleWidth = " + scaleWidth);
            int top = 0;
            int bottom = sHeight;
            int left = -(scaleWidth - sWidth) / 2;
            int right = left + scaleWidth;
            bg.setBounds(left, top, right, bottom);
        }
    }
}
