package com.passerbywhu.study.rxjavastudy.callbackhell;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by wuwenchao3 on 2015/10/21.
 */
public class CallbackHellActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
//            CallbackB.run();
            ObservableB.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
