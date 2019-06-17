package com.easygo.cashier.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.niubility.library.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Describe：仓储管理app基类，继承基础库基类Fragment
 * @Date：2019-04-28
 */
public abstract class BaseAppFragment extends BaseFragment {
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        setListener();
    }

    protected abstract int getLayout();

    protected abstract void initView();

    protected abstract void setListener();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
