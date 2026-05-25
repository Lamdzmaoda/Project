package com.example.democode3.core.base;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BaseViewModel extends ViewModel {

    public MutableLiveData<Boolean> isLoading =
            new MutableLiveData<>(false);

    public MutableLiveData<String> errorMessage =
            new MutableLiveData<>();

    public MutableLiveData<String> successMessage =
            new MutableLiveData<>();
}