package com.example.democode3.features.community.ui.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.democode3.R;

import com.example.democode3.features.community.model.Post;
import com.example.democode3.features.community.repository.CommunityRepository;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAdapter
        extends RecyclerView.Adapter<PostAdapter.VH> {

    // =====================================
    // LISTENER
    // =====================================

    public interface OnPostActionListener {

        void onOpenProfile(String userId);

        void onOpenComment(Post post);

        void onOpenPost(Post post);
    }

    // =====================================
    // CONTEXT
    // =====================================

    Context context;

    // =====================================
    // LIST
    // =====================================

    List<Post> list;

    // =====================================
    // LISTENER
    // =====================================

    OnPostActionListener listener;

    // =====================================
    // REPOSITORY
    // =====================================

    CommunityRepository repository;

    // =====================================
    // CONSTRUCTOR
    // =====================================

    public PostAdapter(

            Context context,

            List<Post> list,

            OnPostActionListener listener
    ) {

        this.context = context;

        this.list = list;

        this.listener = listener;

        repository =
                new CommunityRepository(
                        context
                );
    }

    // =====================================
    // VIEW HOLDER
    // =====================================

    static class VH
            extends RecyclerView.ViewHolder {

        TextView txtAvatar;

        TextView txtUsername;

        TextView txtTime;

        TextView txtContent;

        TextView btnLike;

        TextView btnComment;

        TextView btnSave;

        TextView btnMore;

        ImageView imgPost;

        HorizontalScrollView layoutCode;

        TextView txtCode;

        TextView btnCopyCode;

        TextView txtCodeLanguage;

        public VH(View v) {

            super(v);

            txtAvatar =
                    v.findViewById(R.id.txtAvatar);

            txtUsername =
                    v.findViewById(R.id.txtUsername);

            txtTime =
                    v.findViewById(R.id.txtTime);

            txtContent =
                    v.findViewById(R.id.txtContent);

            btnLike =
                    v.findViewById(R.id.btnLike);

            btnComment =
                    v.findViewById(R.id.btnComment);

            btnSave =
                    v.findViewById(R.id.btnSave);

            btnMore =
                    v.findViewById(R.id.btnMore);

            imgPost =
                    v.findViewById(R.id.imgPost);

            layoutCode =
                    v.findViewById(R.id.layoutCode);

            txtCode =
                    v.findViewById(R.id.txtCode);

            btnCopyCode =
                    v.findViewById(R.id.btnCopyCode);

            txtCodeLanguage =
                    v.findViewById(R.id.txtCodeLanguage);
        }
    }

    // =====================================
    // CREATE VIEW
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

        Post p = list.get(position);
        int adapterPosition =
                h.getAdapterPosition();

        if (adapterPosition == RecyclerView.NO_POSITION)
            return;

        // =====================================
        // USERNAME
        // =====================================

        h.txtUsername.setText(
                p.username
        );

        // =====================================
        // AVATAR
        // =====================================

        String username = "U";

        if (
                p.username != null
                        &&
                        p.username != null
                        &&
                        !p.username.isEmpty()
        ) {

            username = p.username;
        }

        h.txtAvatar.setText(
                username.substring(0, 1).toUpperCase()
        );

        // =====================================
        // CONTENT
        // =====================================

        h.txtContent.setText(
                p.content
        );

        // =====================================
        // TIME
        // =====================================

        h.txtTime.setText("2 giờ");

        // =====================================
        // IMAGE
        // =====================================

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

        } else {

            h.imgPost.setVisibility(
                    View.GONE
            );
        }

        // =====================================
        // CODE SNIPPET
        // =====================================

        if (
                p.codeSnippet != null
                        &&
                        !p.codeSnippet.isEmpty()
        ) {

            h.layoutCode.setVisibility(
                    View.VISIBLE
            );

            h.txtCode.setText(
                    p.codeSnippet
            );

            // =================================
            // LANGUAGE
            // =================================

            if (p.codeSnippet.contains("def ")) {

                h.txtCodeLanguage.setText(
                        "PYTHON"
                );

            }

            else if (
                    p.codeSnippet.contains("System.out")
            ) {

                h.txtCodeLanguage.setText(
                        "JAVA"
                );

            }

            else {

                h.txtCodeLanguage.setText(
                        "CODE"
                );
            }

            // =================================
            // COPY
            // =================================

            h.btnCopyCode.setOnClickListener(v -> {

                ClipboardManager clipboard =

                        (ClipboardManager)

                                context.getSystemService(
                                        Context.CLIPBOARD_SERVICE
                                );

                ClipData clip =

                        ClipData.newPlainText(
                                "code",
                                p.codeSnippet
                        );

                clipboard.setPrimaryClip(clip);

                Toast.makeText(

                        context,

                        "Đã sao chép code",

                        Toast.LENGTH_SHORT

                ).show();
            });

        }

        else {

            h.layoutCode.setVisibility(
                    View.GONE
            );
        }

        // =====================================
        // LIKE
        // =====================================

        h.btnLike.setText(
                (p.likedByMe ? "❤️ " : "🤍 ")
                        + p.likeCount
        );

        h.btnLike.setOnClickListener(v -> {

            if (!p.likedByMe) {

                repository.likePost(

                        String.valueOf(p.id),

                        new Callback<Void>() {

                            @Override
                            public void onResponse(

                                    Call<Void> call,

                                    Response<Void> response
                            ) {

                                p.likedByMe = true;

                                p.likeCount++;

                                notifyItemChanged(adapterPosition);
                            }

                            @Override
                            public void onFailure(

                                    Call<Void> call,

                                    Throwable t
                            ) {

                                Toast.makeText(

                                        context,

                                        t.getMessage(),

                                        Toast.LENGTH_SHORT

                                ).show();
                            }
                        }
                );

            } else {

                repository.unlikePost(

                        String.valueOf(p.id),

                        new Callback<Void>() {

                            @Override
                            public void onResponse(

                                    Call<Void> call,

                                    Response<Void> response
                            ) {

                                p.likedByMe = false;

                                p.likeCount--;

                                notifyItemChanged(adapterPosition);
                            }

                            @Override
                            public void onFailure(

                                    Call<Void> call,

                                    Throwable t
                            ) {

                                Toast.makeText(

                                        context,

                                        t.getMessage(),

                                        Toast.LENGTH_SHORT

                                ).show();
                            }
                        }
                );
            }
        });

        // =====================================
        // COMMENT
        // =====================================

        h.btnComment.setText(
                "💬 " + p.commentCount
        );

        h.btnComment.setOnClickListener(v -> {

            listener.onOpenComment(p);
        });

        // =====================================
        // SAVE
        // =====================================

        h.btnSave.setText(
                p.savedByMe ? "🔖" : "📑"
        );

        h.btnSave.setOnClickListener(v -> {

            if (!p.savedByMe) {

                repository.savePost(

                        String.valueOf(p.id),

                        new Callback<Void>() {

                            @Override
                            public void onResponse(

                                    Call<Void> call,

                                    Response<Void> response
                            ) {

                                p.savedByMe = true;

                                notifyItemChanged(adapterPosition);
                            }

                            @Override
                            public void onFailure(

                                    Call<Void> call,

                                    Throwable t
                            ) {

                                Toast.makeText(

                                        context,

                                        t.getMessage(),

                                        Toast.LENGTH_SHORT

                                ).show();
                            }
                        }
                );

            } else {

                repository.unsavePost(

                        String.valueOf(p.id),

                        new Callback<Void>() {

                            @Override
                            public void onResponse(

                                    Call<Void> call,

                                    Response<Void> response
                            ) {

                                p.savedByMe = false;

                                notifyItemChanged(adapterPosition);
                            }

                            @Override
                            public void onFailure(

                                    Call<Void> call,

                                    Throwable t
                            ) {

                                Toast.makeText(

                                        context,

                                        t.getMessage(),

                                        Toast.LENGTH_SHORT

                                ).show();
                            }
                        }
                );
            }
        });

        // =====================================
        // OPEN PROFILE
        // =====================================

        View.OnClickListener openProfileListener =

                v -> listener.onOpenProfile(
                        p.userId
                );

        h.txtAvatar.setOnClickListener(
                openProfileListener
        );

        h.txtUsername.setOnClickListener(
                openProfileListener
        );

        // =====================================
        // OPEN POST
        // =====================================

        h.imgPost.setOnClickListener(v -> {

            listener.onOpenPost(p);
        });

        // =====================================
