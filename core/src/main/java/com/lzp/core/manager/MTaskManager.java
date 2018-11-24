package com.lzp.core.manager;

import com.lzp.core.mtask.MTask;
import com.lzp.core.mtask.MTaskList;

public interface MTaskManager extends Manager{

    /**
     * 将MTasks添加到MTaskManager
     */
    void collectTasks(MTask... mTasks);

    /**
     * 删除task
     * @param tasks
     * @return 删除了多少了task
     */
    int removeTasks(MTask... tasks);

    /**
     * 删除task
     * @param tasks
     * @return 删除了多少了task
     */
    int removeTasks(String... tasks);

    /**
     * 调用MTask的config方法
     */
    void configTasks();

    /**
     * 将收集到的任务进行依赖分析，去除重复的依赖以及调整依赖顺序
     */
    void flatTasks();

    /**
     * 分析单个Task，去除重复的依赖以及调整依赖顺序
     *
     * @param task
     */
    void flatTask(MTask task);

    /**
     * 获取所有的任务
     */
    MTask[] getTasks();

    /**
     * 根据MTask名获取MTask对象
     * @param taskName
     * @return
     */
    MTask getMTask(String taskName);

    /**
     * 获取Task执行队列
     * 以链表的数据结构进行表示
     */
    MTaskList getTaskList();
}
