package com.lzp.core.observer;

/**
 * module间公共Observer
 * 使用时各module自定义一个Observer类继承CommonObserver，
 * 调用{@link com.lzp.core.AppRuntime#notifyObserver(Class, boolean, Object)}方法时，
 * 第一个参数传{@link CommonObserver}，这样所有继承CommonObserver的类都可以收到通知。
 */
public abstract class CommonObserver implements BusinessObserver {
}
