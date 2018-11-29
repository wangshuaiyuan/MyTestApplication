package com.example.navtablayout.Adapter;

import android.database.Observable;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wsy on 27/11/2018
 */
public abstract class AbsTabAdapter {

    private AdapterDataObservable mAdapterDataObservable = new AdapterDataObservable();

    /**
     * @param parent   父视图
     * @param position 当前位置
     * @param isSelect 是否选中
     * @return 内容视图
     */
    public abstract View getItemView(ViewGroup parent, int position, boolean isSelect);

    /**
     * 刷新选中位置
     *
     * @param itemView          最新选中位置itemView
     * @param newSelectPosition 最新选中位置
     */
    public abstract void updateSelectPosition(View itemView, int newSelectPosition);


    /**
     * 刷新取消选中位置
     *
     * @param itemView         更新前选中位置itemView
     * @param unSelectPosition 更新前选中位置
     */
    public abstract void updateUnSelectPosition(View itemView, int unSelectPosition);

    /**
     * @return 条目数量
     */
    public abstract int getItemCount();

    /**
     * 通知数据变通
     */
    public void notifyDataSetChanged() {
        mAdapterDataObservable.notifyChanged();
    }

    /**
     * 注册监听者
     *
     * @param observer 监听者
     */
    public void registerDataObserver(IAdapterDataObserver observer) {
        mAdapterDataObservable.registerObserver(observer);
    }

    /**
     * 解除注册监听者
     *
     * @param observer 监听者
     */
    public void unRegisterDataObserver(IAdapterDataObserver observer) {
        mAdapterDataObservable.unregisterObserver(observer);
    }


}

