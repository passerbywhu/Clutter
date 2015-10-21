package com.passerbywhu.study.rxjavastudy.callbackhell;

import com.passerbywhu.study.common.utils.Logger;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;


/**
 * Created by wuwenchao3 on 2015/10/21.
 * https://gist.github.com/benjchristensen/4677544 来自
 */
public class CallbackB {
    private static interface CallBack<T> {
        public void call(T t);
    }
    private static final class CallToRemoteServiceA implements Runnable {
        private final CallBack<String> callback;
        private CallToRemoteServiceA(CallBack<String> callback) {
            this.callback = callback;
        }
        @Override
        public void run() {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            callback.call("responseA");
        }
    }

    private static final class CallToRemoteServiceB implements Runnable {
        private final CallBack<String> callback;
        private CallToRemoteServiceB(CallBack<String> callback) {
            this.callback = callback;
        }
        @Override
        public void run() {
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            callback.call("responseB");
        }
    }

    private static final class CallToRemoteServiceC implements Runnable {
        private final CallBack<String> callback;
        private final String dependencyFromA;
        private CallToRemoteServiceC(CallBack<String> callback, String dependencyFromA) {
            this.callback = callback;
            this.dependencyFromA = dependencyFromA;
        }
        @Override
        public void run() {
            try {
                Thread.sleep(60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            callback.call("responseC_denpendOn_" + dependencyFromA);
        }
    }

    private static final class CallToRemoteServiceD implements Runnable {
        private final CallBack<String> callback;
        private final String dependencyFromB;
        private CallToRemoteServiceD(CallBack<String> callback, String dependencyFromB) {
            this.callback = callback;
            this.dependencyFromB = dependencyFromB;
        }
        @Override
        public void run() {
            try {
                Thread.sleep(140);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            callback.call("responseD_denpendOn_" + dependencyFromB);
        }
    }

    private static final class CallToRemoteServiceE implements Runnable {
        private final CallBack<String> callback;
        private final String dependencyFromB;
        private CallToRemoteServiceE(CallBack<String> callback, String dependencyFromB) {
            this.callback = callback;
            this.dependencyFromB = dependencyFromB;
        }
        @Override
        public void run() {
            try {
                Thread.sleep(55);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            callback.call("responseE_denpendOn_" + dependencyFromB);
        }
    }

    //f3依赖于f1, f4,f5依赖于f2

    public static void run() throws Exception {
        final ExecutorService executor = new ThreadPoolExecutor(4, 4, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
        final CountDownLatch latch = new CountDownLatch(3);
        final AtomicReference<String> f3Value = new AtomicReference<String>();
        final AtomicReference<String> f4Value = new AtomicReference<String>();
        final AtomicReference<String> f5Value = new AtomicReference<String>();

        try {
            executor.execute(new CallToRemoteServiceA(new CallBack<String>() {
                @Override
                public void call(String f1) {
                    executor.execute(new CallToRemoteServiceC(new CallBack<String>() {
                        @Override
                        public void call(String f3) {
                            f3Value.set(f3);
                            latch.countDown();
                        }
                    }, f1));
                }
            }));

            executor.execute(new CallToRemoteServiceB(new CallBack<String>() {
                @Override
                public void call(String f2) {
                    executor.execute(new CallToRemoteServiceD(new CallBack<String>() {
                        @Override
                        public void call(String f4) {
                            f4Value.set(f4);
                            latch.countDown();
                        }
                    }, f2));

                    executor.execute(new CallToRemoteServiceE(new CallBack<String>() {
                        @Override
                        public void call(String f5) {
                            f5Value.set(f5);
                            latch.countDown();
                        }
                    }, f2));
                }
            }));
            latch.await();
            //闭锁的作用
            //f3 f4 f5都计算出来以后，用f3 f4 f5计算一个新值
            Logger.e("f3Value f4Value f5Value = " + f3Value.get() + " " + f4Value.get() + " " + f5Value.get());
        } finally {
            executor.shutdown();
        }
    }
}
