package com.passerbywhu.study.widgetstudy.edgeeffect;

import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import com.passerbywhu.study.R;

/**
 * Created by wuwenchao3 on 2015/7/1.
 */
public class ResourcesEdgeEffect extends Resources {
    private int overscroll_edge = getPlatformDrawableId("overscroll_edge");
    private int overscroll_glow = getPlatformDrawableId("overscroll_glow");
    private ContextWrapper mContextWrapper;

    public ResourcesEdgeEffect(AssetManager assets, DisplayMetrics metrics, Configuration config, ContextWrapper contextWrapper) {
        super(assets, metrics, config);
        this.mContextWrapper = contextWrapper;
    }

    private int getPlatformDrawableId(String name) {
        try {
            int i = ((Integer) Class.forName("com.android.internal.R$drawable").getField(name).get(null)).intValue();
            return i;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return 0;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return 0;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return 0;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Drawable getDrawable(int resId) {
        if (resId == this.overscroll_edge) {
//            return mContextWrapper.getBaseContext().getResources().getDrawable(R.drawable.bg);
            return super.getDrawable(R.drawable.default_place_holder);
        }
        if (resId == this.overscroll_glow) {
            return super.getDrawable(R.drawable.default_place_holder);
//            return mContextWrapper.getBaseContext().getResources().getDrawable(R.drawable.bg);
        }
        return super.getDrawable(resId);
    }
}
