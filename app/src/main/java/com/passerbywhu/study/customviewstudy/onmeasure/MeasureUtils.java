package com.passerbywhu.study.customviewstudy.onmeasure;

import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;

import com.passerbywhu.study.MyApplication;
import com.passerbywhu.study.customviewstudy.onmeasure.meausreinfo.FrameLayoutWithMeasureInfo;
import com.passerbywhu.study.customviewstudy.onmeasure.meausreinfo.LinearLayoutWithMeasureInfo;
import com.passerbywhu.study.customviewstudy.onmeasure.meausreinfo.RelativeLayoutWithMeasureInfo;

/**
 * Created by wuwenchao3 on 2015/7/2.
 */
public class MeasureUtils {
    public static Paint PAINT;

    static {
        PAINT = new Paint();
        PAINT.setAntiAlias(true);
        PAINT.setColor(MyApplication.getInstance().getResources().getColor(android.R.color.black));
        PAINT.setTextSize(40);
    }

    public static int PADDING = 30;
    public static String getModeName(int mode) {
        if (mode == View.MeasureSpec.AT_MOST) {
            return "AT_MOST";
        }
        if (mode == View.MeasureSpec.EXACTLY) {
            return "EXACTLY";
        }
        if (mode == View.MeasureSpec.UNSPECIFIED) {
            return "UNSPECIFIED";
        }
        return "UNKNOWN_MODE";
    }

    public static String getLayoutParamName(int layoutParam) {
        if (layoutParam == ViewGroup.LayoutParams.MATCH_PARENT) {
            return "MATCH_PARENT";
        }
        if (layoutParam == ViewGroup.LayoutParams.WRAP_CONTENT) {
            return "WRAP_CONTENT";
        }
        return "EXACTLY " + layoutParam;
    }

    public static String getMeasureInfo(ViewGroup layout) {
        StringBuilder sb = new StringBuilder();
        if (layout instanceof FrameLayoutWithMeasureInfo) {
            sb.append(((FrameLayoutWithMeasureInfo) layout).getMeasureInfo());
        } else if (layout instanceof RelativeLayoutWithMeasureInfo) {
            sb.append(((RelativeLayoutWithMeasureInfo) layout).getMeasureInfo());
        } else if (layout instanceof LinearLayoutWithMeasureInfo) {
            sb.append(((LinearLayoutWithMeasureInfo) layout).getMeasureInfo());
        }
        sb.append("\n");
        int childCount = layout.getChildCount();  //在这个程序中只有为1的情况。
        for (int i = 0;i < childCount; i ++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                sb.append(getMeasureInfo((ViewGroup) child) + "\n");
            }
        }
        return sb.toString();
    }

    public static void clearMeasureInfo(ViewGroup layout) {
        if (layout instanceof FrameLayoutWithMeasureInfo) {
            ((FrameLayoutWithMeasureInfo) layout).setMeasureInfo(new StringBuilder());
        } else if (layout instanceof RelativeLayoutWithMeasureInfo) {
            ((RelativeLayoutWithMeasureInfo) layout).setMeasureInfo(new StringBuilder());
        } else if (layout instanceof LinearLayoutWithMeasureInfo) {
            ((LinearLayoutWithMeasureInfo) layout).setMeasureInfo(new StringBuilder());
        }
        int childCount = layout.getChildCount();  //在这个程序中只有为1的情况。
        for (int i = 0;i < childCount; i ++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                clearMeasureInfo((ViewGroup) child);
            }
        }
    }
}
