package com.walker.core.base.mvvm.model;

import android.text.TextUtils;

import androidx.annotation.CallSuper;

import com.walker.core.store.sp.BasicDataSPHelper;
import com.walker.core.util.GsonUtils;
import com.walker.core.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class MvvmBaseModel<F, T extends ArrayList> implements MvvmAcquireDataObserver<F>{
    private CompositeDisposable compositeDisposable;
    protected ReferenceQueue<IBaseModelListener> mReferenceQueue;
    protected ConcurrentLinkedQueue<WeakReference<IBaseModelListener>> mWeakListenerArrayList;
    private BaseCachedData<F> mCachedData;
    protected boolean isRefresh = true;
    protected int pageNumber = 0;
    /**
     * 缓存数据获取key
     */
    private String mCachedDataKey;
    /**
     * apk预置数据
     */
    private String mApkPredefinedData;
    /**
     * 分页标识
     */
    private boolean mIsPaging;
    /**
     * 数据元结构
     */
    private Class<F> clazz;

    public MvvmBaseModel(Class<F> clazz) {
        this(clazz,false,null,null,0);
    }

    public MvvmBaseModel(Class<F> clazz, boolean isPaging, String cacheDataKey, String apkPredefinedData, int... initPageNumber) {
        this.mIsPaging = isPaging;
        this.clazz = clazz;
        if(initPageNumber != null && initPageNumber.length == 1) {
            this.pageNumber = initPageNumber[0];
        }
        this.mCachedDataKey = cacheDataKey;
        this.mApkPredefinedData = apkPredefinedData;
        mReferenceQueue = new ReferenceQueue<>();
        mWeakListenerArrayList = new ConcurrentLinkedQueue<>();
        if (!Utils.isEmpty(mCachedDataKey)) {
            mCachedData = new BaseCachedData<F>();
        }
    }

    public boolean isPaging() {
        return mIsPaging;
    }

    /**
     * 注册监听
     *
     * @param listener
     */
    public void register(IBaseModelListener listener) {
        if (listener == null) {
            return;
        }

        synchronized (this) {
            // 每次注册的时候清理已经被系统回收的对象
            Reference<? extends IBaseModelListener> releaseListener = null;
            while ((releaseListener = mReferenceQueue.poll()) != null) {
                mWeakListenerArrayList.remove(releaseListener);
            }

            for (WeakReference<IBaseModelListener> weakListener : mWeakListenerArrayList) {
                IBaseModelListener listenerItem = weakListener.get();
                if (listenerItem == listener) {
                    return;
                }
            }
            WeakReference<IBaseModelListener> weakListener = new WeakReference<IBaseModelListener>(listener, mReferenceQueue);
            mWeakListenerArrayList.add(weakListener);
        }

    }

    /**
     * 取消监听
     *
     * @param listener
     */
    public void unRegister(IBaseModelListener listener) {

        if (listener == null) {
            return;
        }
        synchronized (this) {
            for (WeakReference<IBaseModelListener> weakListener : mWeakListenerArrayList) {
                IBaseModelListener listenerItem = weakListener.get();
                if (listener == listenerItem) {
                    mWeakListenerArrayList.remove(weakListener);
                    break;
                }
            }
        }
    }

    protected void saveDataToCache(F data) {
        if(data != null) {
            mCachedData.data = data;
            mCachedData.updateTimeInMills = System.currentTimeMillis();
            BasicDataSPHelper.get().setString(mCachedDataKey, GsonUtils.toJson(mCachedData));
        }
    }

    public abstract void refresh();

    protected abstract void load();

    /**
     * 是否更新数据，可以在这里设计策略，可以是一天一次，一月一次等等，
     * 默认是每次请求都更新
     */
    protected boolean isNeedToUpdate() {
        return true;
    }

    @CallSuper
    public void cancel() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }


    public void addDisposable(Disposable d) {
        if (d == null) {
            return;
        }

        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }

        compositeDisposable.add(d);
    }

    public void getCachedDataAndLoad() {
        if (!Utils.isEmpty(mCachedDataKey)) {
            String saveDataString = BasicDataSPHelper.get().getString(mCachedDataKey);
            if (!TextUtils.isEmpty(saveDataString)) {
                try {
                    F savedData = GsonUtils.fromLocalJson(new JSONObject(saveDataString).getString("data"), clazz);
                    if (savedData != null) {
                        onSuccess(savedData, true);
                        if (isNeedToUpdate()) {
                            load();
                        }
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            if (mApkPredefinedData != null) {
                F savedData = GsonUtils.fromLocalJson(mApkPredefinedData, clazz);
                if(savedData != null) {
                    onSuccess(savedData, true);
                }
            }
        }
        load();
    }

    /**
     * 发消息给UI线程
     */
    protected void loadSuccess(F networkResonseBean, T data, boolean isFromCache) {
        synchronized (this) {
            for (WeakReference<IBaseModelListener> weakListener : mWeakListenerArrayList) {
                if (weakListener.get() instanceof IBaseModelListener) {
                    IBaseModelListener listenerItem = weakListener.get();
                    if (listenerItem != null) {
                        if (isPaging()) {
                            listenerItem.onLoadFinish(this, data,
                                    isFromCache? new PagingResult(false, true, true) :
                                            new PagingResult(data.isEmpty(), isRefresh, data.size() > 0));
                            if(!Utils.isEmpty(mCachedDataKey) && isRefresh && !isFromCache) {
                                saveDataToCache(networkResonseBean);
                            }
                            if(!isFromCache) {
                                pageNumber ++;
                            }
                        } else {
                            listenerItem.onLoadFinish(this, data);
                            if(!Utils.isEmpty(mCachedDataKey)) {
                                saveDataToCache(networkResonseBean);
                            }
                        }
                    }
                }
            }
        }
    }

    protected void loadFail(final String errorMessage) {
        synchronized (this) {
            for (WeakReference<IBaseModelListener> weakListener : mWeakListenerArrayList) {
                if (weakListener.get() instanceof IBaseModelListener) {
                    IBaseModelListener listenerItem = weakListener.get();
                    if (listenerItem != null) {
                        if (isPaging()) {
                            listenerItem.onLoadFail(this, errorMessage, new PagingResult(true, isRefresh, false));
                        } else {
                            listenerItem.onLoadFail(this, errorMessage);
                        }
                    }
                }
            }
        }
    }
}
