package com.lzp.library.net;

import android.os.Looper;
import android.util.Log;

import com.lzp.library.net.api.ApiService;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Function;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 用法
 * ObservableNet.subscribeOn(Schedulers.io())
 * .unsubscribeOn(Schedulers.io())
 * .observeOn(AndroidSchedulers.mainThread())
 * .subscribe(new Observer<String>() {
 * .....
 * }
 *
 * @param <T>
 */
public class ObservableNet<T> extends Observable<T> implements Disposable {
    private RequestParams mRequestParams;
    private ApiService mApiService;
    private Function<? super byte[], ? super T> mResponseConvert;
    private Function<Map<String, String>, byte[]> mRequestConvert;
    private Observer<? super T> mObserver;

    private Call<ResponseBody> mCall;

    private final AtomicBoolean unsubscribed = new AtomicBoolean();

    public ObservableNet(ApiService apiSerive, RequestParams requestParams) {
        mRequestParams = requestParams;
        mApiService = apiSerive;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
//        Log.e("Test", "subscribeActual=" + Thread.currentThread().getName());
        if (!check(observer)) {
            return;
        }

        mObserver = observer;
        try {
            if (RequestParams.HttpMethod.GET.equals(mRequestParams.httpMethod)) {//get
                mCall = mApiService.get(mRequestParams.url, mRequestParams.headers, mRequestParams.params);
            } else if (RequestParams.HttpMethod.POST.equals(mRequestParams.httpMethod)
                    && RequestParams.ContentType.APPLICATION_FORM_URLENCODED.equals(mRequestParams.contentType)) {//post application/x-www-form-urlencoded
                mCall = mApiService.post(mRequestParams.url, mRequestParams.headers, mRequestParams.params);
            } else if (RequestParams.HttpMethod.POST.equals(mRequestParams.httpMethod)
                    && RequestParams.ContentType.APPLICATION_JSON.equals(mRequestParams.contentType)) {//post application/json
                RequestBody requestBody = RequestBody.create(MediaType.parse(mRequestParams.contentType), mRequestConvert
                        .apply(mRequestParams.params));
                mCall = mApiService.post(mRequestParams.url, mRequestParams.headers, requestBody);
            } else {
                mObserver.onSubscribe(Disposables.empty());
                mObserver.onError(new IllegalStateException("UnSupported request method:" + mRequestParams.httpMethod + " or request contentType:" + mRequestParams.contentType));
                return;
            }

            mObserver.onSubscribe(this);

            Response<ResponseBody> response = mCall.execute();
            if (!isDisposed()) {
                T data = (T) mResponseConvert.apply(response.body().bytes());
                mObserver.onNext(data);
            }
        } catch (Exception e) {
            if (!isDisposed()) {
                mObserver.onSubscribe(Disposables.empty());
                mObserver.onError(e);
            }
        }
    }

    /**
     * convert request params
     *
     * @param function
     * @return
     */
    public ObservableNet<T> requestConvert(Function<Map<String, String>, byte[]> function) {
        mRequestConvert = function;
        return this;
    }

    /**
     * convert http response
     *
     * @param function
     * @return
     */
    public ObservableNet<T> responseConvert(Function<? super byte[], ? super T> function) {
        mResponseConvert = function;
        return this;
    }

    @Override
    public void dispose() {
        if (unsubscribed.compareAndSet(false, true)) {
            mObserver = null;
            mCall.cancel();
            mCall = null;
        }
    }

    @Override
    public boolean isDisposed() {
        return unsubscribed.get();
    }

    private boolean check(Observer<? super T> observer) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            observer.onSubscribe(Disposables.empty());
            observer.onError(new IllegalStateException("Could not run on main thread"));
            return false;
        }
        if (mResponseConvert == null) {
            observer.onSubscribe(Disposables.empty());
            observer.onError(new IllegalStateException("ObservableNet should call responseConvert method before subscribe"));
            return false;
        }

        if (RequestParams.HttpMethod.POST.equals(mRequestParams.httpMethod)
                && RequestParams.ContentType.APPLICATION_JSON.equals(mRequestParams.contentType)) {//post application/json
            if (mRequestConvert == null) {
                observer.onSubscribe(Disposables.empty());
                observer.onError(new IllegalStateException("ObservableNet should call requestConvert method before subscribe"));
                return false;
            }
        }
        return true;
    }
}
