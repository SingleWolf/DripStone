package com.walker.core.efficiency.taskflow;


public class TaskNode implements Node {

    /**
     * 节点id
     */
    private int nodeId;

    /**
     * 节点任务者
     */
    private Tasker tasker;

    private WorkCallBack callBack;

    public static TaskNode build(int nodeId, Tasker tasker) {
        return new TaskNode(nodeId, tasker);
    }

    public TaskNode(int nodeId, Tasker tasker) {
        this.nodeId = nodeId;
        this.tasker = tasker;
    }

    void doWork(WorkCallBack callBack) {
        this.callBack = callBack;
        tasker.doWork(this);
    }

    void removeCallBack() {
        this.callBack = null;
    }

    @Override
    public int getId() {
        return nodeId;
    }

    @Override
    public void onCompleted() {
        if (null != callBack) {
            callBack.onWorkCompleted();
        }
    }

    @Override
    public String toString() {
        return "nodeId : " + getId();
    }

    interface WorkCallBack {

        /**
         * 当前任务完成
         */
        void onWorkCompleted();

    }
}
