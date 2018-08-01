package com.lzp.library.net;

/**
 *  根据{@link NetCenter#getApiService(RequestParams)}的实现，{@link Converter}运行在{@link io.reactivex.schedulers.Schedulers#IO}线程上
 * @param <I> 外界传入的请求参数类型
 * @param <O> http response返回的数据类型经过{@link Converter#responseConvert(byte[])}转换后的类型
 */
public abstract class Converter<I, O> {
    /**
     * 将服务器返回的数据，转换为外界期望返回的数据
     * @param data
     * @return
     */
    public O responseConvert(byte[] data) {
        return null;
    }

    /**
     * 将外界传入的请求数据，转换为byte[]数组
     * @param data
     * @return
     */
    public byte[] requestConvert(I data) {
        return null;
    }
}
