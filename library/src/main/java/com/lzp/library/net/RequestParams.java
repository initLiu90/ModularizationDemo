package com.lzp.library.net;

import java.util.HashMap;
import java.util.Map;

import retrofit2.http.Url;

public class RequestParams<R> {
    public final String httpMethod;
//    public final long connectTimeout;
//    public final long readTimeout;
//    public final long writeTimeout;
    public final String url;
    public final Map<String, String> headers;
    public final Map<String, String> params;
    public final Convert<? super R> converter;

    public RequestParams(Builder builder) {
        this.httpMethod = builder.httpMethod;
//        this.connectTimeout = builder.connectTimeout;
//        this.readTimeout = builder.readTimeout;
//        this.writeTimeout = builder.writeTimeout;
        this.url = builder.url;
        this.headers = builder.headers;
        this.params = builder.params;
        this.converter = builder.converter;
    }





    public static class Builder<R> {
        private String httpMethod;
        private Map<String, String> headers = new HashMap<>();
        private Map<String, String> params = new HashMap<>();

        private long connectTimeout = 10 * 1000;

        private long readTimeout = 10 * 1000;

        private long writeTimeout = 10 * 1000;

        private String url;

        private Convert<? super R> converter;

        public Builder(String url, String httpMethod) {
            this.url = url;
            this.httpMethod = httpMethod;
        }

        private Builder() {
        }

        /**
         * @param httpMethod {@link HttpMethod}
         * @return
         */
        public Builder<R> method(String httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        public Builder<R> addHeader(String key, String value) {
            headers.put(key, value);
            return this;
        }

        public Builder<R> addHeader(Map<String, String> headers) {
            this.headers.putAll(headers);
            return this;
        }

        public Builder<R> addParam(String key, String value) {
            this.params.put(key, value);
            return this;
        }

        public Builder<R> addParam(Map<String, String> params) {
            this.params.putAll(params);
            return this;
        }

//        /**
//         * 建立连接超时时间
//         *
//         * @param connectTimeout Millisecond
//         * @return
//         */
//        public Builder<R> connectTimeout(long connectTimeout) {
//            this.connectTimeout = connectTimeout;
//            return this;
//        }
//
//        /**
//         * @param readTimeout Millisecond
//         * @return
//         */
//        public Builder<R> readTimeout(long readTimeout) {
//            this.readTimeout = readTimeout;
//            return this;
//        }
//
//        /**
//         * @param writeTimeout Millisecond
//         * @return
//         */
//        public Builder<R> writeTimeout(long writeTimeout) {
//            this.writeTimeout = writeTimeout;
//            return this;
//        }

        public Builder<R> url(String url) {
            this.url = url;
            return this;
        }

        public Builder<R> converter(Convert<? super R> converter) {
            this.converter = converter;
            return this;
        }

        public RequestParams<R> build() {
            return new RequestParams(this);
        }
    }

    public interface HttpMethod {
        String GET = "Get";
        String POST = "Post";
    }
}
