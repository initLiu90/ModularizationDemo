package com.lzp.library.net;

public abstract class Converter<I, O> {
    public O responseConvert(byte[] data) {
        return null;
    }

    public byte[] requestConvert(I data) {
        return null;
    }
}
