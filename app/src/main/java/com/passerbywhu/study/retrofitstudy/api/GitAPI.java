package com.passerbywhu.study.retrofitstudy.api;

import com.passerbywhu.study.retrofitstudy.mode.GitModel;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by wuwenchao3 on 2015/10/21.
 */
public interface GitAPI {
    @GET("/users/{user}")
    public void getFeed(@Path("user") String user, Callback<GitModel> response);

    @GET("/users/{user}")
    public Observable<GitModel> getFeed(@Path("user") String user);
}
