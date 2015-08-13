package com.passerbywhu.study.touchstudy.pulltozoom;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.passerbywhu.study.R;

public class PullToZoomActivity extends Activity {
	MyPullToZoomListView listView;
//    PullToZoomListView listView;
    String[] dataSet = new String[] {"A Item", "B Item", "C Item", "D Item", "E Item", "F Item", "G Item", "H Item", "I Item", "J Item", "K Item", "L Item", "M Item", "N Item",
         "O Item", "P Item", "Q Item", "R Item", "S Item", "T Item", "U Item", "V Item", "W Item", "X Item", "Y Item", "Z Item"};
//    String[] dataSet = new String[0];
    TextView emptyView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pull_zoom);
		listView = (MyPullToZoomListView)findViewById(R.id.listview);
//        listView = (PullToZoomListView)findViewById(R.id.listview);
        emptyView = (TextView) findViewById(R.id.emptyView);
        listView.setEmptyView(emptyView);

//        listView.getHeaderView().setImageResource(R.drawable.splash01);
//        listView.getHeaderView().setScaleType(ImageView.ScaleType.CENTER);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataSet));
	}
}
