package com.lzp.library.net;

import java.util.HashMap;
import java.util.Map;

public class RequestParams<F, T> {
    public final String httpMethod;
    public final String url;
    public final Map<String, String> headers;
    public final Map<String, String> params;
    public final Convert<? super F, ? super T> converter;

    public RequestParams(Builder builder) {
        this.httpMethod = builder.httpMethod;
        this.url = builder.url;
        this.headers = builder.headers;
        this.params = builder.params;
        this.converter = builder.converter;
    }


    public static class Builder<F, T> {
        private String httpMethod;
        private Map<String, String> headers = new HashMap<>();
        private Map<String, String> params = new HashMap<>();
        private String url;

        private Convert<? super F, ? super T> converter;

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
        public Builder<F, T> method(String httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        public Builder<F, T> addHeader(String key, String value) {
            headers.put(key, value);
            return this;
        }

        public Builder<F, T> addHeader(Map<String, String> headers) {
            this.headers.putAll(headers);
            return this;
        }

        public Builder<F, T> addParam(String key, String value) {
            this.params.put(key, value);
            return this;
        }

        public Builder<F, T> addParam(Map<String, String> params) {
            this.params.putAll(params);
            return this;
        }

        public Builder<F, T> url(String url) {
            this.url = url;
            return this;
        }

        public Builder<F, T> converter(Convert<? super F, ? super T> converter) {
            this.converter = converter;
            return this;
        }

        public RequestParams<F, T> build() {
            return new RequestParams(this);
        }
    }

    public interface HttpMethod {
        String GET = "Get";
        String POST = "Post";
    }
}
