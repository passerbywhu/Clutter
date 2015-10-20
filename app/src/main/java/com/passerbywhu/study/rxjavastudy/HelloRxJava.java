package com.passerbywhu.study.rxjavastudy;

import com.passerbywhu.study.common.utils.Logger;
import com.passerbywhu.study.common.utils.Utils;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by wuwenchao3 on 2015/10/14.
 */
public class HelloRxJava {
    Observable<String> myObservable = Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> sub) {
            sub.onNext("Hello, world!");
            sub.onCompleted();
        }
    });

    Subscriber<String> mySubscriber = new Subscriber<String>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onNext(String s) {
            Logger.e(s);
            Utils.showToast(s);
        }
    };

    Observable<String> oneTimeObservable = Observable.just("Hello, world!");  //just 发出一个事件就结束

    Action1<String> onNextAction = new Action1<String>() {
        @Override
        public void call(String s) {
            System.out.println(s);
        }
    };

    public void map() {   //RxJava的Map把一件事转换为另一件事
        Observable.just("Hello, world!").map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                return s + "-Dan";
            }
        }).subscribe(a -> Utils.showToast(a));
    }

    //lamda只对拥有一个方法的接口才有用。
    public void lamdaMap() {
//        Observable.just("Hello, world!").map(s -> s + "-Dan").subscribe(a -> Utils.showToast(a));
        Observable.just("Hello, world!").map(s -> s.hashCode()).map(i -> Integer.toString(i)).subscribe(s->Utils.showToast(s));
    }

    public void mapNewObservable() {
        Observable.just("Hello, world!").map(s -> s.hashCode()).subscribe(i -> Utils.showToast(i + ""));
    }


    public void subscribe() {
        myObservable.subscribe(mySubscriber);
//        myObservable.subscribe(onNextAction);
    }

    public void lamdaSubscribe() {
        Observable.just("Hello, World! -Dan").subscribe(s -> Utils.showToast(s));
    }
}
