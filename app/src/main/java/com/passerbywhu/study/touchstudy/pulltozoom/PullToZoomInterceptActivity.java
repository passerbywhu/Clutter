package com.passerbywhu.study.touchstudy.pulltozoom;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.passerbywhu.study.R;

public class PullToZoomInterceptActivity extends Activity {
    private ImageView mHeaderView;
    private ListView mListView;
    String[] dataSet = new String[] {"A Item", "B Item", "C Item", "D Item", "E Item", "F Item", "G Item", "H Item", "I Item", "J Item", "K Item", "L Item", "M Item", "N Item",
            "O Item", "P Item", "Q Item", "R Item", "S Item", "T Item", "U Item", "V Item", "W Item", "X Item", "Y Item", "Z Item"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_zoom_byintercept);
        mHeaderView = (ImageView) findViewById(R.id.headerView);
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataSet));
        mHeaderView.setImageResource(R.drawable.splash02);
        Drawable drawable = mHeaderView.getDrawable();
        int height = drawable.getMinimumHeight();
        mHeaderView.getLayoutParams().height = height / 3;
//        setContentView(R.layout.test_list_layout);
//        mListView = (ListView) findViewById(R.id.testList);
//        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataSet));
    }
}
