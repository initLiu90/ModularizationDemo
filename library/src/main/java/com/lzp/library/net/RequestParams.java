package com.lzp.library.net;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

public class RequestParams<I, O> {
    public final String httpMethod;
    public final String url;
    public final Map<String, String> headers;
    public final Map<String, String> params;
    public final String contentType;
    public final Converter<? super I, ? super O> converter;
    public final I requestBody;

    public RequestParams(Builder<I, O> builder) {
        this.httpMethod = builder.httpMethod;
        this.url = builder.url;
        this.headers = builder.headers;
        this.params = builder.params;
        this.converter = builder.converter;
        this.contentType = builder.contentType;
        this.requestBody = builder.requestBody;
    }


    public static <I, O> Builder<I, O> creat(Converter<I, O> converter) {
        return new Builder<>(converter);
    }

    public static class Builder<I, O> {
        private String httpMethod;
        private Map<String, String> headers = new HashMap<>();
        private Map<String, String> params = new HashMap<>();
        private String url;

        private Converter<? super I, ? super O> converter;
        private String contentType = ContentType.APPLICATION_FORM_URLENCODED;
        private I requestBody;

        private Builder(Converter<? super I, ? super O> converter) {
            this.converter = converter;
        }

        private Builder() {
        }

        /**
         * @param httpMethod {@link HttpMethod}
         * @return
         */
        public Builder<I, O> method(String httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        public Builder<I, O> addHeader(String key, String value) {
            headers.put(key, value);
            return this;
        }

        public Builder<I, O> addHeader(Map<String, String> headers) {
            this.headers.putAll(headers);
            return this;
        }

        public Builder<I, O> addParam(String key, String value) {
            this.params.put(key, value);
            return this;
        }

        public Builder<I, O> addParam(Map<String, String> params) {
            this.params.putAll(params);
            return this;
        }

        public Builder<I, O> requestBody(I requestBody) {
            this.requestBody = requestBody;
            return this;
        }

        public Builder<I, O> url(String url) {
            this.url = url;
            return this;
        }

        public Builder<I, O> contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public RequestParams<I, O> build() {
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
