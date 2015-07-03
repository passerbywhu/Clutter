package com.passerbywhu.study.singletonstudy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.passerbywhu.study.R;
import com.passerbywhu.study.common.utils.Utils;

/**
 * Created by wuwenchao3 on 2015/6/17.
 */
public class SingletonWeakTest extends Activity {
    private SingletonClass mSingletonClass;
    private Button getInstanceBtn;
    private Button releaseInstanceBtn;
    private Button showInstanceCountBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singleton_weak_activity);
        getInstanceBtn = (Button) findViewById(R.id.getInstanceBtn);
        releaseInstanceBtn = (Button) findViewById(R.id.releaseInstanceBtn);
        showInstanceCountBtn = (Button) findViewById(R.id.showInstanceCountBtn);
        getInstanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSingletonClass = SingletonClass.getInstance();
            }
        });
        releaseInstanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSingletonClass = null;
            }
        });
        showInstanceCountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showToast("instance count = " + SingletonClass.instanceCount);
            }
        });
    }
}
