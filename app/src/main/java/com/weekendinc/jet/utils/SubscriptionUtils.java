package com.weekendinc.jet.utils;

import com.weekendinc.jet.model.pojo.ResultDefault;
import com.weekendinc.jet.network.ApiConstants;

import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.FuncN;
import rx.schedulers.Schedulers;

/**
 * Android-rX utility
 */
public class SubscriptionUtils {
    private SubscriptionUtils() {
    }

    /**
     * Subscribe a request with error handler
     *
     * @param observable
     * @param doOnError
     * @param doOnSuccess
     * @param <T>
     * @return
     */
    static public <T> Subscription subscribing(
            Observable<T> observable,
            Action1<Throwable> doOnError,
            Action1<T> doOnSuccess) {
        return observable
                .retry(ApiConstants.retry)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(doOnSuccess, doOnError);
    }

    /**
     * Subscribe a request with error handler
     *
     * @param observable
     * @param doOnError
     * @param doOnSuccess
     * @param onErrorReturn
     * @param <T>
     * @return
     */
    static public <T> Subscription subscribing(
            Observable<T> observable,
            Action1<Throwable> doOnError,
            Action1<T> doOnSuccess,
            Func1<Throwable, T> onErrorReturn) {
        return observable
                .retry(ApiConstants.retry)
                .subscribeOn(Schedulers.io())
                .onErrorReturn(onErrorReturn)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(doOnSuccess, doOnError);
    }

    /**
     * Subscribe a request with no error handler for silent request
     *
     * @param observable
     * @param subscribe
     * @param <T>
     * @return
     */
    static public <T> Subscription subscribe(final Observable<T> observable, Action1<T> subscribe) {
        return observable
                .retry(ApiConstants.retry)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscribe);
    }

    /**
     * For upload many images
     *
     * @param onError
     * @param onSuccess
     * @param observables
     * @return
     */
    static public Subscription subsUploadMany(
            Action1<Throwable> onError,
            Action1<ResultDefault> onSuccess,
            List<Observable<ResultDefault>> observables) {

        return Observable.zip(
                observables,
                new FuncN<ResultDefault>() {
                    @Override
                    public ResultDefault call(Object... args) {
                        return (ResultDefault) args[0];
                    }
                })
                .retry(ApiConstants.retry)
                .take(observables.size())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess, onError);


    }

    /**
     * Zipping request
     *
     * @param onError
     * @param onSuccess
     * @param observables
     * @return
     */
    static public Subscription subsZip(
            Action1<Throwable> onError,
            Action1<Object> onSuccess,
            List<Observable<?>> observables) {
        return Observable.zip(
                observables,
                new FuncN<Object>() {
                    @Override
                    public Object call(Object... args) {
                        return null;
                    }
                })
                .retry(ApiConstants.retry)
                .take(observables.size())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess, onError);
    }
}
