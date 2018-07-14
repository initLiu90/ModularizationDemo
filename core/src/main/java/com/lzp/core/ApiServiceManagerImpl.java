package com.lzp.core;

import com.lzp.core.manager.ApiServiceManager;

import java.util.HashMap;
import java.util.Map;

class ApiServiceManagerImpl implements ApiServiceManager {
    private Map<Class<?>, Object> apiServiceMap = new HashMap<>();

    @Override
    public <T> void addApiService(Class<T> apiClz, Class<? extends T> serviceImplClz) {
        try {
            if (serviceImplClz != null) {
                apiServiceMap.put(apiClz, serviceImplClz.newInstance());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> T getApiService(Class<T> apiClz) {
        Object service = apiServiceMap.get(apiClz);
        if (service != null) {
            return (T) service;
        }
        return null;
    }

    @Override
    public <T> void removeApiService(Class<T> apiClz) {
        apiServiceMap.remove(apiClz);
    }

    @Override
    public void destry() {
        apiServiceMap.clear();
        apiServiceMap = null;
    }
}
