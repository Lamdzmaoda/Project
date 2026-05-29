package com.example.democode3.features.profile.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.democode3.R;
import com.example.democode3.core.session.TokenManager;
import com.example.democode3.features.auth.ui.activity.LoginActivity;
import com.example.democode3.features.profile.model.UserProfileResponse;
import com.example.democode3.features.profile.ui.adapter.ProfileGridPostAdapter;
import com.example.democode3.features.profile.ui.viewmodel.ProfileViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ProfileFragment
        extends Fragment {

    // =====================================
    // ARG
    // =====================================

    private static final String KEY_USER_ID =
            "user_id";

    // =====================================
    // USER
    // =====================================

    private String userId;

    // =====================================
    // VIEWMODEL
    // =====================================

    private ProfileViewModel viewModel;

    // =====================================
    // VIEW
    // =====================================

    private TextView txtAvatar;

    private TextView txtUsername;

    private TextView txtFullName;

    private TextView txtBio;

    private TextView txtFollowers;

    private TextView txtFollowing;

    private TextView txtPosts;

    private TextView txtStreak;

    private TextView txtVerified;

    private TextView txtLevel;

    private TextView txtRankTitle;

    private TextView txtXpProgress;

    private ProgressBar progressXp;

    private MaterialButton btnEdit;

    private MaterialButton btnLogout;

    private MaterialButton btnFollow;

    private TabLayout tabLayout;

    private RecyclerView recyclerPost;

    // =====================================
    // ADAPTER
    // =====================================

    private ProfileGridPostAdapter adapter;

    // =====================================
    // INSTANCE
    // =====================================

    public static ProfileFragment newInstance(
            String userId
    ) {

        ProfileFragment fragment =
                new ProfileFragment();

        Bundle bundle =
                new Bundle();

        bundle.putString(
                KEY_USER_ID,
                userId
        );

        fragment.setArguments(bundle);

        return fragment;
    }

    // =====================================
    // CREATE
    // =====================================

    @Nullable
    @Override
    public View onCreateView(

            @NonNull LayoutInflater inflater,

            @Nullable ViewGroup container,

            @Nullable Bundle savedInstanceState
    ) {

        View view =
                inflater.inflate(

                        R.layout.fragment_profile,

                        container,

                        false
                );

        initViews(view);

        getArgumentsData();

        setupRecycler();

        setupTabs();

        setupButtons();

        viewModel =
                new ViewModelProvider(this)
                        .get(ProfileViewModel.class);

        observeProfile();

        observePosts();

        observeFollow();

        observeCounts();

        loadProfile();

        return view;
    }

    // =====================================
    // INIT
    // =====================================

    private void initViews(
            View view
    ) {

        txtAvatar =
                view.findViewById(
                        R.id.txtAvatar
                );

        txtUsername =
                view.findViewById(
                        R.id.txtUsername
                );

        txtFullName =
                view.findViewById(
                        R.id.txtFullName
                );

        txtBio =
                view.findViewById(
                        R.id.txtBio
                );

        txtFollowers =
                view.findViewById(
                        R.id.txtFollowers
                );

        txtFollowing =
                view.findViewById(
                        R.id.txtFollowing
                );

        txtPosts =
                view.findViewById(
                        R.id.txtPosts
                );

        txtStreak =
                view.findViewById(
                        R.id.txtStreak
                );

        txtVerified =
                view.findViewById(
                        R.id.txtVerified
                );

        txtLevel =
                view.findViewById(
                        R.id.txtLevel
                );

        txtRankTitle =
                view.findViewById(
                        R.id.txtRankTitle
                );

        txtXpProgress =
                view.findViewById(
                        R.id.txtXpProgress
                );

        progressXp =
                view.findViewById(
                        R.id.progressXp
                );

        btnEdit =
                view.findViewById(
                        R.id.btnEdit
                );

        btnLogout =
                view.findViewById(
                        R.id.btnLogout
                );

        btnFollow =
                view.findViewById(
                        R.id.btnFollow
                );

        tabLayout =
                view.findViewById(
                        R.id.tabLayout
                );

        recyclerPost =
                view.findViewById(
                        R.id.recyclerPost
                );
    }

    // =====================================
    // ARG
    // =====================================

    private void getArgumentsData() {

        if (getArguments() == null)
            return;

        userId =
                getArguments()
                        .getString(
                                KEY_USER_ID
                        );
    }

    // =====================================
    // RECYCLER
    // =====================================

    private void setupRecycler() {

        adapter =
                new ProfileGridPostAdapter(
                        new ArrayList<>()
                );

        recyclerPost.setLayoutManager(

                new GridLayoutManager(

                        requireContext(),

                        2
                )
        );

        recyclerPost.setAdapter(adapter);
    }

    // =====================================
    // LOAD
    // =====================================

    private void loadProfile() {

        if (userId == null) {

            viewModel.loadMyProfile();
        }

        else {

            viewModel.loadUserProfile(
                    userId
            );
        }
    }

    // =====================================
    // OBSERVE PROFILE
    // =====================================

    private void observeProfile() {

        viewModel.getProfile().observe(

                getViewLifecycleOwner(),

                this::renderProfile
        );
    }

    // =====================================
    // POSTS
    // =====================================

    private void observePosts() {

        viewModel.getPosts().observe(

                getViewLifecycleOwner(),

                posts -> {

                    if (posts == null)
                        return;

                    txtPosts.setText(
                            String.valueOf(
                                    posts.size()
                            )
                    );

                    adapter.updateData(posts);
                }
        );
    }

    // =====================================
    // FOLLOW
    // =====================================

    private void observeFollow() {

        viewModel.isFollowed().observe(

                getViewLifecycleOwner(),

                followed -> {

                    if (followed == null)
                        return;

                    btnFollow.setText(

                            followed

                                    ?

                                    "Đang theo dõi"

                                    :

                                    "Theo dõi"
                    );
                }
        );
    }

    // =====================================
    // COUNTS
    // =====================================

    private void observeCounts() {

        viewModel.getFollowersCount().observe(

                getViewLifecycleOwner(),

                count -> {

                    txtFollowers.setText(
                            String.valueOf(count)
                    );
                }
        );

        viewModel.getFollowingCount().observe(

                getViewLifecycleOwner(),

                count -> {

                    txtFollowing.setText(
                            String.valueOf(count)
                    );
                }
        );
    }

    // =====================================
    // PROFILE
    // =====================================

    private void renderProfile(
            UserProfileResponse user
    ) {

        if (user == null)
            return;

        txtAvatar.setText(

                user.username
                        .substring(0, 1)
                        .toUpperCase()
        );

        txtUsername.setText(
                "@" + user.username
        );

        txtFullName.setText(
                user.displayName
        );

        txtBio.setText(
                user.bio
        );

        txtStreak.setText(

                "🔥 "
                        + user.streak
                        + " ngày"
        );

        txtVerified.setVisibility(

                user.verified

                        ?

                        View.VISIBLE

                        :

                        View.GONE
        );

        // =================================
        // MY PROFILE
        // =================================

        boolean isMyProfile =
                userId == null;

        btnEdit.setVisibility(

                isMyProfile

                        ?

                        View.VISIBLE

                        :

                        View.GONE
        );

        btnLogout.setVisibility(

                isMyProfile

                        ?

                        View.VISIBLE

                        :

                        View.GONE
        );

        btnFollow.setVisibility(

                isMyProfile

                        ?

                        View.GONE

                        :

                        View.VISIBLE
        );

        // =================================
        // LEVEL
        // =================================

        txtLevel.setText(
                "LV " + user.level
        );

        // =================================
        // TITLE
        // =================================

        String title;

        if (user.level >= 50) {

            title = "Huyền thoại";
        }

        else if (user.level >= 20) {

            title = "Bậc thầy";
        }

        else if (user.level >= 10) {

            title = "Lập trình viên";
        }

        else if (user.level >= 5) {

            title = "Khám phá";
        }

        else {

            title = "Người mới";
        }

        txtRankTitle.setText(
                "• " + title
        );

        // =================================
        // XP
        // =================================

        int nextXp =
                user.level * 100;

        if (nextXp <= 0) {

            nextXp = 100;
        }

        int currentXp =
                user.xp % nextXp;

        int progress =

                (int) (

                        (
                                currentXp * 100f
                        )

                                / nextXp
                );

        progressXp.setProgress(progress);

        txtXpProgress.setText(

                currentXp
                        + " / "
                        + nextXp
                        + " XP"
        );
    }

    // =====================================
    // TAB
    // =====================================

    private void setupTabs() {

        tabLayout.removeAllTabs();

        tabLayout.addTab(

                tabLayout.newTab()
                        .setText("Bài viết")
        );

        tabLayout.addTab(

                tabLayout.newTab()
                        .setText("Thành tựu")
        );

        tabLayout.addOnTabSelectedListener(

                new TabLayout.OnTabSelectedListener() {

                    @Override
                    public void onTabSelected(
                            TabLayout.Tab tab
                    ) {

                        if (tab == null)
                            return;

                        if (tab.getPosition() == 0) {

                            observePosts();
                        }

                        else {

                            Toast.makeText(

                                    requireContext(),

                                    "Thành tựu sẽ thêm sau 😭🔥",

                                    Toast.LENGTH_SHORT

                            ).show();
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
    // BUTTONS
    // =====================================

    private void setupButtons() {

        btnEdit.setOnClickListener(v -> {

            Toast.makeText(

                    requireContext(),

                    "Tính năng đang phát triển 😭🔥",

                    Toast.LENGTH_SHORT

            ).show();
        });

        btnLogout.setOnClickListener(v -> {

            TokenManager.clear(
                    requireContext()
            );

            Intent intent =
                    new Intent(

                            requireContext(),

                            LoginActivity.class
                    );

            intent.setFlags(

                    Intent.FLAG_ACTIVITY_NEW_TASK
                            |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);
        });

        btnFollow.setOnClickListener(v -> {

            Boolean followed =
                    viewModel.isFollowed()
                            .getValue();

            if (followed != null && followed) {

                viewModel.unfollowUser(
                        userId
                );
            }

            else {

                viewModel.followUser(
                        userId
                );
            }
        });
    }
}