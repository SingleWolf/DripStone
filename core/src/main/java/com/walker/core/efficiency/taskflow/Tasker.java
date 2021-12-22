package com.walker.core.efficiency.taskflow;

public interface Tasker {

    /**
     * 执行任务
     *
     * @param current 当前节点
     */
    void doWork(Node current);

}