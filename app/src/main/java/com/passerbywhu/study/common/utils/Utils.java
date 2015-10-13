package com.passerbywhu.study.common.utils;

import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.Toast;

import com.passerbywhu.study.MyApplication;
import com.passerbywhu.study.common.consts.Const;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuwenchao3 on 2015/5/18.
 */
public class Utils {
    public static String generateImageUrl(int index) {
        return Const.imgUrls[index % Const.imgUrls.length];
    }

    public static List<String> generateImageList(int page) {
        return generateImageList(page, Const.DEFAULT_PAGESIZE);
    }

    public static List<String> generateImageList(int page, int pageSize) {
        int currentSize = page * pageSize;
        int needLoadSize = Const.DEFAULT_TOTAL_SIZE  - currentSize;
        if (needLoadSize < 0) {
            needLoadSize = 0;
        }
        if (needLoadSize > pageSize) {
            needLoadSize = pageSize;
        }
        List<String> imageList = new ArrayList<String>();
        for (int i = 0; i <needLoadSize; i ++) {
            imageList.add(generateImageUrl((int) Math.random() * Const.DEFAULT_TOTAL_SIZE));
        }
        return imageList;
    }

    private static Toast toast;


    public static void showToast(String msg) {
        if (toast == null) {
            synchronized (Utils.class) {
                if (toast == null) {
                    toast = Toast.makeText(MyApplication.getInstance().getApplicationContext(), "", Toast.LENGTH_SHORT);
                }
            }
        }
        toast.setText(msg);
        toast.show();
    }

    public static void showToast(int resId) {
        showToast(MyApplication.getInstance().getString(resId));
    }

    public static String getTouchActionName(int action) {
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                return "ACTION_DOWN";
            case MotionEvent.ACTION_MOVE:
                return "ACTION_MOVE";
            case MotionEvent.ACTION_UP:
                return "ACTION_UP";
            case MotionEvent.ACTION_CANCEL:
                return "ACTION_CANCEL";
            case MotionEvent.ACTION_POINTER_DOWN:
                return "POINTER_DOWN";
            case MotionEvent.ACTION_POINTER_UP:
                return "POINTER_UP";
        }
        return "";
    }

    public static String getScrollState(int state) {
        switch (state) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                return "SCROLL_STATE_IDLE";
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                return "SCROLL_STATE_TOUCH_SCROLL";
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                return "SCROLL_STATE_FLING";
        }
        return "";
    }
}
