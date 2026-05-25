package com.example.democode3.core.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    protected View rootView;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        rootView = inflater.inflate(
                getLayoutId(),
                container,
                false
        );

        initView();

        initData();

        initListener();

        return rootView;
    }

    protected abstract int getLayoutId();

    protected void initView() {

    }

    protected void initData() {

    }

    protected void initListener() {

    }
}