// MENU
// =====================================

        h.btnMore.setOnClickListener(v -> {

            BottomSheetDialog dialog =
                    new BottomSheetDialog(context);

            View sheet =

                    LayoutInflater
                            .from(context)
                            .inflate(
                                    R.layout.dialog_post_menu,
                                    null
                            );

            dialog.setContentView(sheet);

            TextView btnReport =
                    sheet.findViewById(
                            R.id.btnReport
                    );

            TextView btnNotInterested =
                    sheet.findViewById(
                            R.id.btnNotInterested
                    );

            TextView btnGoToPost =
                    sheet.findViewById(
                            R.id.btnGoToPost
                    );

            TextView btnShare =
                    sheet.findViewById(
                            R.id.btnShare
                    );

            TextView btnCopyLink =
                    sheet.findViewById(
                            R.id.btnCopyLink
                    );

            TextView btnEmbed =
                    sheet.findViewById(
                            R.id.btnEmbed
                    );

            TextView btnAboutAccount =
                    sheet.findViewById(
                            R.id.btnAboutAccount
                    );

            TextView btnCancel =
                    sheet.findViewById(
                            R.id.btnCancel
                    );

            // =====================================
            // OWNER MENU
            // =====================================

            boolean isOwner =
                    p.username != null
                            &&
                            p.username.equalsIgnoreCase(
                                    "dantew"
                            );

            if (isOwner) {

                btnReport.setText(
                        "Xóa bài viết"
                );

                btnReport.setTextColor(
                        0xFFF43F5E
                );

            } else {

                btnReport.setText(
                        "Báo cáo"
                );
            }

            // =====================================
            // REPORT / DELETE
            // =====================================

            btnReport.setOnClickListener(v1 -> {

                if (isOwner) {

                    repository.deletePost(

                            String.valueOf(p.id),

                            new Callback<Void>() {

                                @Override
                                public void onResponse(

                                        Call<Void> call,

                                        Response<Void> response
                                ) {

                                    int pos =
                                            h.getAdapterPosition();

                                    if (
                                            pos != RecyclerView.NO_POSITION
                                    ) {

                                        list.remove(pos);

                                        notifyItemRemoved(pos);

                                        notifyItemRangeChanged(
                                                pos,
                                                list.size()
                                        );
                                    }

                                    Toast.makeText(

                                            context,

                                            "Đã xóa bài viết",

                                            Toast.LENGTH_SHORT

                                    ).show();
                                }

                                @Override
                                public void onFailure(

                                        Call<Void> call,

                                        Throwable t
                                ) {

                                    Toast.makeText(

                                            context,

                                            t.getMessage(),

                                            Toast.LENGTH_SHORT

                                    ).show();
                                }
                            }
                    );

                } else {

                    Toast.makeText(

                            context,

                            "Đã báo cáo",

                            Toast.LENGTH_SHORT

                    ).show();
                }

                dialog.dismiss();
            });

            // =====================================
            // NOT INTERESTED
            // =====================================

            btnNotInterested.setOnClickListener(v1 -> {

                int pos =
                        h.getAdapterPosition();

                if (
                        pos != RecyclerView.NO_POSITION
                ) {

                    list.remove(pos);

                    notifyItemRemoved(pos);
                }

                Toast.makeText(

                        context,

                        "Ẩn bài viết",

                        Toast.LENGTH_SHORT

                ).show();

                dialog.dismiss();
            });

            // =====================================
            // GO TO POST
            // =====================================

            btnGoToPost.setOnClickListener(v1 -> {

                listener.onOpenPost(p);

                dialog.dismiss();
            });

            // =====================================
            // SHARE
            // =====================================

            btnShare.setOnClickListener(v1 -> {

                Toast.makeText(

                        context,

                        "Chia sẻ bài viết",

                        Toast.LENGTH_SHORT

                ).show();

                dialog.dismiss();
            });

            // =====================================
            // COPY LINK
            // =====================================

            btnCopyLink.setOnClickListener(v1 -> {

                ClipboardManager clipboard =

                        (ClipboardManager)

                                context.getSystemService(
                                        Context.CLIPBOARD_SERVICE
                                );

                ClipData clip =

                        ClipData.newPlainText(

                                "link",

                                "https://myapp/post/" + p.id
                        );

                clipboard.setPrimaryClip(clip);

                Toast.makeText(

                        context,

                        "Đã sao chép link",

                        Toast.LENGTH_SHORT

                ).show();

                dialog.dismiss();
            });

            // =====================================
            // EMBED
            // =====================================

            btnEmbed.setOnClickListener(v1 -> {

                Toast.makeText(

                        context,

                        "Nhúng bài viết",

                        Toast.LENGTH_SHORT

                ).show();

                dialog.dismiss();
            });

            // =====================================
            // ABOUT ACCOUNT
            // =====================================

            btnAboutAccount.setOnClickListener(v1 -> {

                listener.onOpenProfile(
                        p.userId
                );

                dialog.dismiss();
            });

            // =====================================
            // CANCEL
            // =====================================

            btnCancel.setOnClickListener(v1 -> {

                dialog.dismiss();
            });

            dialog.show();
        });
    }

    // =====================================
    // COUNT
    // =====================================

    @Override
    public int getItemCount() {

        return list.size();
    }
}