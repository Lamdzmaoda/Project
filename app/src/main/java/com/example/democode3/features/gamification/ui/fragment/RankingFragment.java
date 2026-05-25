package com.example.democode3.features.gamification.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.democode3.R;
import com.example.democode3.core.session.SessionManager;
import com.example.democode3.features.gamification.model.RankingUser;
import com.example.democode3.features.gamification.ui.adapter.RankingAdapter;
import com.example.democode3.features.gamification.ui.viewmodel.RankingViewModel;
import com.example.democode3.features.profile.ui.fragment.ProfileFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class RankingFragment
        extends Fragment {

    RecyclerView recyclerRanking;

    TabLayout tabLeague;

    TextView txtTop1;

    TextView txtTop2;

    TextView txtTop3;

    TextView txtTop1Xp;

    TextView txtTop2Xp;

    TextView txtTop3Xp;

    TextView txtAvatarTop1;

    TextView txtAvatarTop2;

    TextView txtAvatarTop3;

    LinearLayout layoutTop1;

    LinearLayout layoutTop2;

    LinearLayout layoutTop3;

    RankingViewModel viewModel;

    @Override
    public View onCreateView(

            LayoutInflater inflater,

            ViewGroup container,

            Bundle savedInstanceState
    ) {

        View view =
                inflater.inflate(

                        R.layout.fragment_ranking,

                        container,

                        false
                );

        initViews(view);

        viewModel =
                new ViewModelProvider(this)
                        .get(RankingViewModel.class);

        setupLeagueTabs();

        setupTabListener();

        observeRanking();

        viewModel.loadWeeklyRanking();

        return view;
    }

    // =====================================
    // INIT
    // =====================================

    private void initViews(View view) {

        recyclerRanking =
                view.findViewById(
                        R.id.recyclerRanking
                );

        tabLeague =
                view.findViewById(
                        R.id.tabLeague
                );

        txtTop1 =
                view.findViewById(
                        R.id.txtTop1
                );

        txtTop2 =
                view.findViewById(
                        R.id.txtTop2
                );

        txtTop3 =
                view.findViewById(
                        R.id.txtTop3
                );

        txtTop1Xp =
                view.findViewById(
                        R.id.txtTop1Xp
                );

        txtTop2Xp =
                view.findViewById(
                        R.id.txtTop2Xp
                );

        txtTop3Xp =
                view.findViewById(
                        R.id.txtTop3Xp
                );

        txtAvatarTop1 =
                view.findViewById(
                        R.id.txtAvatarTop1
                );

        txtAvatarTop2 =
                view.findViewById(
                        R.id.txtAvatarTop2
                );

        txtAvatarTop3 =
                view.findViewById(
                        R.id.txtAvatarTop3
                );

        layoutTop1 =
                view.findViewById(
                        R.id.layoutTop1
                );

        layoutTop2 =
                view.findViewById(
                        R.id.layoutTop2
                );

        layoutTop3 =
                view.findViewById(
                        R.id.layoutTop3
                );
    }

    // =====================================
    // TABS
    // =====================================

    private void setupLeagueTabs() {

        tabLeague.addTab(
                tabLeague.newTab()
                        .setText("BXH Tuần")
        );

        tabLeague.addTab(
                tabLeague.newTab()
                        .setText("BXH Tháng")
        );

        tabLeague.addTab(
                tabLeague.newTab()
                        .setText("Trùm Coder")
        );
    }

    // =====================================
    // TAB LISTENER
    // =====================================

    private void setupTabListener() {

        tabLeague.addOnTabSelectedListener(

                new TabLayout.OnTabSelectedListener() {

                    @Override
                    public void onTabSelected(
                            TabLayout.Tab tab
                    ) {

                        if (tab == null) {

                            return;
                        }

                        int pos =
                                tab.getPosition();

                        // WEEK

                        if (pos == 0) {

                            viewModel
                                    .loadWeeklyRanking();
                        }

                        // MONTH

                        else if (pos == 1) {

                            viewModel
                                    .loadMonthlyRanking();
                        }

                        // LEGEND

                        else {

                            viewModel
                                    .loadLegendRanking();
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
    }

    // =====================================
    // OBSERVE
    // =====================================

    private void observeRanking() {

        viewModel.getRanking().observe(

                getViewLifecycleOwner(),

                this::renderRanking
        );
    }

    // =====================================
    // RENDER
    // =====================================

    private void renderRanking(
            List<RankingUser> list
    ) {

        if (list == null
                || list.size() < 3) {

            return;
        }

        RankingUser top1 =
                list.get(0);

        RankingUser top2 =
                list.get(1);

        RankingUser top3 =
                list.get(2);

        txtTop1.setText(
                top1.username
        );

        txtTop2.setText(
                top2.username
        );

        txtTop3.setText(
                top3.username
        );

        txtTop1Xp.setText(
                top1.xp + " XP"
        );

        txtTop2Xp.setText(
                top2.xp + " XP"
        );

        txtTop3Xp.setText(
                top3.xp + " XP"
        );

        txtAvatarTop1.setText(
                top1.username
                        .substring(0, 1)
                        .toUpperCase()
        );

        txtAvatarTop2.setText(
                top2.username
                        .substring(0, 1)
                        .toUpperCase()
        );

        txtAvatarTop3.setText(
                top3.username
                        .substring(0, 1)
                        .toUpperCase()
        );

        layoutTop1.setOnClickListener(v ->
                openProfile(top1.id)
        );

        layoutTop2.setOnClickListener(v ->
                openProfile(top2.id)
        );

        layoutTop3.setOnClickListener(v ->
                openProfile(top3.id)
        );

        recyclerRanking.setLayoutManager(

                new LinearLayoutManager(
                        requireContext()
                )
        );

        recyclerRanking.setAdapter(

                new RankingAdapter(

                        list,

                        this::openProfile
                )
        );

        // AUTO SCROLL

        String currentUserId =
                String.valueOf(
                        SessionManager
                                .getUser(
                                        requireContext()
                                )
                                .id
                );

        for (int i = 0; i < list.size(); i++) {

            RankingUser user =
                    list.get(i);

            if (
                    user.id != null
                            &&
                            user.id.equals(currentUserId)
            ) {

                recyclerRanking
                        .scrollToPosition(i);

                break;
            }
        }
    }

    // =====================================
    // OPEN PROFILE
    // =====================================

    private void openProfile(
            String userId
    ) {

        String currentUserId =
                String.valueOf(
                        SessionManager
                                .getUser(requireContext())
                                .id
                );

        if (
                userId != null
                        &&
                        userId.equals(currentUserId)
        ) {

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(

                            ((ViewGroup)
                                    requireView()
                                            .getParent())
                                    .getId(),

                            new com.example.democode3
                                    .features.profile
                                    .ui.fragment.ProfileFragment()
                    )
                    .addToBackStack(null)
                    .commit();

            return;
        }

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(

                        ((ViewGroup)
                                requireView()
                                        .getParent())
                                .getId(),

                        ProfileFragment.newInstance(
                                userId
                        )
                )
                .addToBackStack(null)
                .commit();
    }
}