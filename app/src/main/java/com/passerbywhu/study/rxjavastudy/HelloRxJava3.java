package com.passerbywhu.study.rxjavastudy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.widget.ImageView;

import com.passerbywhu.study.common.utils.Logger;
import com.passerbywhu.study.common.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wuwenchao3 on 2015/10/19.
 */
public class HelloRxJava3 {
//      1.只要有异常发生onError()一定会被调用
//     这极大的简化了错误处理。只需要在一个地方处理错误即可以。
//     2.操作符不需要处理异常
//     将异常处理交给订阅者来做，Observerable的操作符调用链中一旦有一个抛出了异常，就会直接执行onError()方法。
//     3.你能够知道什么时候订阅者已经接收了全部的数据。
//     知道什么时候任务结束能够帮助简化代码的流程。（虽然有可能Observable对象永远不会结束）
//
//     我觉得这种错误处理方式比传统的错误处理更简单。传统的错误处理中，通常是在每个回调中处理错误。
//     这不仅导致了重复的代码，并且意味着每个回调都必须知道如何处理错误，你的回调代码将和调用者紧耦合在一起。
//     比如多个API链的情况。

    //每一个Observerable对象在终结的时候都会调用onCompleted()或者onError()方法，所以Demo中会打印”Completed!”或者”Ouch!”。
    void subscribe() {
        Observable.just("Hello, world!").map(s->potentialException(s)).map(s->anotherPotentialExceptions(s)).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Utils.showToast("Completed!");
            }

            @Override
            public void onError(Throwable e) {
                Utils.showToast("Ouch!");
            }

            @Override
            public void onNext(String s) {
                Utils.showToast(s);
            }
        });
    }

    public String potentialException(String s) throws IllegalArgumentException {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        return "";
    }

    public String anotherPotentialExceptions(String s) throws IllegalArgumentException {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        return "";
    }


    private static final String IMG_URL = "http://ss.bdimg.com/static/superman/img/logo/bd_logo1_31bdc765.png";

    //调度器
    //使用RxJava，你可以使用subscribeOn()指定观察者代码运行的线程，使用observerOn()指定订阅者运行的线程：
    public Observable<Bitmap> retrieveImage(final String imgUrl) {
//        Observable<Bitmap> myObservable = Observable.create(new Observable.OnSubscribe<Bitmap>() {
//            @Override
//            public void call(Subscriber<? super Bitmap> subscriber) {
//                //从网络下载网络图片
//                Bitmap result = null;
//                try {
//                    if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
//                        Logger.e("retrieveImage in Main Thread");
//                    } else {
//                        Logger.e("retrieveImage in Non-Main Thread");
//                    }
//                    URL url = new URL(imgUrl);
//                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                    InputStream in = urlConnection.getInputStream();
//                    result = BitmapFactory.decodeStream(in);
//                } catch (Exception e) {
//                    throw new IllegalArgumentException();
//                }
//                subscriber.onNext(result);
//                subscriber.onCompleted();
//            }
//        });
        //lamda 简化
        Observable<Bitmap> myObservable = Observable.create(subscriber->{
            //从网络下载网络图片
            Bitmap result = null;
            try {
                if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
                    Logger.e("retrieveImage in Main Thread");
                } else {
                    Logger.e("retrieveImage in Non-Main Thread");
                }
                URL url = new URL(imgUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                result = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                throw new IllegalArgumentException();
            }
            subscriber.onNext(result);
            subscriber.onCompleted();
        });
        return myObservable;
    }

    //使用RxJava，你可以使用subscribeOn()指定Observable代码运行的线程，使用observerOn()指定subscriber运行的线程：
    //这个例子中获取图片的代码应该是在后台线程运行，然后回到前台线程来设置ImageView。
    public void setImage(ImageView imgView) {
        Subscriber<Bitmap> subscriber = new Subscriber<Bitmap>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                PrintWriter pw = new PrintWriter(bos);
                e.printStackTrace(pw);
                pw.close();
                Logger.e("error " + bos.toString());
            }

            @Override
            public void onNext(Bitmap bitmap) {
                imgView.setImageBitmap(bitmap);
            }
        };
        //订阅
        Subscription subscription = retrieveImage(IMG_URL).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
//        subscription.unsubscribe();
        //RxJava的另外一个好处就是它处理unsubscribing的时候，会停止整个调用链。
        // 如果你使用了一串很复杂的操作符，调用unsubscribe将会在他当前执行的地方终止。不需要做任何额外的工作！
    }
}
