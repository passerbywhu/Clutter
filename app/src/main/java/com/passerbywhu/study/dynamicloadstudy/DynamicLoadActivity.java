package com.passerbywhu.study.dynamicloadstudy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.passerbywhu.study.common.activity.BaseActivity;

/**
 * Created by wuwenchao3 on 2015/7/8.
 */
public class DynamicLoadActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected View createChildView() {
        LinearLayout layout = new LinearLayout(this);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        layout.setBackgroundColor(Color.parseColor("#000000"));
        Button button = new Button(this);
        button.setText("button");
        layout.addView(button, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DynamicLoadActivity.this, "you clicked button",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DynamicLoadActivity.this, ProxyActivity.class);
                intent.putExtra(ProxyActivity.EXTRA_DEX_PATH, "/mnt/sdcard/dynamic/plugin.apk");
                startActivity(intent);
            }
        });
        return layout;
    }
}
