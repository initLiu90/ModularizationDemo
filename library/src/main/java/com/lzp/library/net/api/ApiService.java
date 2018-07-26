package com.lzp.library.net.api;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface ApiService {
    @GET
    <R> Observable<ResponseBody> get(@Url String url, @HeaderMap Map<String, String> headers, @QueryMap Map<String, String> querys);

    @FormUrlEncoded
    @POST
    <R> Observable<ResponseBody> post(@Url String url, @HeaderMap Map<String, String> headers, @FieldMap Map<String, String> fields);

    @POST
    <R> Observable<ResponseBody> post(@Url String url, @HeaderMap Map<String, String> headers, @Body RequestBody request);
}
