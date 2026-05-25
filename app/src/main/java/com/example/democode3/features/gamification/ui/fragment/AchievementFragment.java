package com.example.democode3.features.gamification.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.democode3.R;

public class AchievementFragment
        extends Fragment {

    @Override
    public View onCreateView(

            LayoutInflater inflater,

            ViewGroup container,

            Bundle savedInstanceState
    ) {

        View view =

                inflater.inflate(

                        R.layout.fragment_achievement,

                        container,

                        false
                );

        setupAnimation(view);

        return view;
    }

    // =====================================
    // SIMPLE ANIMATION
    // =====================================

    private void setupAnimation(
            View view
    ) {

        view.setAlpha(0f);

        view.animate()

                .alpha(1f)

                .setDuration(400)

                .start();
    }
}