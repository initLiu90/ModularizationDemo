package com.lzp.library.net;

import com.lzp.library.net.api.ApiService;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class NetCenter {
    private volatile static NetCenter sInstance;
    private Retrofit mRetrofit;
    private ApiService mApiService;

    public static NetCenter getInstance(Config config) {
        if (sInstance == null) {
            synchronized (NetCenter.class) {
                if (sInstance == null) {
                    sInstance = new NetCenter(config);
                }
            }
        }
        return sInstance;
    }

    private NetCenter() {
    }

    private NetCenter(Config config) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(config.connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(config.readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(config.writeTimeout, TimeUnit.MILLISECONDS)
                .build();
        okHttpClient.dispatcher().setMaxRequests(okHttpClient.dispatcher().getMaxRequests() * 2);
        okHttpClient.dispatcher().setMaxRequestsPerHost(okHttpClient.dispatcher().getMaxRequestsPerHost() * 2);


        mRetrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        mApiService = mRetrofit.create(ApiService.class);
    }

    public <R> Observable<R> createObservable(final RequestParams params) {
        Observable<ResponseBody> observable = null;

        if (RequestParams.HttpMethod.GET.equals(params.httpMethod)) {//get
            observable = mApiService.get(params.url, params.headers, params.params);
        } else if (RequestParams.HttpMethod.POST.equals(params.httpMethod)) {//post
            observable = mApiService.post(params.url, params.headers, params.params);
        }

        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, R>() {
                    @Override
                    public R apply(ResponseBody responseBody) throws Exception {
                        return (R) params.converter.convert(responseBody.bytes());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static class Config {
        public long connectTimeout = 10 * 1000;

        public long readTimeout = 10 * 1000;

        public long writeTimeout = 10 * 1000;
    }
}
