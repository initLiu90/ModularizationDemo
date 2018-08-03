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

    public <T> ObservableNet<T> getApiService(final RequestParams<T> params) {
        return new ObservableNet<>(mApiService, params);
    }


    private static class Config {
        public static long connectTimeout = 10 * 1000;

        public static long readTimeout = 10 * 1000;

        public static long writeTimeout = 10 * 1000;
    }
}
