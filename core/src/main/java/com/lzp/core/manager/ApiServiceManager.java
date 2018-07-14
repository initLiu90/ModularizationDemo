package com.lzp.core.manager;

public interface ApiServiceManager extends Manager {
    <T> void addApiService(Class<T> apiClz, Class<? extends T> serviceImplClz);

    <T> T getApiService(Class<T> apiClz);

    <T> void removeApiService(Class<T> apiClz);
}
