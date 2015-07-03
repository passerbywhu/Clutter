package com.passerbywhu.study.customviewstudy.onmeasure.meausreinfo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RelativeLayout;

import com.passerbywhu.study.customviewstudy.onmeasure.MeasureUtils;

import static com.passerbywhu.study.customviewstudy.onmeasure.MeasureUtils.PADDING;
import static com.passerbywhu.study.customviewstudy.onmeasure.MeasureUtils.getLayoutParamName;
import static com.passerbywhu.study.customviewstudy.onmeasure.MeasureUtils.getModeName;

/**
 * Created by wuwenchao3 on 2015/7/2.
 */
public class RelativeLayoutWithMeasureInfo extends RelativeLayout {
    public StringBuilder getMeasureInfo() {
        return measureInfo;
    }

    public void setMeasureInfo(StringBuilder measureInfo) {
        this.measureInfo = measureInfo;
    }

    private StringBuilder measureInfo = new StringBuilder();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public RelativeLayoutWithMeasureInfo(Context context) {
        super(context);
        setGravity(Gravity.CENTER);
        setPadding(PADDING, PADDING, PADDING, PADDING);
    }

    public RelativeLayoutWithMeasureInfo(Context context, AttributeSet attrs) {
        super(context, attrs);
        setGravity(Gravity.CENTER);
        setPadding(PADDING, PADDING, PADDING, PADDING);
    }

    public RelativeLayoutWithMeasureInfo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setGravity(Gravity.CENTER);
        setPadding(PADDING, PADDING, PADDING, PADDING);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureInfo.append("name = " + name + "\n");
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        measureInfo.append("width = " + width + " widthMode = " + getModeName(widthMode) + "\n");
        measureInfo.append("height = " + height + " heightMode = " + getModeName(heightMode) + "\n");
        measureInfo.append("paramWidth = " + getLayoutParamName(getLayoutParams().width) + "\n");
        measureInfo.append("paramHeight = " + getLayoutParamName(getLayoutParams().height) + "\n");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureInfo.append("measuredWidth = " + getMeasuredWidth() + " measuredHeight = " + getMeasuredHeight() + "\n\n");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!TextUtils.isEmpty(name)) {
            Paint paint = MeasureUtils.PAINT;
            Rect textBound = new Rect();
            paint.getTextBounds(name, 0, name.length(), textBound);
            int textWidth = textBound.right - textBound.left;
            int textHeight = textBound.bottom - textBound.top;
            paint.setColor(getResources().getColor(android.R.color.darker_gray));
            canvas.drawRect(0, 0, textWidth, textHeight, paint);
            paint.setColor(getResources().getColor(android.R.color.black));
            new StaticLayout(name, new TextPaint(paint), textWidth + 10, Layout.Alignment.ALIGN_NORMAL, 1, 0, false).draw(canvas);

//
//            canvas.drawText(name, 0, textHeight, paint);
        }
    }
}
