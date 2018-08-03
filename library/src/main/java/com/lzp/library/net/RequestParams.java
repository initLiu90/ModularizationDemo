package com.lzp.library.net;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @param <T> http response返回的数据类型经过{@link Converter#responseConvert(byte[])}转换后的类型
 */
public class RequestParams<T> {
    public final String httpMethod;
    public final String url;
    public final Map<String, String> headers;
    public final Map<String, String> params;
    public final String contentType;
    public final Converter<? super T> converter;

    public RequestParams(Builder<T> builder) {
        this.httpMethod = builder.httpMethod;
        this.url = builder.url;
        this.headers = builder.headers;
        this.params = builder.params;
        this.converter = builder.converter;
        this.contentType = builder.contentType;
    }


    public static <T> Builder<T> creat(Converter<T> converter) {
        return new Builder<>(converter);
    }

    public static <T> Builder<T> creat(Class<T> responseClazz) {
        return new Builder<>();
    }

    public static class Builder<T> {
        private String httpMethod;
        private Map<String, String> headers = new HashMap<>();
        private Map<String, String> params = new HashMap<>();
        private String url;

        private Converter<? super T> converter;
        private String contentType = ContentType.APPLICATION_FORM_URLENCODED;

        private Builder(Converter<? super T> converter) {
            this.converter = converter;
        }

        private Builder() {
        }

        /**
         * http请求类型{@link HttpMethod}
         * @param httpMethod {@link HttpMethod}
         * @return
         */
        public Builder<T> method(String httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        public Builder<T> addHeader(String key, String value) {
            headers.put(key, value);
            return this;
        }

        public Builder<T> addHeader(Map<String, String> headers) {
            this.headers.putAll(headers);
            return this;
        }

        public Builder<T> addParam(String key, String value) {
            this.params.put(key, value);
            return this;
        }

        public Builder<T> addParam(Map<String, String> params) {
            this.params.putAll(params);
            return this;
        }

        /**
         * 请求地址
         * @param url
         * @return
         */
        public Builder<T> url(String url) {
            this.url = url;
            return this;
        }

        /**
         * 请求类型 {@link ContentType}
         * @param contentType {@link ContentType}
         * @return
         */
        public Builder<T> contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        /**
         * create Requestparams
         * @return
         */
        public RequestParams<T> build() {
            if (TextUtils.isEmpty(url))
                throw new RuntimeException("url should not be null");
            if (TextUtils.isEmpty(httpMethod))
                throw new RuntimeException("httpMethod should not be null");

            return new RequestParams(this);
        }
    }

    public interface HttpMethod {
        String GET = "Get";
        String POST = "Post";
    }

    public interface ContentType {
        String APPLICATION_JSON = "application/json; charset=UTF-8";
        String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";
    }
}
