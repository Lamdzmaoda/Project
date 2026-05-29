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
    // HOLDER
    // =====================================

    static class VH
            extends RecyclerView.ViewHolder {

        TextView txtAvatar;

        TextView txtUsername;

        TextView txtContent;

        TextView txtLike;

        TextView txtComment;

        TextView txtTime;

        public VH(
                @NonNull View itemView
        ) {

            super(itemView);

            txtAvatar =
                    itemView.findViewById(
                            R.id.txtAvatar
                    );

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

            txtTime =
                    itemView.findViewById(
                            R.id.txtTime
                    );
        }
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
        // AVATAR
        // =================================

        if (

                post.username != null

                        &&

                        !post.username.isEmpty()
        ) {

            h.txtAvatar.setText(

                    post.username
                            .substring(0, 1)
                            .toUpperCase()
            );
        }

        // =================================
        // USERNAME
        // =================================

        h.txtUsername.setText(

                "@"
                        + post.username
        );

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

                "❤ "
                        + post.likeCount
        );

        // =================================
        // COMMENT
        // =================================

        h.txtComment.setText(

                "💬 "
                        + post.commentCount
        );

        // =================================
        // TIME
        // =================================

        if (post.createdAt != null) {

            h.txtTime.setText(
                    post.createdAt
            );
        }

        // =================================
        // OPEN DETAIL
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
}