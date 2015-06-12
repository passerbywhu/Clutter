package com.passerbywhu.study.dragstudy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.passerbywhu.study.R;

/**
 * Created by wuwenchao3 on 2015/5/13.
 */
public class SimpleDragLayoutActivity extends Activity {
    private Button hideBtn;
    private SimpleDragLayout mSimpleDragLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_simple_draglayout);
        mSimpleDragLayout = (SimpleDragLayout) findViewById(R.id.simpleDragLayout);

        hideBtn = (Button) findViewById(R.id.hideButton);
        hideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SimpleDragLayoutActivity.this, "hideBtn clicked", Toast.LENGTH_SHORT).show();
                mSimpleDragLayout.restore();
            }
        });
    }
}
