package com.passerbywhu.study.rxjavastudy;

/**
 * Created by wuwenchao3 on 2015/10/20.
 */
public class HelloRxJava4 {
    //如果你已经创建了自己的Handler，你可以使用HandlerThreadScheduler1将一个调度器链接到你的handler上。
    {
        //HandlerThreadScheduler1 找不到这个类
    }

    //接着要介绍的就是AndroidObservable，它提供了跟多的功能来配合Android的生命周期。
    // bindActivity()和bindFragment()方法默认使用AndroidSchedulers.mainThread()来执行观察者代码，
    // 这两个方法会在Activity或者Fragment结束的时候通知被观察者停止发出新的消息。
    {
        //找不到AndroidObservable
//        AndroidObservable.bindActivity(this, retrofitService.getImage(url))
//                .subscribeOn(Schedulers.io())
//                .subscribe(bitmap -> myImageView.setImageBitmap(bitmap);
    }

    //我自己也很喜欢AndroidObservable.fromBroadcast()方法，它允许你创建一个类似BroadcastReceiver的Observable对象。
    // 下面的例子展示了如何在网络变化的时候被通知到：
    {
        //找不到AndroidObservable
        //IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
//        AndroidObservable.fromBroadcast(context, filter)
//                .subscribe(intent -> handleConnectivityChange(intent));
    }

    //最后要介绍的是ViewObservable,使用它可以给View添加了一些绑定。如果你想在每次点击view的时候都收到一个事件，
    // 可以使用ViewObservable.clicks()，或者你想监听TextView的内容变化，可以使用ViewObservable.text()。
    {
        //找不到ViewObservable类
//        ViewObservable.clicks(mCardNameEditText, false)
//                .subscribe(view -> handleClick(view));
    }

    //@GET("/user/{id}/photo")
//    Observable<Photo> getUserPhoto(@Path("id") int id);
    {
//        RxJava和Retrofit的典型使用场景
//        Observable.zip(
//                service.getUserPhoto(id),
//                service.getPhotoMetadata(id),
//                (photo, metadata) -> createPhotoWithData(photo, metadata))
//                .subscribe(photoWithData -> showPhoto(photoWithData));
    }

    //defer
    {
//        private Object slowBlockingMethod() { ... }
//
//        public Observable<Object> newMethod() {
//        return Observable.defer(() -> Observable.just(slowBlockingMethod()));
    }

    //1.在configuration改变（比如转屏）之后继续之前的Subscription。
    //第一个问题的解决方案就是使用RxJava内置的缓存机制，
    // 这样你就可以对同一个Observable对象执行unsubscribe/resubscribe，
    // 却不用重复运行得到Observable的代码。cache() (或者 replay())会继续执行网络请求（甚至你调用了unsubscribe也不会停止）。
    // 这就是说你可以在Activity重新创建的时候从cache()的返回值中创建一个新的Observable对象。
    {
//        Observable<Photo> request = service.getUserPhoto(id).cache();
//        Subscription sub = request.subscribe(photo -> handleUserPhoto(photo));
//
//// ...When the Activity is being recreated...
//        sub.unsubscribe();
//
//// ...Once the Activity is recreated...
//        request.subscribe(photo -> handleUserPhoto(photo));
    }
}
