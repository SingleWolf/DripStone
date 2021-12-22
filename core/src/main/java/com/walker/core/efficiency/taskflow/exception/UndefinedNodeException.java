package com.walker.core.efficiency.taskflow.exception;

public class UndefinedNodeException extends IllegalStateException {
    public UndefinedNodeException(int nodeId) {
        super("The id :" + nodeId + " did not found when task flow executed \n Check your task flow build");
    }
}
