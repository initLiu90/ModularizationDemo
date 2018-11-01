package com.lzp.modularizationdemo.task;

public interface Task {
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
    void dependOn(Task... tasks);

    /**
     * 删除依赖
     *
     * @param tasks
     * @return
     */
    boolean remove(Task... tasks);

    /**
     * 获取依赖
     *
     * @return
     */
    Task[] getDepends();

    /**
     * 是否有依赖的Task
     *
     * @return
     */
    boolean hasDepend();

    /**
     * 在依赖集中查找传入的Task是否存在
     *
     * @param tasks
     * @return Task依赖集中存在的task
     */
    Task[] filterDepends(Task... tasks);

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
