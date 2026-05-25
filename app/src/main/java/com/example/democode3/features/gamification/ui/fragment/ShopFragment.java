package com.example.democode3.features.gamification.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.democode3.R;

public class ShopFragment
        extends Fragment {

    @Override
    public View onCreateView(

            LayoutInflater inflater,

            ViewGroup container,

            Bundle savedInstanceState
    ) {

        return inflater.inflate(

                R.layout.fragment_shop,

                container,

                false
        );
    }
}