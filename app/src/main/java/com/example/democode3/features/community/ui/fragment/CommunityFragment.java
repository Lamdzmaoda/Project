package com.example.democode3.features.community.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.democode3.R;
import com.example.democode3.core.enums.FeedType;
import com.example.democode3.features.community.model.Post;
import com.example.democode3.features.community.ui.activity.PostDetailActivity;
import com.example.democode3.features.community.ui.adapter.PostAdapter;
import com.example.democode3.features.community.ui.component.CommentBottomSheet;
import com.example.democode3.features.community.ui.viewmodel.CommunityViewModel;
import com.example.democode3.features.profile.ui.fragment.ProfileFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class CommunityFragment extends Fragment {

    // =====================================
    // CURRENT USER
    // =====================================

    private final String currentUserId = "1";

    // =====================================
    // VIEW
    // =====================================

    private RecyclerView recyclerView;

    private TabLayout tabLayout;

    private FloatingActionButton btnAddPost;

    // =====================================
    // VIEWMODEL
    // =====================================

    private CommunityViewModel viewModel;

    // =====================================
    // FEED
    // =====================================

    private FeedType currentFeed =
            FeedType.FOR_YOU;

    @Override
    public View onCreateView(

            LayoutInflater inflater,

            ViewGroup container,

            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(

                R.layout.fragment_community,

                container,

                false
        );

        // =====================================
        // FIND VIEW
        // =====================================

        recyclerView =
                view.findViewById(R.id.recyclerPost);

        tabLayout =
                view.findViewById(R.id.tabLayout);

        btnAddPost =
                view.findViewById(R.id.btnAddPost);

        // =====================================
        // VIEWMODEL
        // =====================================

        viewModel =
                new ViewModelProvider(this)
                        .get(CommunityViewModel.class);

        viewModel.init(requireContext());

        // =====================================
        // TAB
        // =====================================

        tabLayout.addTab(
                tabLayout.newTab()
                        .setText("Đang theo dõi")
        );

        tabLayout.addTab(
                tabLayout.newTab()
                        .setText("Dành cho bạn")
        );

        tabLayout.addTab(
                tabLayout.newTab()
                        .setText("Nhóm học tập")
        );

        // =====================================
        // DEFAULT TAB
        // =====================================

        tabLayout.selectTab(
                tabLayout.getTabAt(1)
        );

        currentFeed =
                FeedType.FOR_YOU;

        // =====================================
        // RECYCLER
        // =====================================

        recyclerView.setLayoutManager(

                new LinearLayoutManager(
                        requireContext()
                )
        );

        // =====================================
        // OBSERVE
        // =====================================

        viewModel.getPosts().observe(

                getViewLifecycleOwner(),

                posts -> {

                    recyclerView.setAdapter(

                            new PostAdapter(

                                    requireContext(),

                                    posts,

                                    new PostAdapter.OnPostActionListener() {

                                        // =========================
                                        // PROFILE
                                        // =========================

                                        @Override
                                        public void onOpenProfile(
                                                String userId
                                        ) {

                                            ProfileFragment fragment =
                                                    new ProfileFragment();

                                            if (!userId.equals(currentUserId)) {

                                                Bundle args =
                                                        new Bundle();

                                                args.putString(
                                                        "userId",
                                                        userId
                                                );

                                                fragment.setArguments(args);
                                            }

                                            requireActivity()
                                                    .getSupportFragmentManager()
                                                    .beginTransaction()
                                                    .replace(
                                                            R.id.fragmentContainer,
                                                            fragment
                                                    )
                                                    .addToBackStack(null)
                                                    .commit();
                                        }

                                        // =========================
                                        // COMMENT
                                        // =========================

                                        @Override
                                        public void onOpenComment(
                                                Post post
                                        ) {

                                            CommentBottomSheet sheet =
                                                    CommentBottomSheet
                                                            .newInstance(post.id);

                                            sheet.show(

                                                    getParentFragmentManager(),

                                                    "comment_sheet"
                                            );
                                        }


                                    }
                            )
                    );
                }
        );

        // =====================================
        // FIRST LOAD
        // =====================================

        viewModel.loadFeed(currentFeed);

        // =====================================
        // TAB CHANGE
        // =====================================

        tabLayout.addOnTabSelectedListener(

                new TabLayout.OnTabSelectedListener() {

                    @Override
                    public void onTabSelected(
                            TabLayout.Tab tab
                    ) {

                        switch (tab.getPosition()) {

                            case 0:

                                currentFeed =
                                        FeedType.FOLLOWING;

                                break;

                            case 1:

                                currentFeed =
                                        FeedType.FOR_YOU;

                                break;

                            case 2:

                                currentFeed =
                                        FeedType.GROUP;

                                break;
                        }

                        viewModel.loadFeed(currentFeed);
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

        // =====================================
        // CREATE POST
        // =====================================

        btnAddPost.setOnClickListener(v -> {

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(

                            R.id.fragmentContainer,

                            new CreatePostFragment()
                    )
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}