package com.passerbywhu.study.butterknifestudy;

import android.app.Application;

import com.passerbywhu.study.BuildConfig;

import butterknife.ButterKnife;

public class SimpleApp extends Application {
  @Override public void onCreate() {
    super.onCreate();
    ButterKnife.setDebug(BuildConfig.DEBUG);
  }
}
