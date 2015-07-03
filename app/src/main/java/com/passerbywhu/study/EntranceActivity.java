package com.passerbywhu.study;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.passerbywhu.study.contentproviderstudy.DictionaryProviderStudy;
import com.passerbywhu.study.dragstudy.DragSideMenuActivity;
import com.passerbywhu.study.dragstudy.GetViewTestActivity;
import com.passerbywhu.study.dragstudy.SimpleDragLayoutActivity;

import java.util.Arrays;
import java.util.List;

/**
 * Created by wuwenchao3 on 2015/6/9.
 */
public class EntranceActivity extends FragmentActivity {
    private ListView mListView;
    private List<String> activityList;
    private Toast toast;

    {
        String[] activityNames = {
                DragSideMenuActivity.class.getName(), SimpleDragLayoutActivity.class.getName(), GetViewTestActivity.class.getName(),
                DictionaryProviderStudy.class.getName(), MainActivity.class.getName()
        };
        activityList = Arrays.asList(activityNames);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);
        mListView = (ListView) findViewById(R.id.activityList);
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, activityList));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String activityName = (String) parent.getAdapter().getItem(position);
                Intent intent = null;
                try {
                    intent = new Intent(EntranceActivity.this, Class.forName(activityName));
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    toast.setText(activityName + " class not found");
                    toast.show();
                }
            }
        });
        toast = Toast.makeText(this, "", Toast.LENGTH_LONG);
    }
}
