package com.example.democode3.features.community.ui.component;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.democode3.R;

import com.example.democode3.features.community.api.request.CreateCommentRequest;
import com.example.democode3.features.community.api.response.CommentResponse;

import com.example.democode3.features.community.model.Comment;

import com.example.democode3.features.community.repository.CommunityRepository;

import com.example.democode3.features.community.ui.adapter.CommentAdapter;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentBottomSheet
        extends BottomSheetDialogFragment {

    // =====================================
    // ARG
    // =====================================

    private String postId;

    // =====================================
    // VIEW
    // =====================================

    RecyclerView recyclerComment;

    EditText edtComment;

    TextView btnSend;

    // =====================================
    // DATA
    // =====================================

    ArrayList<Comment> comments =
            new ArrayList<>();

    CommentAdapter adapter;

    // =====================================
    // REPOSITORY
    // =====================================

    private CommunityRepository repository;

    // =====================================
    // CREATE
    // =====================================

    public static CommentBottomSheet newInstance(
            String postId
    ) {

        CommentBottomSheet sheet =
                new CommentBottomSheet();

        Bundle args = new Bundle();

        args.putString(
                "postId",
                postId
        );

        sheet.setArguments(args);

        return sheet;
    }

    // =====================================
    // VIEW
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

                        R.layout.sheet_comment,

                        container,

                        false
                );

        // =================================
        // ARG
        // =================================

        if (getArguments() != null) {

            postId =
                    getArguments()
                            .getString("postId");
        }

        // =================================
        // REPOSITORY
        // =================================

        repository =
                new CommunityRepository(
                        requireContext()
                );

        // =================================
        // VIEW
        // =================================

        recyclerComment =
                view.findViewById(
                        R.id.recyclerComment
                );

        edtComment =
                view.findViewById(
                        R.id.edtComment
                );

        btnSend =
                view.findViewById(
                        R.id.btnSend
                );

        // =================================
        // RECYCLER
        // =================================

        recyclerComment.setLayoutManager(
                new LinearLayoutManager(
                        requireContext()
                )
        );

        adapter =
                new CommentAdapter(
                        requireContext(),
                        comments,
                        null
                );

        recyclerComment.setAdapter(
                adapter
        );

        // =================================
        // LOAD COMMENTS
        // =================================

        loadComments();

        // =================================
        // SEND COMMENT
        // =================================

        btnSend.setOnClickListener(v -> {

            String text =
                    edtComment.getText()
                            .toString()
                            .trim();

            if (text.isEmpty()) {

                return;
            }

            CreateCommentRequest request =
                    new CreateCommentRequest();

            request.postId =
                    postId;

            request.content =
                    text;

            repository.createComment(

                    request,

                    new Callback<Void>() {

                        @Override
                        public void onResponse(

                                Call<Void> call,

                                Response<Void> response
                        ) {

                            edtComment.setText("");

                            loadComments();
                        }

                        @Override
                        public void onFailure(

                                Call<Void> call,

                                Throwable t
                        ) {

                            Toast.makeText(

                                    requireContext(),

                                    t.getMessage(),

                                    Toast.LENGTH_LONG

                            ).show();
                        }
                    }
            );
        });

        return view;
    }

    // =====================================
    // LOAD COMMENTS
    // =====================================

    private void loadComments() {

        repository.getComments(

                postId,

                new Callback<CommentResponse>() {

                    @Override
                    public void onResponse(

                            Call<CommentResponse> call,

                            Response<CommentResponse> response
                    ) {

                        if (
                                response.body() != null
                                        &&
                                        response.body().result != null
                        ) {

                            comments.clear();

                            comments.addAll(
                                    response.body().result
                            );

                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(

                            Call<CommentResponse> call,

                            Throwable t
                    ) {

                        Toast.makeText(

                                requireContext(),

                                t.getMessage(),

                                Toast.LENGTH_LONG

                        ).show();
                    }
                }
        );
    }

    // =====================================
    // FULL HEIGHT
    // =====================================

    @Override
    public void onStart() {

        super.onStart();

        Dialog dialog = getDialog();

        if (dialog instanceof BottomSheetDialog) {

            BottomSheetDialog bottomSheetDialog =
                    (BottomSheetDialog) dialog;

            View bottomSheet =
                    bottomSheetDialog.findViewById(

                            com.google.android.material
                                    .R.id.design_bottom_sheet
                    );

            if (bottomSheet != null) {

                bottomSheet.getLayoutParams().height =
                        WindowManager.LayoutParams.MATCH_PARENT;

                BottomSheetBehavior<View> behavior =
                        BottomSheetBehavior.from(
                                bottomSheet
                        );

                behavior.setState(
                        BottomSheetBehavior.STATE_EXPANDED
                );

                behavior.setSkipCollapsed(true);
            }
        }
    }
}