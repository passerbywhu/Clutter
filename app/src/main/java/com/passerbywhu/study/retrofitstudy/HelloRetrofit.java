package com.passerbywhu.study.retrofitstudy;

import android.app.Activity;
import android.os.Bundle;

import com.passerbywhu.study.common.utils.Logger;
import com.passerbywhu.study.retrofitstudy.api.GitAPI;

import retrofit.RestAdapter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wuwenchao3 on 2015/10/21.
 */
public class HelloRetrofit extends Activity {
    String API = "https://api.github.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RestAdapter mRestAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setEndpoint(API).build();
        GitAPI mGitAPI = mRestAdapter.create(GitAPI.class);
//        mGitAPI.getFeed("abc", new Callback<GitModel>() {
//            @Override
//            public void success(GitModel gitModel, Response response) {
//                Logger.e(response.toString());
//                Logger.e("Github Name :" + gitModel.getName());
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                Logger.e(error.getMessage());
//            }
//        });
        mGitAPI.getFeed("abc").observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).subscribe(gitModel -> {
            Logger.e("Github Name : " + gitModel.getName());
        });
    }
}
