package com.passerbywhu.study.widgetstudy.edgeeffect;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;

/**
 * Created by wuwenchao3 on 2015/7/1.
 */
public class ContextWrapperEdgeEffect extends ContextWrapper {
    private static ResourcesEdgeEffect RES_EDGE_EFFECT;

    public ContextWrapperEdgeEffect(Context context) {
        super(context);
        Resources resources = context.getResources();
        if (RES_EDGE_EFFECT == null) {
            RES_EDGE_EFFECT = new ResourcesEdgeEffect(resources.getAssets(), resources.getDisplayMetrics(), resources.getConfiguration(), this);
        }
    }

    @Override
    public Resources getResources() {
        return RES_EDGE_EFFECT;
    }
}
