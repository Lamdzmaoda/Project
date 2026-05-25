package com.example.democode3.features.gamification.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.democode3.R;
import com.google.android.material.tabs.TabLayout;

public class GamificationFragment
        extends Fragment {

    // =====================================
    // TAB
    // =====================================

    TabLayout tabLayout;

    @Override
    public View onCreateView(

            LayoutInflater inflater,

            ViewGroup container,

            Bundle savedInstanceState
    ) {

        View view =
                inflater.inflate(

                        R.layout.fragment_gamification,

                        container,

                        false
                );

        tabLayout =
                view.findViewById(
                        R.id.tabLayout
                );

        setupTabs();

        // =================================
        // DEFAULT
        // =================================

        loadFragment(
                new RankingFragment()
        );

        return view;
    }

    // =====================================
    // TAB
    // =====================================

    private void setupTabs() {

        tabLayout.addTab(

                tabLayout.newTab()
                        .setText("🏆 Thành tựu")
        );

        tabLayout.addTab(

                tabLayout.newTab()
                        .setText("🥇 BXH")
        );

        tabLayout.addTab(

                tabLayout.newTab()
                        .setText("🪙 Shop")
        );

        tabLayout.addOnTabSelectedListener(

                new TabLayout.OnTabSelectedListener() {

                    @Override
                    public void onTabSelected(
                            TabLayout.Tab tab
                    ) {

                        switch (tab.getPosition()) {

                            // =====================
                            // ACHIEVEMENT
                            // =====================

                            case 0:

                                loadFragment(
                                        new AchievementFragment()
                                );

                                break;

                            // =====================
                            // RANKING
                            // =====================

                            case 1:

                                loadFragment(
                                        new RankingFragment()
                                );

                                break;

                            // =====================
                            // SHOP
                            // =====================

                            case 2:

                                loadFragment(
                                        new ShopFragment()
                                );

                                break;
                        }
                    }

                    @Override
                    public void onTabUnselected(
                            TabLayout.Tab tab
                    ) {
                    }

                    @Override
                    public void onTabReselected(
                            TabLayout.Tab tab
                    ) {
                    }
                }
        );

        // =================================
        // DEFAULT BXH
        // =================================

        if (tabLayout.getTabAt(1) != null) {

            tabLayout.getTabAt(1).select();
        }
    }

    // =====================================
    // LOAD FRAGMENT
    // =====================================

    private void loadFragment(
            Fragment fragment
    ) {

        getChildFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.container,
                        fragment
                )
                .commit();
    }
}