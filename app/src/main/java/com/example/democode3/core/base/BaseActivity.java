package com.example.democode3.core.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());

        initView();

        initData();

        initListener();
    }

    protected abstract int getLayoutId();

    protected void initView() {

    }

    protected void initData() {

    }

    protected void initListener() {

    }
}