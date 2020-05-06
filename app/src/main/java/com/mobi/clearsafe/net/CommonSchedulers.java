package com.mobi.clearsafe.net;

import android.content.Context;

import com.mobi.clearsafe.base.BaseActivity;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.RxActivity;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragmentActivity;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CommonSchedulers {



    public static <T> ObservableTransformer<T,T> observableIO2Main(final Context context){
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                Observable<T> observable = upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
                return composeContext(context,observable);
            }
        };
    }


    private static <T> ObservableSource<T> composeContext(Context context, Observable<T> observable) {
        if(context instanceof BaseActivity) {
            return (ObservableSource<T>) observable.compose(((RxActivity) context).bindUntilEvent(ActivityEvent.DESTROY));
        }
        else if(context instanceof RxFragmentActivity){
            return (ObservableSource<T>) observable.compose(((RxFragmentActivity) context).bindUntilEvent(ActivityEvent.DESTROY));
        }else if(context instanceof RxAppCompatActivity){
            return (ObservableSource<T>) observable.compose(((RxAppCompatActivity) context).bindUntilEvent(ActivityEvent.DESTROY));
        }else {
            return observable;
        }
    }



}
