package com.passerbywhu.study.touchstudy.dragstudy;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.passerbywhu.study.R;
import com.passerbywhu.study.common.adapter.ImageAdapter;
import com.passerbywhu.study.common.asynctask.AsyncTaskBase;
import com.passerbywhu.study.common.consts.Const;
import com.passerbywhu.study.common.utils.Utils;

import java.util.List;

/**
 * Created by wuwenchao3 on 2015/5/14.
 */
public class DragSideMenuActivity extends Activity {
    private BesideDragLayout mDragResideLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;
    private ImageAdapter mImageAdapter;
    private int page = 0;
    private LoadImageTask mLoadImageTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_side_menu);
        mDragResideLayout = (BesideDragLayout) findViewById(R.id.dragresideLayout);
        mDragResideLayout.setDragListener(new BesideDragLayout.DragListener() {
            @Override
            public void onOpen() {

            }

            @Override
            public void onClose() {

            }

            @Override
            public void onDrag(float percent) {

            }
        });
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setColorScheme(R.color.swipe_color_1, R.color.swipe_color_2, R.color.swipe_color_3, R.color.swipe_color_4);
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Utils.showToast(position + " item clicked");
            }
        });
//        mListView.addFooterView(new );
        mImageAdapter = new ImageAdapter(this);
        mListView.setAdapter(mImageAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                initiateRefresh();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                initiateRefresh();
            }
        }, 500);
//        initiateRefresh();
    }

    @Override
    protected void onDestroy() {
        if (mLoadImageTask != null) {
            mLoadImageTask.cancel(true);
            mLoadImageTask = null;
        }
        super.onDestroy();
    }

    private void initiateRefresh() {
        if (mLoadImageTask != null) {
            mLoadImageTask.cancel(true);
            mLoadImageTask = null;
        }
        mLoadImageTask = new LoadImageTask(this);
        mLoadImageTask.execute(page);
    }

    private class LoadImageTask extends AsyncTaskBase<Integer, Void, List<String>> {
        private int page_local;

        public LoadImageTask(Context context) {
            super(context);
        }
        @Override
        protected List<String> realDoInBackground(Integer... params) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            page_local = params[0];
            return Utils.generateImageList(page_local);
        }

        @Override
        protected void realOnPostExecute(List<String> result) {
            mSwipeRefreshLayout.setRefreshing(false);
            if (result != null) {
                if (page_local == 0) {
                    mImageAdapter.clearList();
                    mImageAdapter.setList(result);
                } else {
                    mImageAdapter.appendList(result);
                }
                page ++;
                if (result.size() < Const.DEFAULT_PAGESIZE) {
                    //setNoMoreData;
                }
            }

            if (mImageAdapter.getCount() == 0) {
                //setEmptyView();
            }
        }

        @Override
        protected void onError(Exception e) {
            mSwipeRefreshLayout.setRefreshing(false);
            if (mImageAdapter.getCount() == 0) {
                //setEmptyView
            } else {
                //Toast
            }
        }
    }

}
