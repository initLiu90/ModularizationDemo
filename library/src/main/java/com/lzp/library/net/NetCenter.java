package com.lzp.library.net;

import com.lzp.library.net.api.ApiService;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetCenter {
    private volatile static NetCenter sInstance;
    private Retrofit mRetrofit;
    private ApiService mApiService;

    public static NetCenter getInstance() {
        if (sInstance == null) {
            synchronized (NetCenter.class) {
                if (sInstance == null) {
                    sInstance = new NetCenter();
                }
            }
        }
        return sInstance;
    }

    private NetCenter() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(Config.connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(Config.readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(Config.writeTimeout, TimeUnit.MILLISECONDS)
                .build();
        okHttpClient.dispatcher().setMaxRequests(okHttpClient.dispatcher().getMaxRequests() * 2);
        okHttpClient.dispatcher().setMaxRequestsPerHost(okHttpClient.dispatcher().getMaxRequestsPerHost() * 2);


        mRetrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                //接口中通过@url指定了请求地址，所以这里baseurl没有用了，但是为了避免
                //Retrofit.java
                //public Retrofit build() {
                //      if (baseUrl == null) {
                //        throw new IllegalStateException("Base URL required.");
                //      }
                //  .....
                //}
                .baseUrl("http://square.github.io")
                .build();
        mApiService = mRetrofit.create(ApiService.class);
    }

    public <T> Observable<T> getApiService(final RequestParams<T> params) {
        Observable<T> observable = Observable.just(0)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .flatMap(new Function<Integer, ObservableSource<ResponseBody>>() {
                    @Override
                    public ObservableSource<ResponseBody> apply(Integer integer) throws Exception {
                        Observable<ResponseBody> apiObservable = null;
                        if (RequestParams.HttpMethod.GET.equals(params.httpMethod)) {//get
                            apiObservable = mApiService.get(params.url, params.headers, params.params);
                        } else if (RequestParams.HttpMethod.POST.equals(params.httpMethod)
                                && RequestParams.ContentType.APPLICATION_FORM_URLENCODED.equals(params.contentType)) {//post application/x-www-form-urlencoded
                            apiObservable = mApiService.post(params.url, params.headers, params.params);
                        } else if (RequestParams.HttpMethod.POST.equals(params.httpMethod)
                                && RequestParams.ContentType.APPLICATION_JSON.equals(params.contentType)) {//post application/json
                            RequestBody requestBody = RequestBody.create(MediaType.parse(params.contentType), params.converter.requestConvert(params.params));
                            apiObservable = mApiService.post(params.url, params.headers, requestBody);
                        }
                        return apiObservable;
                    }
                })
                .map(new Function<ResponseBody, T>() {
                    @Override
                    public T apply(ResponseBody responseBody) throws Exception {
                        return (T) params.converter.responseConvert(responseBody.bytes());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }


    private static class Config {
        public static long connectTimeout = 10 * 1000;

        public static long readTimeout = 10 * 1000;

        public static long writeTimeout = 10 * 1000;
    }
}
