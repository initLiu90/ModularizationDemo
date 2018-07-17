package com.lzp.core.observer;

public interface BusinessObserver {
    void onUpdate(boolean isSuccess, Object data);
}
