package com.walker.core.base.mvvm.model;

public class PagingResult {
    public PagingResult(boolean isEmpty, boolean isFirstPage, boolean hasNextPage) {
        this.isEmpty = isEmpty;
        this.isFirstPage = isFirstPage;
        this.hasNextPage = hasNextPage;
    }

    public boolean isFirstPage;
    public boolean isEmpty;
    public boolean hasNextPage;
}
