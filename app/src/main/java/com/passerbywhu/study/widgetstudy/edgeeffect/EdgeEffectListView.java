package com.passerbywhu.study.widgetstudy.edgeeffect;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by wuwenchao3 on 2015/7/1.
 */
public class EdgeEffectListView extends ListView {
    public EdgeEffectListView(Context context) {
        super(new ContextWrapperEdgeEffect(context));
    }

    public EdgeEffectListView(Context context, AttributeSet attrs) {
        super(new ContextWrapperEdgeEffect(context), attrs);
    }

    public EdgeEffectListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(new ContextWrapperEdgeEffect(context), attrs, defStyleAttr);
    }
}
