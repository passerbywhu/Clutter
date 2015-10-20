package com.passerbywhu.study.rxjavastudy;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.passerbywhu.study.R;

public class RxJavaActivity extends Activity {
    ImageView imgView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);
        imgView = (ImageView) findViewById(R.id.imgView);
        HelloRxJava rxJava = new HelloRxJava();
        rxJava.subscribe();
        HelloRxJava3 rxJava3 = new HelloRxJava3();
        rxJava3.setImage(imgView);
    }
}
