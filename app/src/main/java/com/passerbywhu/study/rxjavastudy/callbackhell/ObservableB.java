package com.passerbywhu.study.rxjavastudy.callbackhell;

import com.passerbywhu.study.common.utils.Logger;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wuwenchao3 on 2015/10/21.
 */
public class ObservableB {
    static Observable<String> CallToRemoteServiceA() {
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                subscriber.onNext("responseA");
                subscriber.onCompleted();
            }
        });
        return observable;
    }

    static Observable<String> CallToRemoteServiceB() {
        Observable<String> observable = Observable.create(subscriber -> {
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            subscriber.onNext("responseB");
            subscriber.onCompleted();
        });
        return observable;
    }

    static Observable<String> CallToRemoteServiceC(String dependencyFromA) {
        Observable<String> observable = Observable.create(subscriber -> {
            try {
                Thread.sleep(60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            subscriber.onNext("responseC_denpendOn_" + dependencyFromA);
            subscriber.onCompleted();
        });
        return observable;
    }

    static Observable<String> CallToRemoteServiceD(String dependencyFromB) {
        Observable<String> observable = Observable.create(subscriber -> {
            try {
                Thread.sleep(140);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            subscriber.onNext("responseD_dependOn_" + dependencyFromB);
            subscriber.onCompleted();
        });
        return observable;
    }

    static Observable<String> CallToRemoteServiceE(String dependencyFromB) {
        Observable<String> observable = Observable.create(subscriber -> {
            try {
                Thread.sleep(55);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            subscriber.onNext("responseE_dependOn_" + dependencyFromB);
            subscriber.onCompleted();
        });
        return observable;
    }

    public static void run() {
//        Observable<String> f3 = CallToRemoteServiceA().flatMap(f1 -> CallToRemoteServiceC(f1));
//        Observable<String> f4_merge_f5 = CallToRemoteServiceB().flatMap(f2 ->
//            CallToRemoteServiceD(f2).mergeWith(CallToRemoteServiceE(f2))
//        );
//        f3.mergeWith(f4_merge_f5).observeOn(Schedulers.io()).
//                subscribeOn(AndroidSchedulers.mainThread()).subscribe(value-> Logger.e(value+"\n"));

        CallToRemoteServiceA().flatMap(f1->CallToRemoteServiceC(f1)).zipWith(
                CallToRemoteServiceB().flatMap(f2->CallToRemoteServiceD(f2).zipWith(
                        CallToRemoteServiceE(f2), (f4, f5) -> f4 + " " + f5)), (f3, f4_merge_f5) -> f3 + " " + f4_merge_f5
        ).observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).subscribe(result-> Logger.e(result));
    }
}
