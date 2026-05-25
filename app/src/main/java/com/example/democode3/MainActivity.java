package com.example.democode3;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.democode3.features.community.ui.fragment.CommunityFragment;
import com.example.democode3.features.gamification.ui.fragment.GamificationFragment;
import com.example.democode3.features.home.ui.fragment.HomeFragment;
import com.example.democode3.features.practice.ui.fragment.PracticeFragment;
import com.example.democode3.features.profile.ui.fragment.ProfileFragment;

public class MainActivity
        extends AppCompatActivity {

    // =====================================================
    // VIEW
    // =====================================================

    private LinearLayout btnHome;

    private LinearLayout btnPractice;

    private LinearLayout btnCommunity;

    private LinearLayout btnProgress;

    private LinearLayout btnProfile;

    // =====================================================
    // CURRENT TAB
    // =====================================================

    private String currentTab = "HOME";

    // =====================================================
    // CREATE
    // =====================================================

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_main
        );

        initViews();

        setupBottomNavigation();

        // =============================================
        // DEFAULT
        // =============================================

        selectTab(btnHome);

        loadFragment(
                new HomeFragment()
        );
    }

    // =====================================================
    // INIT VIEW
    // =====================================================

    private void initViews() {

        btnHome =
                findViewById(
                        R.id.btnHome
                );

        btnPractice =
                findViewById(
                        R.id.btnPractice
                );

        btnCommunity =
                findViewById(
                        R.id.btnCommunity
                );

        btnProgress =
                findViewById(
                        R.id.btnProgress
                );

        btnProfile =
                findViewById(
                        R.id.btnProfile
                );
    }

    // =====================================================
    // BOTTOM NAVIGATION
    // =====================================================

    private void setupBottomNavigation() {

        // =============================================
        // HOME
        // =============================================

        btnHome.setOnClickListener(v -> {

            if (currentTab.equals("HOME")) {
                return;
            }

            currentTab = "HOME";

            selectTab(btnHome);

            loadFragment(
                    new HomeFragment()
            );
        });

        // =============================================
        // PRACTICE
        // =============================================

        btnPractice.setOnClickListener(v -> {

            if (currentTab.equals("PRACTICE")) {
                return;
            }

            currentTab = "PRACTICE";

            selectTab(btnPractice);

            loadFragment(
                    new PracticeFragment()
            );
        });

        // =============================================
        // COMMUNITY
        // =============================================

        btnCommunity.setOnClickListener(v -> {

            if (currentTab.equals("COMMUNITY")) {
                return;
            }

            currentTab = "COMMUNITY";

            selectTab(btnCommunity);

            loadFragment(
                    new CommunityFragment()
            );
        });

        // =============================================
        // PROGRESS
        // =============================================

        btnProgress.setOnClickListener(v -> {

            if (currentTab.equals("PROGRESS")) {
                return;
            }

            currentTab = "PROGRESS";

            selectTab(btnProgress);

            loadFragment(
                    new GamificationFragment()
            );
        });

        // =============================================
        // PROFILE
        // =============================================

        btnProfile.setOnClickListener(v -> {

            if (currentTab.equals("PROFILE")) {
                return;
            }

            currentTab = "PROFILE";

            selectTab(btnProfile);

            loadFragment(
                    new ProfileFragment()
            );
        });
    }

    // =====================================================
    // SELECT TAB
    // =====================================================

    private void selectTab(
            LinearLayout selected
    ) {

        resetTab(btnHome);

        resetTab(btnPractice);

        resetTab(btnCommunity);

        resetTab(btnProgress);

        resetTab(btnProfile);

        selected.setAlpha(1f);

        selected.setScaleX(1.08f);

        selected.setScaleY(1.08f);

        selected.setBackgroundColor(
                0xFF4F46E5
        );
    }

    // =====================================================
    // RESET TAB
    // =====================================================

    private void resetTab(
            LinearLayout tab
    ) {

        tab.setAlpha(0.6f);

        tab.setScaleX(1f);

        tab.setScaleY(1f);

        tab.setBackgroundColor(
                0x00000000
        );
    }

    // =====================================================
    // LOAD FRAGMENT
    // =====================================================

    private void loadFragment(
            Fragment fragment
    ) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.fragmentContainer,
                        fragment
                )
                .commit();
    }
}