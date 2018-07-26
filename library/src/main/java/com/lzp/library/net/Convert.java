package com.lzp.library.net;

import okhttp3.RequestBody;

public abstract class Convert<F, T> {
    public T responseBodyConvert(byte[] data) {
        return null;
    }

    public byte[] requestBodyConvert(F data) {
        return null;
    }
}
