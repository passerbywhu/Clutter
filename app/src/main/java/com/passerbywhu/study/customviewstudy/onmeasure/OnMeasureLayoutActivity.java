package com.passerbywhu.study.customviewstudy.onmeasure;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.passerbywhu.study.R;
import com.passerbywhu.study.common.activity.BaseActivity;

import butterknife.InjectView;

/**
 * Created by wuwenchao3 on 2015/7/1.
 */
public class OnMeasureLayoutActivity extends BaseActivity {
    @InjectView(R.id.topLayout)
    protected OnMeasureLayout topLayout;
    @InjectView(R.id.drawerLayout)
    protected DrawerLayout mDrawerLayout;
    @InjectView(R.id.leftDrawer)
    protected RelativeLayout mLeftDrawer;
    @InjectView(R.id.leftDrawerContent)
    protected ViewGroup mLeftDrawerContent;
    @InjectView(R.id.rightDrawer)
    protected  RelativeLayout mRightDrawer;
    @InjectView(R.id.rightTextView)
    protected  TextView mRightTextView;
    private ActionBarDrawerToggle mToggle;

    private ViewGroup treeViewGroup;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d("haha", getRequestedOrientation() + "");
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            super.onConfigurationChanged(newConfig);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            super.onConfigurationChanged(newConfig);
        } else {
            super.onConfigurationChanged(newConfig);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (drawerView == mRightDrawer) {
                    mRightTextView.setText(topLayout.getMeasureInfo());
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (drawerView == mLeftDrawer) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            topLayout.invalidate();
                        }
                    }, 5000);
                }
            }
        };
        mToggle.syncState();
        mDrawerLayout.setDrawerListener(mToggle);
        mLeftDrawerContent.addView(LeftDrawerContent.generateLeftDrawerContent(topLayout, this, new LeftDrawerContent.OnParamChangeListener() {
            @Override
            public void onParamChange() {
                mDrawerLayout.closeDrawer(Gravity.START);
            }
        }));
    }

    @Override
    protected int createChildViewId() {
        return R.layout.onmeasure_layout_activity;
    }

    @Override
    protected String createTitle() {
        return "OnMeasureLayoutActivity";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 0, 0, "Look Measure Info");
        menu.add(Menu.NONE, 1, 1, "Add LinearLayout");
        menu.add(Menu.NONE, 2, 2, "Add FrameLayout");
        menu.add(Menu.NONE, 3, 3, "Add RelativeLayout");
        menu.add(Menu.NONE, 4, 4, "Toggle rotate");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                showToggle();
                return true;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LOCKED) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showToggle() {
        if (mDrawerLayout.isDrawerOpen(Gravity.END)) {
            mDrawerLayout.closeDrawer(Gravity.END);
        } else {
            mDrawerLayout.openDrawer(Gravity.END);
        }
    }
}
