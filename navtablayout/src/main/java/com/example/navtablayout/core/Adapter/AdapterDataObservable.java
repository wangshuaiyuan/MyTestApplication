package com.example.navtablayout.core.Adapter;

import android.database.Observable;

/**
 * Created by wsy on 28/11/2018
 * adapter数据被观察者
 */
public class AdapterDataObservable extends Observable<IAdapterDataObserver> {
    /**
     * 数据变更通知
     */
    public void notifyChanged() {
        for (int i = mObservers.size() - 1; i >= 0; i--) {
            mObservers.get(i).onChanged();
        }
    }
}
