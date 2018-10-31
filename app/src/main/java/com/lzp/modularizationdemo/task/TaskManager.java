package com.lzp.modularizationdemo.task;

public interface TaskManager {
    /**
     * 收集任务
     *
     * @param tasks
     */
    void collectTasks(Task... tasks);

    /**
     * 将收集到的任务进行依赖分析，去除重复的依赖以及调整依赖顺序
     */
    void flatTasks();

    /**
     * 分析单个Task，去除重复的依赖以及调整依赖顺序
     *
     * @param task
     */
    void flatTask(Task task);

    /**
     * 获取所有的任务
     */
    Task[] getTasks();

    /**
     * 创建Task树
     */
    void createTaskTree();
}
