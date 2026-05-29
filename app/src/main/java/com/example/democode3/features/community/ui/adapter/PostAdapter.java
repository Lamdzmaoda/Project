package com.example.democode3.features.community.ui.adapter;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.democode3.R;
import com.example.democode3.features.community.model.Post;

import java.util.List;

public class PostAdapter
        extends RecyclerView.Adapter<PostAdapter.VH> {

    private final Context context;

    private final List<Post> posts;

    private final OnPostActionListener listener;

    public interface OnPostActionListener {

        void onOpenProfile(
                String userId
        );

        void onOpenComment(
                Post post
        );
    }

    public PostAdapter(

            Context context,

            List<Post> posts,

            OnPostActionListener listener
    ) {

        this.context = context;

        this.posts = posts;

        this.listener = listener;
    }

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

    @Override
    public void onBindViewHolder(

            @NonNull VH holder,

            int position
    ) {

        Post post =
                posts.get(position);

        // USER

        holder.txtUsername.setText(
                safe(post.username)
        );

        holder.txtAvatar.setText(
                avatar(post.username)
        );

        // CONTENT

        holder.txtContent.setText(
                safe(post.content)
        );

        // TIME

        holder.txtTime.setText(
                "2 giờ trước"
        );

        // LEVEL

        holder.txtLevel.setText(
                "Lv.12"
        );

        // LIKE

        holder.btnLike.setText(
                "❤️ " + post.likeCount
        );

        // COMMENT

        holder.btnComment.setText(
                "💬 " + post.commentCount
        );

        // CODE

        if (
                post.codeSnippet != null
                        &&
                        !post.codeSnippet.trim().isEmpty()
        ) {

            holder.layoutCode.setVisibility(
                    View.VISIBLE
            );

            holder.txtCode.setText(
                    post.codeSnippet
            );

        } else {

            holder.layoutCode.setVisibility(
                    View.GONE
            );
        }

        // IMAGE

        if (
                post.imageUrl != null
                        &&
                        !post.imageUrl.trim().isEmpty()
        ) {

            holder.imgPost.setVisibility(
                    View.VISIBLE
            );

            try {

                holder.imgPost.setImageURI(
                        Uri.parse(post.imageUrl)
                );

            } catch (Exception e) {

                holder.imgPost.setVisibility(
                        View.GONE
                );
            }

        } else {

            holder.imgPost.setVisibility(
                    View.GONE
            );
        }

        // COPY

        holder.btnCopyCode.setOnClickListener(v -> {

            ClipboardManager clipboard =
                    (ClipboardManager)

                            context.getSystemService(
                                    Context.CLIPBOARD_SERVICE
                            );

            ClipData clip =
                    ClipData.newPlainText(
                            "code",
                            holder.txtCode.getText()
                    );

            clipboard.setPrimaryClip(clip);

            Toast.makeText(
                    context,
                    "Đã copy code",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // MENU

        holder.btnMore.setOnClickListener(v -> {

            Dialog dialog =
                    new Dialog(
                            context,
                            android.R.style.Theme_Black_NoTitleBar_Fullscreen
                    );

            dialog.setContentView(
                    R.layout.dialog_post_menu
            );

            if (dialog.getWindow() != null) {

                dialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );
            }

            dialog.setCancelable(true);

            TextView btnCancel =
                    dialog.findViewById(
                            R.id.btnCancel
                    );

            if (btnCancel != null) {

                btnCancel.setOnClickListener(v2 -> {

                    dialog.dismiss();
                });
            }

            dialog.show();
        });

        // PROFILE

        holder.txtAvatar.setOnClickListener(v -> {

            if (listener != null) {

                listener.onOpenProfile(
                        post.userId
                );
            }
        });

        holder.txtUsername.setOnClickListener(v -> {

            if (listener != null) {

                listener.onOpenProfile(
                        post.userId
                );
            }
        });

        // COMMENT

        holder.btnComment.setOnClickListener(v -> {

            if (listener != null) {

                listener.onOpenComment(
                        post
                );
            }
        });
    }

    @Override
    public int getItemCount() {

        return posts == null
                ? 0
                : posts.size();
    }

    private String safe(
            String text
    ) {

        if (
                text == null
                        ||
                        text.trim().isEmpty()
        ) {

            return "";
        }

        return text;
    }

    private String avatar(
            String text
    ) {

        if (
                text == null
                        ||
                        text.trim().isEmpty()
        ) {

            return "?";
        }

        return text
                .substring(0, 1)
                .toUpperCase();
    }

    class VH extends RecyclerView.ViewHolder {

        TextView txtAvatar;

        TextView txtUsername;

        TextView txtLevel;

        TextView txtTime;

        TextView txtContent;

        TextView txtCode;

        TextView txtCodeLanguage;

        TextView btnCopyCode;

        TextView btnLike;

        TextView btnComment;

        TextView btnShare;

        TextView btnMore;

        ImageView imgPost;

        LinearLayout layoutTags;

        LinearLayout layoutCode;

        VH(View v) {

            super(v);

            txtAvatar =
                    v.findViewById(R.id.txtAvatar);

            txtUsername =
                    v.findViewById(R.id.txtUsername);

            txtLevel =
                    v.findViewById(R.id.txtLevel);

            txtTime =
                    v.findViewById(R.id.txtTime);

            txtContent =
                    v.findViewById(R.id.txtContent);

            txtCode =
                    v.findViewById(R.id.txtCode);

            txtCodeLanguage =
                    v.findViewById(R.id.txtCodeLanguage);

            btnCopyCode =
                    v.findViewById(R.id.btnCopyCode);

            btnLike =
                    v.findViewById(R.id.btnLike);

            btnComment =
                    v.findViewById(R.id.btnComment);

            btnShare =
                    v.findViewById(R.id.btnShare);

            btnMore =
                    v.findViewById(R.id.btnMore);

            imgPost =
                    v.findViewById(R.id.imgPost);

            layoutTags =
                    v.findViewById(R.id.layoutTags);

            layoutCode =
                    v.findViewById(R.id.layoutCode);
        }
    }
}