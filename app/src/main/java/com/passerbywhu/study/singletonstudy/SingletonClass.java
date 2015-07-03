package com.passerbywhu.study.singletonstudy;

import java.lang.ref.WeakReference;

/**
 * Created by wuwenchao3 on 2015/6/17.
 */
public class SingletonClass {
    private volatile static WeakReference<SingletonClass> instanceRef;
    public static int instanceCount = 0;

    private SingletonClass() {

    }

    public static SingletonClass getInstance() {
        if (instanceRef == null || instanceRef.get() == null) {
            synchronized (SingletonClass.class) {
                if (instanceRef == null || instanceRef.get() == null) {
                    instanceRef = new WeakReference<SingletonClass>(new SingletonClass());
                    instanceCount ++;
                }
            }
        }
        return instanceRef.get();
    }
}
