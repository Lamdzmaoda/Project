package com.example.democode3.features.profile.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.democode3.R;

import com.example.democode3.features.community.model.Post;

import com.example.democode3.features.community.ui.adapter.PostAdapter;

import java.util.List;

public class ProfilePostAdapter
        extends RecyclerView.Adapter<
        ProfilePostAdapter.VH> {

    // =====================================
    // DATA
    // =====================================

    private final Context context;

    private List<Post> list;

    private final PostAdapter.OnPostActionListener
            listener;

    // =====================================
    // CONSTRUCTOR
    // =====================================

    public ProfilePostAdapter(

            Context context,

            List<Post> list,

            PostAdapter.OnPostActionListener listener
    ) {

        this.context = context;

        this.list = list;

        this.listener = listener;
    }

    // =====================================
    // HOLDER
    // =====================================

    public static class VH
            extends RecyclerView.ViewHolder {

        TextView txtAvatar;

        TextView txtUsername;

        TextView txtContent;

        TextView btnComment;

        TextView btnLike;

        TextView btnSave;

        ImageView imgPost;

        TextView btnMore;

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

            btnComment =
                    itemView.findViewById(
                            R.id.btnComment
                    );

            btnLike =
                    itemView.findViewById(
                            R.id.btnLike
                    );

            btnSave =
                    itemView.findViewById(
                            R.id.btnSave
                    );

            imgPost =
                    itemView.findViewById(
                            R.id.imgPost
                    );

            btnMore =
                    itemView.findViewById(
                            R.id.btnMore
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
                LayoutInflater.from(context)
                        .inflate(

                                R.layout.item_post,

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

        Post p =
                list.get(position);

        // =================================
        // AVATAR
        // =================================

        if (

                p.username != null

                        &&

                        !p.username.isEmpty()
        ) {

            h.txtAvatar.setText(

                    p.username
                            .substring(0, 1)
                            .toUpperCase()
            );
        }

        // =================================
        // USERNAME
        // =================================

        if (p.username != null) {

            h.txtUsername.setText(
                    p.username
            );
        }

        // =================================
        // CONTENT
        // =================================

        h.txtContent.setText(
                p.content
        );

        // =================================
        // IMAGE
        // =================================

        if (

                p.imageUrl != null

                        &&

                        !p.imageUrl.isEmpty()
        ) {

            h.imgPost.setVisibility(
                    View.VISIBLE
            );

            Glide.with(context)
                    .load(p.imageUrl)
                    .into(h.imgPost);
        }

        else {

            h.imgPost.setVisibility(
                    View.GONE
            );
        }

        // =================================
        // LIKE
        // =================================

        h.btnLike.setText(
                (p.likedByMe ? "❤️ " : "🤍 ")
                        + p.likeCount
        );

        // =================================
        // COMMENT
        // =================================

        h.btnComment.setText(
                "💬 " + p.commentCount
        );

        // =================================
        // SAVE
        // =================================

        if (p.savedByMe) {

            h.btnSave.setText("🔖");
        }

        else {

            h.btnSave.setText("📑");
        }

        // =================================
        // OPEN COMMENT
        // =================================

        h.btnComment.setOnClickListener(v -> {

            listener.onOpenComment(p);
        });

        // =================================
        // OPEN POST
        // =================================

        h.itemView.setOnClickListener(v -> {

            listener.onOpenPost(p);
        });

        // =================================
        // OPEN PROFILE
        // =================================

        h.txtUsername.setOnClickListener(v -> {

            if (p.userId != null) {

                listener.onOpenProfile(
                        p.userId
                );
            }
        });

        // =================================
        // LIKE CLICK
        // =================================

        h.btnLike.setOnClickListener(v -> {

            p.likedByMe = !p.likedByMe;

            if (p.likedByMe) {

                p.likeCount++;
            }

            else {

                if (p.likeCount > 0) {

                    p.likeCount--;
                }
            }

            notifyItemChanged(position);
        });

        // =================================
        // SAVE CLICK
        // =================================

        h.btnSave.setOnClickListener(v -> {

            p.savedByMe = !p.savedByMe;

            notifyItemChanged(position);

            if (p.savedByMe) {

                h.btnSave.setText("🔖");
            }

            else {

                h.btnSave.setText("📑");
            }
        });
    }

    // =====================================
    // COUNT
    // =====================================

    @Override
    public int getItemCount() {

        return list.size();
    }

    // =====================================
    // UPDATE
    // =====================================

    public void updateData(
            List<Post> newList
    ) {

        this.list = newList;

        notifyDataSetChanged();
    }
}