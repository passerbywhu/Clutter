package com.passerbywhu.study.touchstudy.pulltozoom;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.passerbywhu.study.R;

public class AnimatePullToZoomInterceptActivity extends Activity {
    private ImageView mHeaderView;
    private ListView mListView;
    String[] dataSet = new String[] {"A Item", "B Item", "C Item", "D Item", "E Item", "F Item", "G Item", "H Item", "I Item", "J Item", "K Item", "L Item", "M Item", "N Item",
            "O Item", "P Item", "Q Item", "R Item", "S Item", "T Item", "U Item", "V Item", "W Item", "X Item", "Y Item", "Z Item"};
    private Toast toast;
    
    private TextView title;
    private TextView subTitle;
    private TextView totalPage;
    private AnimatePullToZoomInterceptLayout rootLayout;
    private int title_originalTopMargin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animate_pull_zoom_byintercept);
        rootLayout = (AnimatePullToZoomInterceptLayout) findViewById(R.id.rootLayout);
        title = (TextView) findViewById(R.id.title);
        title_originalTopMargin = ((MarginLayoutParams) title.getLayoutParams()).topMargin;
        subTitle = (TextView) findViewById(R.id.subTitle);
        totalPage = (TextView) findViewById(R.id.totalPage);
        
        rootLayout.setOnAnimationListener(new AnimatePullToZoomInterceptLayout.OnAnimationListener() {
			@Override
			public void onFirstUpAnimation(float fraction, int distance) {
				MarginLayoutParams titleParams = (MarginLayoutParams) title.getLayoutParams();
				if (distance > 0) {
					titleParams.topMargin = title_originalTopMargin + distance;
				} else {
					titleParams.topMargin = title_originalTopMargin;
				}
				float alpha = (float) (1.0 - fraction * 1.0);
				subTitle.setAlpha(alpha);
				totalPage.setAlpha(alpha);
			}
			
			@Override
			public void onFirstDownAnimation(float fraction, int distance) {
				MarginLayoutParams titleParams = (MarginLayoutParams) title.getLayoutParams();
				if (distance > 0) {
					titleParams.topMargin = title_originalTopMargin + distance;
				} else {
					titleParams.topMargin = title_originalTopMargin;
				}
				float alpha = fraction;
				subTitle.setAlpha(alpha);
				totalPage.setAlpha(alpha);
			}

			@Override
			public void onSecondUpAnimation(int distance) {
				MarginLayoutParams titleParams = (MarginLayoutParams) title.getLayoutParams();
				titleParams.topMargin = title_originalTopMargin + distance;
			}

			@Override
			public void onSecondDownAnimation(int distance) {
				MarginLayoutParams titleParams = (MarginLayoutParams) title.getLayoutParams();
				titleParams.topMargin = title_originalTopMargin + distance;
//				Log.e("hehe", "topMargin = " + titleParams.topMargin + " augment = " + augment);
			}
		});
        mHeaderView = (ImageView) findViewById(R.id.headerView);
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataSet));
        toast = Toast.makeText(this, "", Toast.LENGTH_LONG);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toast.setText(position + " clicked");
                toast.show();
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                toast.setText(position + " long clicked");
                toast.show();
                return true;
            }
        });
    }
}
