package com.passerbywhu.study.common.asynctask;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

/**
 * Created by wuwenchao3 on 2015/5/18.
 */
public abstract class AsyncTaskBase<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    protected WeakReference<Context> mWeakReference;
    private Exception mException;

    public AsyncTaskBase(Context context) {
        mWeakReference = new WeakReference<Context>(context){};
    }

    protected abstract Result realDoInBackground(Params... params);

    protected abstract void realOnPostExecute(Result result);

    protected void realOnCancelled() {}

    protected void realOnProgressUpdate(Progress... values) {}

    protected abstract void onError(Exception e);

    private boolean isActivityFinishing() {
        return mWeakReference == null ||mWeakReference.get() == null || (mWeakReference.get() instanceof Activity && ((Activity) mWeakReference.get()).isFinishing());
    }

    @Override
    @Deprecated
    protected Result doInBackground(Params... params) {
        try {
            return realDoInBackground(params);
        } catch (Exception e) {
            e.printStackTrace();
            mException = e;
            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (isActivityFinishing()) {
            return;
        }
    }

    @Override
    @Deprecated
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);

        if (isActivityFinishing()) {
            return ;
        }

        if (mException != null) {
            onError(mException);
        } else {
            realOnPostExecute(result);
        }
    }

    @Override
    @Deprecated
    protected void onProgressUpdate(Progress... values) {
        super.onProgressUpdate(values);
        if (isActivityFinishing()) {
            return;
        }
        realOnProgressUpdate(values);
    }

    @Override
    @Deprecated
    protected void onCancelled() {
        super.onCancelled();
        if (isActivityFinishing()) {
            return;
        }
        realOnCancelled();
    }
}
