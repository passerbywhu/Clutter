package com.passerbywhu.study.rxjavastudy;

import com.passerbywhu.study.common.utils.Utils;

import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by wuwenchao3 on 2015/10/19.
 */
public class HelloRxJava2 {
    Observable<List<String>> query(String text) {
        Observable<List<String>> myObservable = Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                subscriber.onNext(Arrays.asList("A B C D E F G H I J K L M N".split(" ")));
                subscriber.onCompleted();
            }
        });
        return myObservable;
    }

    Subscriber<List<String>> mySubscriber = new Subscriber<List<String>>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onNext(List<String> strings) {
            for (String str : strings) {
                Utils.showToast(str);
            }
        }
    };

    Action1<List<String>> myAction = new Action1<List<String>>() {
        @Override
        public void call(List<String> strings) {
            for (String str : strings) {
                Utils.showToast(str);
            }
        }
    };

    public void subscribe() {
        query("abc").subscribe(mySubscriber);
        query("abc").subscribe(myAction);
        query("abc").subscribe(strings->{
            for (String str : strings) {
                Utils.showToast(str);
            }
        });

        //Observable.from()方法，它接收一个集合作为输入，然后每次输出一个元素给subscriber
//        Observable.from("url1 url2 url3".split(" ")).subscribe(url -> Utils.showToast(url.toString()));

        query("abc").subscribe(urls-> {                  //前面的例子都是通过之间多个map来转换数据的。这里用了多个subscribe。。。
            Observable.from(urls).subscribe(url->Utils.showToast(url));
        });
        //虽然去掉了for each循环，但是代码依然看起来很乱。多个嵌套的subscription不仅看起来很丑，难以修改，更严重的是它会破坏某些我们现在还没有讲到的RxJava的特性。
        //救星来了,他就是flatMap()。Observable.flatMap()接收一个Observable的输出作为输入，同时输出另外一个Observable。直接看代码：
        query("Hello, world!").flatMap(new Func1<List<String>, Observable<String>>() {
            @Override
            public Observable<String> call(List<String> urls) {
                return Observable.from(urls);
            }
        }).subscribe(url -> Utils.showToast(url));

        //lamda简化
        query("Hello, world!").flatMap(urls->Observable.from(urls)).subscribe(url->Utils.showToast(url));

        //接着前面的例子，现在我不想打印URL了，而是要打印收到的每个网站的标题。问题来了，我的方法每次只能传入一个URL，
        // 并且返回值不是一个String，而是一个输出String的Observabl对象。使用flatMap()可以简单的解决这个问题。
        query("Hello, world!").flatMap(urls->Observable.from(urls)).flatMap(new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String url) {
                return getTitle(url);           //这个getTitle是个Observable，也就是说可以是异步线程去服务端获取
            }
        }).subscribe(title -> Utils.showToast(title));

        //lamda简化
        query("Hello, world!").flatMap(urls->Observable.from(urls)).flatMap(url->getTitle(url)).subscribe(title->Utils.showToast(title));
        //不止这些，我还将两个API的调用组合到一个链式调用中了。我们可以将任意多个API调用链接起来。
        // 大家应该都应该知道同步所有的API调用，然后将所有API调用的回调结果组合成需要展示的数据是一件多么蛋疼的事情。
        // 这里我们成功的避免了callback hell（多层嵌套的回调，导致代码难以阅读维护）。现在所有的逻辑都包装成了这种简单的响应式调用。

        //过滤掉getTitle()返回的null
        query("Hello, World!").flatMap(urls->Observable.from(urls)).flatMap(url->getTitle(url)).
                filter(title -> title != null).subscribe(title->Utils.showToast(title));

        //只想要最多5个结果   take(5)
        query("Hello, World!").flatMap(urls->Observable.from(urls)).flatMap(url->getTitle(url)).
                filter(title->title!=null).take(5).subscribe(title -> Utils.showToast(title));

        //doOnNext()允许我们在每次输出一个元素之前做一些额外的事情，比如这里的保存标题。
        query("Hello World!").flatMap(urls->Observable.from(urls)).flatMap(url->getTitle(url)).filter(title->title != null)
                .take(5).doOnNext(title->saveTitle(title)).subscribe(title->Utils.showToast(title));

        //看到这里操作数据流是多么简单了么。你可以添加任意多的操作，并且不会搞乱你的代码。
        //将一系列的操作符链接起来就可以完成复杂的逻辑。代码被分解成一系列可以组合的片段。
        // 这就是响应式函数编程的魅力。用的越多，就会越多的改变你的编程思维。
    }

    public Observable<String> getTitle(String url) {
        return Observable.just(url + " title");
    }

    public void saveTitle(String title) {

    }
}
