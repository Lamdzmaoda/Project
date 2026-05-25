package com.example.democode3.features.community.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.democode3.R;
import com.example.democode3.features.community.model.Comment;

import java.util.List;

public class CommentAdapter
        extends RecyclerView.Adapter<CommentAdapter.VH> {

    // =====================================
    // LISTENER
    // =====================================

    public interface OnCommentActionListener {

        void onOpenProfile(String userId);

        void onReply(Comment comment);
    }

    // =====================================
    // DATA
    // =====================================

    Context context;

    List<Comment> list;

    OnCommentActionListener listener;

    public CommentAdapter(
            Context context,
            List<Comment> list,
            OnCommentActionListener listener
    ) {

        this.context = context;

        this.list = list;

        this.listener = listener;
    }

    // =====================================
    // HOLDER
    // =====================================

    static class VH
            extends RecyclerView.ViewHolder {

        TextView txtAvatar;

        TextView txtUsername;

        TextView txtTime;

        TextView txtComment;

        TextView txtViewReplies;

        TextView btnReply;

        TextView btnLike;

        public VH(View v) {

            super(v);

            txtAvatar =
                    v.findViewById(R.id.txtAvatar);

            txtUsername =
                    v.findViewById(R.id.txtUsername);

            txtTime =
                    v.findViewById(R.id.txtTime);

            txtComment =
                    v.findViewById(R.id.txtComment);

            txtViewReplies =
                    v.findViewById(R.id.txtViewReplies);

            btnReply =
                    v.findViewById(R.id.btnReply);

            btnLike =
                    v.findViewById(R.id.btnLike);
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
                                R.layout.item_comment,
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

        Comment c = list.get(position);

        // =====================================
        // REPLY UI
        // =====================================

        RecyclerView.LayoutParams params =
                (RecyclerView.LayoutParams)
                        h.itemView.getLayoutParams();

        if (c.parentId != null) {

            params.setMarginStart(90);

        } else {

            params.setMarginStart(0);
        }

        h.itemView.setLayoutParams(params);

        // =====================================
        // USERNAME
        // =====================================

        h.txtUsername.setText(
                c.username
        );

        // =====================================
        // AVATAR
        // =====================================

        String username = "U";

        if (

                c.username != null

                        &&

                        !c.username.isEmpty()
        ) {

            username = c.username;
        }

        h.txtAvatar.setText(
                username.substring(0, 1).toUpperCase()
        );

        // =====================================
        // COMMENT
        // =====================================

        h.txtComment.setText(
                c.content
        );

        // =====================================
        // TIME
        // =====================================

        if (

                c.createdAt != null

                        &&

                        !c.createdAt.isEmpty()
        ) {

            h.txtTime.setText(c.createdAt);

        } else {

            h.txtTime.setText("Vừa xong");
        }

        // =====================================
        // VIEW REPLIES
        // =====================================

        h.txtViewReplies.setVisibility(
                View.GONE
        );

        // =====================================
        // LIKE
        // =====================================

        h.btnLike.setText(
                "Thích"
        );

        h.btnLike.setOnClickListener(v -> {

            Toast.makeText(

                    context,

                    "Đã thích bình luận",

                    Toast.LENGTH_SHORT

            ).show();
        });

        // =====================================
        // REPLY
        // =====================================

        h.btnReply.setOnClickListener(v -> {

            if (listener != null) {

                listener.onReply(c);
            }
        });

        // =====================================
        // PROFILE
        // =====================================

        View.OnClickListener profileListener =
                v -> {

                    if (listener != null) {

                        listener.onOpenProfile(
                                c.userId
                        );
                    }
                };

        h.txtAvatar.setOnClickListener(
                profileListener
        );

        h.txtUsername.setOnClickListener(
                profileListener
        );
    }

    // =====================================
    // COUNT
    // =====================================

    @Override
    public int getItemCount() {

        return list.size();
    }
}