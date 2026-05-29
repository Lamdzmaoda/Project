package com.example.democode3.features.community.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.democode3.R;
import com.example.democode3.features.community.model.Comment;
import com.example.democode3.features.community.model.Post;
import com.example.democode3.features.community.ui.adapter.CommentAdapter;
import com.example.democode3.features.fake.session.FakeSessionManager;

import java.util.ArrayList;
import java.util.List;

public class PostDetailActivity
        extends AppCompatActivity {

    // =====================================
    // VIEW
    // =====================================

    private ImageView btnBack;

    private TextView txtAvatar;

    private TextView txtUsername;

    private TextView txtTime;

    private TextView txtContent;

    private ImageView imgPost;

    private TextView txtCode;

    private View layoutCode;

    private TextView btnLike;

    private TextView btnComment;

    private TextView btnSave;

    private RecyclerView recyclerComment;

    private EditText edtComment;

    private TextView btnSend;

    // =====================================
    // DATA
    // =====================================

    private Post post;

    private String postId;

    // =====================================
    // COMMENT
    // =====================================

    private final List<Comment> commentList =
            new ArrayList<>();

    private CommentAdapter adapter;

    // =====================================
    // SESSION
    // =====================================

    private final FakeSessionManager session =
            FakeSessionManager.getInstance();

    // =====================================
    // ON CREATE
    // =====================================

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_post_detail
        );

        post =
                (Post) getIntent()
                        .getSerializableExtra(
                                "post"
                        );

        if (post != null) {

            postId =
                    String.valueOf(post.id);
        }

        initView();

        setupRecycler();

        setupPost();

        setupClick();

        loadComments();
    }

    // =====================================
    // INIT
    // =====================================

    private void initView() {

        btnBack =
                findViewById(R.id.btnBack);

        txtAvatar =
                findViewById(R.id.txtAvatar);

        txtUsername =
                findViewById(R.id.txtUsername);

        txtTime =
                findViewById(R.id.txtTime);

        txtContent =
                findViewById(R.id.txtContent);

        imgPost =
                findViewById(R.id.imgPost);

        txtCode =
                findViewById(R.id.txtCode);

        layoutCode =
                findViewById(R.id.layoutCode);

        btnLike =
                findViewById(R.id.btnLike);

        btnComment =
                findViewById(R.id.btnComment);

        btnSave =
                findViewById(R.id.btnSave);

        recyclerComment =
                findViewById(R.id.recyclerComment);

        edtComment =
                findViewById(R.id.edtComment);

        btnSend =
                findViewById(R.id.btnSend);
    }

    // =====================================
    // RECYCLER
    // =====================================

    private void setupRecycler() {

        adapter =
                new CommentAdapter(

                        this,

                        commentList,

                        null
                );

        recyclerComment.setLayoutManager(

                new LinearLayoutManager(this)
        );

        recyclerComment.setAdapter(adapter);
    }

    // =====================================
    // POST
    // =====================================

    private void setupPost() {

        if (post == null)
            return;

        // ================================
        // AVATAR
        // ================================

        if (

                post.username != null

                        &&

                        !post.username.isEmpty()
        ) {

            txtAvatar.setText(

                    post.username
                            .substring(0, 1)
                            .toUpperCase()
            );
        }

        // ================================
        // USERNAME
        // ================================

        txtUsername.setText(
                post.username
        );

        // ================================
        // CONTENT
        // ================================

        txtContent.setText(
                post.content
        );

        // ================================
        // TIME
        // ================================

        txtTime.setText(
                post.createdAt
        );

        // ================================
        // LIKE
        // ================================

        updateLikeUI();

        // ================================
        // COMMENT
        // ================================

        post.commentCount =
                session.getFakeComments(
                        postId
                ).size();

        btnComment.setText(
                "💬 " + post.commentCount
        );

        // ================================
        // SAVE
        // ================================

        updateSaveUI();

        // ================================
        // IMAGE
        // ================================

        if (

                post.imageUrl != null

                        &&

                        !post.imageUrl.isEmpty()
        ) {

            imgPost.setVisibility(
                    View.VISIBLE
            );

            Glide.with(this)

                    .load(post.imageUrl)

                    .into(imgPost);

        }

        else {

            imgPost.setVisibility(
                    View.GONE
            );
        }

        // ================================
        // CODE
        // ================================

        if (

                post.codeSnippet != null

                        &&

                        !post.codeSnippet.isEmpty()
        ) {

            layoutCode.setVisibility(
                    View.VISIBLE
            );

            txtCode.setText(
                    post.codeSnippet
            );
        }

        else {

            layoutCode.setVisibility(
                    View.GONE
            );
        }
    }

    // =====================================
    // CLICK
    // =====================================

    private void setupClick() {

        btnBack.setOnClickListener(
                v -> finish()
        );

        // ================================
        // LIKE
        // ================================

        btnLike.setOnClickListener(v -> {

            post.likedByMe =
                    !post.likedByMe;

            if (post.likedByMe) {

                session.likePost(post.id);

            } else {

                session.unlikePost(post.id);
            }

            updateLikeUI();
        });

        // ================================
        // SAVE
        // ================================

        btnSave.setOnClickListener(v -> {

            post.savedByMe =
                    !post.savedByMe;

            if (post.savedByMe) {

                session.savePost(post.id);

            } else {

                session.unsavePost(post.id);
            }

            updateSaveUI();
        });

        // ================================
        // SEND COMMENT
        // ================================

        btnSend.setOnClickListener(v -> {

            String content =

                    edtComment.getText()
                            .toString()
                            .trim();

            if (content.isEmpty()) {

                Toast.makeText(

                        this,

                        "Nhập bình luận",

                        Toast.LENGTH_SHORT

                ).show();

                return;
            }

            session.addComment(
                    postId,
                    content
            );

            edtComment.setText("");

            loadComments();

            post.commentCount =
                    session.getFakeComments(
                            postId
                    ).size();

            btnComment.setText(
                    "💬 " + post.commentCount
            );

            recyclerComment.scrollToPosition(0);
        });
    }

    // =====================================
    // LOAD COMMENTS
    // =====================================

    private void loadComments() {

        commentList.clear();

        commentList.addAll(

                session.getFakeComments(
                        postId
                )
        );

        adapter.notifyDataSetChanged();
    }

    // =====================================
    // LIKE UI
    // =====================================

    private void updateLikeUI() {

        post.likeCount =
                session.getPostLikeCount(
                        post.id
                );

        btnLike.setText(

                (post.likedByMe ? "❤️ " : "🤍 ")

                        + post.likeCount
        );
    }

    // =====================================
    // SAVE UI
    // =====================================

    private void updateSaveUI() {

        btnSave.setText(

                post.savedByMe

                        ? "🔖"

                        : "📑"
        );
    }
}