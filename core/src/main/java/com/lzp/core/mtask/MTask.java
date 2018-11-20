package com.lzp.core.mtask;

import io.reactivex.Scheduler;

public interface MTask extends Runnable{
    /**
     * Task名
     *
     * @return
     */
    String name();

    /**
     * 添加依赖
     *
     * @param tasks
     */
    void dependsOn(String... tasks);

    /**
     * 设置MTask执行的线程
     */
    void setScheduler(Scheduler scheduler);

    /**
     * 获取MTask执行的线程
     */
    Scheduler getScheduler();

    /**
     * 删除依赖
     *
     * @param tasks
     * @return
     */
    boolean remove(String... tasks);

    /**
     * 获取依赖
     *
     * @return
     */
    String[] getDepends();

    /**
     * 是否有依赖的Task
     *
     * @return
     */
    boolean hasDepends();

    /**
     * 在依赖集中查找传入的Task是否存在
     *
     * @param tasks
     * @return Task依赖集中存在的task
     */
    String[] filterDepends(String... tasks);

    /**
     * 设置Task的依赖
     * the method dependOn must be call in this method
     */
    void config();

    /**
     * 执行task
     */
    void exec();

    /**
     * 是否执行结束
     *
     * @return
     */
    boolean complete();
}
