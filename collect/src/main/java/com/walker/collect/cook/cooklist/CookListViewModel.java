package com.walker.collect.cook.cooklist;

import com.walker.core.base.mvvm.customview.BaseCustomViewModel;
import com.walker.core.base.mvvm.viewmodel.MvvmBaseViewModel;

public class CookListViewModel extends MvvmBaseViewModel<CookListModel, BaseCustomViewModel> {
    public CookListViewModel init() {
        model = new CookListModel();
        model.register(this);
        model.getCachedDataAndLoad();
        return this;
    }

    public void tryToLoadNextPage() {
        model.loadNexPage();
    }
}
