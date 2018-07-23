package com.lzp.library.net;

public interface Convert<T> {
    T convert(byte[] data);
}
