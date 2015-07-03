package com.passerbywhu.study.customviewstudy.onmeasure.meausreinfo;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.passerbywhu.study.R;
import com.passerbywhu.study.common.activity.BaseActivity;
import com.passerbywhu.study.common.utils.Utils;
import com.passerbywhu.study.customviewstudy.onmeasure.MeasureUtils;

import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * Created by wuwenchao3 on 2015/7/3.
 */
public class MeasureInfoActivity extends BaseActivity {
    protected ViewGroup leafLayout;  //初始为leafLayout
    @InjectView(R.id.rootLayout)
    protected ViewGroup rootLayout;
    @InjectView(R.id.drawerLayout)
    protected DrawerLayout mDrawerLayout;
    @InjectView(R.id.leftDrawer)
    protected ViewGroup mLeftDrawer;
    @InjectView(R.id.rightDrawer)
    protected ViewGroup mRightDrawer;
    @InjectView(R.id.leftDrawerContent)
    protected ViewGroup mLeftDrawerContent;
    @InjectView(R.id.rightTextView)
    protected TextView mRightTextView;
    private ActionBarDrawerToggle mToggle;
    private ColorPickerFragment mColorPickerFragment;

    private ViewGroup needAddLayout;

    private static final String[] depthName = "First Second Third Fourth Fifth Sixth Seventh Eighth Nineth".split(" ");
    private int depth = 1;

    public static class ColorSelectedEvent {
        int color;
        public ColorSelectedEvent(int color) {
            this.color = color;
        }
    }

    private int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        leafLayout = rootLayout;
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (drawerView == mRightDrawer) {
                    mRightTextView.setText(MeasureUtils.getMeasureInfo(rootLayout));
                }
            }
        };
        mToggle.syncState();
        mDrawerLayout.setDrawerListener(mToggle);
        mColorPickerFragment = new ColorPickerFragment();
        ((FrameLayoutWithMeasureInfo) rootLayout).setName(depthName[depth - 1] + " FrameLayout");
        mLeftDrawerContent.addView(LeftDrawerContent.generateLeftDrawerContent(rootLayout, this, new LeftDrawerContent.OnParamChangeListener() {
            @Override
            public void onParamChange() {
//                rootLayout.invalidate();
            }
        }));
        color = getResources().getColor(android.R.color.white);
        mColorPickerFragment.setOldColor(color);
    }

    public void onEvent(ColorSelectedEvent event) {
        color = event.color;
        if (leafLayout != null && needAddLayout != null) {
            needAddLayout.setBackgroundColor(color);
            leafLayout.addView(needAddLayout);
            leafLayout = needAddLayout;
            needAddLayout = null;
            mLeftDrawerContent.removeAllViews();
            mLeftDrawerContent.addView(LeftDrawerContent.generateLeftDrawerContent(rootLayout, this, new LeftDrawerContent.OnParamChangeListener() {
                @Override
                public void onParamChange() {
//                    rootLayout.invalidate();
                }
            }));
        } else if (leafLayout != null) {
            leafLayout.setBackgroundColor(color);
        }
    }

    @Override
    protected String createTitle() {
        return "MeasureInfo";
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected int createChildViewId() {
        return R.layout.measure_info_activity;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 0, 0, "Show MeasureInfo").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(Menu.NONE, 1, 1, "Add LinearLayout").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(Menu.NONE, 2, 2, "Add FrameLayout").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(Menu.NONE, 3, 3, "Add RelatvieLayout").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case 0:
                if (mDrawerLayout.isDrawerOpen(Gravity.END)) {
                    mDrawerLayout.closeDrawer(Gravity.END);
                } else if (!mDrawerLayout.isDrawerOpen(Gravity.END)) {
                    mDrawerLayout.openDrawer(Gravity.END);
                }
                break;
            case 1:
                if (depth == depthName.length) {
                    Utils.showToast("Hierarchy Too Deep!");
                    break;
                }
                needAddLayout = new LinearLayoutWithMeasureInfo(this);
                ((LinearLayoutWithMeasureInfo) needAddLayout).setName(depthName[depth ++] + " LinearLayout");
                needAddLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                showColorPicker();
                break;
            case 2:
                if (depth == depthName.length) {
                    Utils.showToast("Hierarchy Too Deep!");
                    break;
                }
                needAddLayout = new FrameLayoutWithMeasureInfo(this);
                ((FrameLayoutWithMeasureInfo) needAddLayout).setName(depthName[depth ++] + " FrameLayout");
                needAddLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                showColorPicker();
                break;
            case 3:
                if (depth == depthName.length) {
                    Utils.showToast("Hierarchy Too Deep!");
                    break;
                }
                needAddLayout = new RelativeLayoutWithMeasureInfo(this);
                ((RelativeLayoutWithMeasureInfo) needAddLayout).setName(depthName[depth ++] + " RelatvieLayout");
                needAddLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                showColorPicker();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showColorPicker() {
        mColorPickerFragment.setOldColor(color);
        mColorPickerFragment.show(getSupportFragmentManager(), "COLOR_PICKER");
    }
}
