package com.passerbywhu.study.daggerstudy.coffee;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.passerbywhu.study.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by wuwenchao3 on 2015/6/18.
 */
public class CoffeeAppActivity extends Activity {
    @InjectView(R.id.infoArea)
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee_app);
        ButterKnife.inject(this);
        CoffeeApp.Coffee coffee = DaggerCoffeeApp_Coffee.builder().build();
        mTextView.setText(coffee.maker().brew());
    }
}
