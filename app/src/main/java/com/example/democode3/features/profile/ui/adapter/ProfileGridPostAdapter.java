package com.example.democode3.features.profile.ui.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.democode3.R;
import com.example.democode3.features.community.model.Post;
import com.example.democode3.features.community.ui.activity.PostDetailActivity;
import com.example.democode3.features.profile.ui.fragment.ProfileFragment;

import java.util.List;

public class ProfileGridPostAdapter
        extends RecyclerView.Adapter<
        ProfileGridPostAdapter.VH> {

    // =====================================
    // DATA
    // =====================================

    private final List<Post> list;

    // =====================================
    // CONSTRUCTOR
    // =====================================

    public ProfileGridPostAdapter(
            List<Post> list
    ) {

        this.list = list;
    }

    // =====================================
    // CREATE
    // =====================================

    @NonNull
    @Override
    public VH onCreateViewHolder(

            @NonNull ViewGroup parent,

            int viewType
    ) {

        View view =
                LayoutInflater.from(
                        parent.getContext()
                ).inflate(

                        R.layout.item_profile_grid_post,

                        parent,

                        false
                );

        return new VH(view);
    }

    // =====================================
    // BIND
    // =====================================

    @Override
    public void onBindViewHolder(

            @NonNull VH h,

            int position
    ) {

        Post post =
                list.get(position);

        // =================================
        // USERNAME
        // =================================

        if (post.username != null) {

            h.txtUsername.setText(
                    "@" + post.username
            );
        }

        // =================================
        // CONTENT
        // =================================

        h.txtContent.setText(
                post.content
        );

        // =================================
        // LIKE
        // =================================

        h.txtLike.setText(
                "❤ " + post.likeCount
        );

        // =================================
        // COMMENT
        // =================================

        h.txtComment.setText(
                "💬 " + post.commentCount
        );

        // =================================
        // OPEN POST
        // =================================

        h.itemView.setOnClickListener(v -> {

            Intent intent =
                    new Intent(

                            v.getContext(),

                            PostDetailActivity.class
                    );

            intent.putExtra(
                    "post",
                    post
            );

            v.getContext()
                    .startActivity(intent);
        });

        // =================================
        // OPEN PROFILE
        // =================================

        h.txtUsername.setOnClickListener(v -> {

            if (post.userId == null)
                return;

            androidx.fragment.app.FragmentActivity activity =
                    (androidx.fragment.app.FragmentActivity)
                            v.getContext();

            activity.getSupportFragmentManager()

                    .beginTransaction()

                    .replace(

                            R.id.fragmentContainer,

                            ProfileFragment.newInstance(
                                    post.userId
                            )
                    )

                    .addToBackStack(null)

                    .commit();
        });
    }

    // =====================================
    // UPDATE
    // =====================================

    public void updateData(
            List<Post> posts
    ) {

        list.clear();

        list.addAll(posts);

        notifyDataSetChanged();
    }

    // =====================================
    // COUNT
    // =====================================

    @Override
    public int getItemCount() {

        return list.size();
    }

    // =====================================
    // HOLDER
    // =====================================

    static class VH
            extends RecyclerView.ViewHolder {

        TextView txtUsername;

        TextView txtContent;

        TextView txtLike;

        TextView txtComment;

        public VH(
                @NonNull View itemView
        ) {

            super(itemView);

            txtUsername =
                    itemView.findViewById(
                            R.id.txtUsername
                    );

            txtContent =
                    itemView.findViewById(
                            R.id.txtContent
                    );

            txtLike =
                    itemView.findViewById(
                            R.id.txtLike
                    );

            txtComment =
                    itemView.findViewById(
                            R.id.txtComment
                    );
        }
    }
}