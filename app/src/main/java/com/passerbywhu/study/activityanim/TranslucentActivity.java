package com.passerbywhu.study.activityanim;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

/**
 * Created by wuwenchao3 on 2015/9/18.
 */
public class TranslucentActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getDecorView().setBackgroundDrawable(null);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        TypedArray a = getTheme().obtainStyledAttributes(new int[]{
//                android.R.attr.windowBackground
//        });
//        int background = a.getResourceId(0, 0);
//        a.recycle();
//
//        ViewGroup decor = (ViewGroup) getWindow().getDecorView();
//        ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
////        decorChild.setBackgroundResource(background);
//        decor.removeView(decorChild);
//        addView(decorChild);
//        setContentView(decorChild);
//        decor.addView(this);
    }
}
