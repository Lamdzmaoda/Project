package com.example.democode3.features.learning.ui.component;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.democode3.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class QuizResultSheet
        extends BottomSheetDialogFragment {

    // =====================================================
    // DATA
    // =====================================================

    private final boolean success;

    private final String message;

    private final Runnable onAction;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public QuizResultSheet(

            boolean success,

            String message,

            Runnable onAction
    ) {

        this.success =
                success;

        this.message =
                message;

        this.onAction =
                onAction;
    }

    // =====================================================
    // CREATE VIEW
    // =====================================================

    @Nullable
    @Override
    public View onCreateView(

            @NonNull LayoutInflater inflater,

            @Nullable ViewGroup container,

            @Nullable Bundle savedInstanceState
    ) {

        return inflater.inflate(

                R.layout.sheet_quiz_result,

                container,

                false
        );
    }

    // =====================================================
    // VIEW CREATED
    // =====================================================

    @Override
    public void onViewCreated(

            @NonNull View view,

            @Nullable Bundle savedInstanceState
    ) {

        super.onViewCreated(
                view,
                savedInstanceState
        );

        TextView txtTitle =
                view.findViewById(
                        R.id.txtTitle
                );

        TextView txtMessage =
                view.findViewById(
                        R.id.txtMessage
                );

        Button btnContinue =
                view.findViewById(
                        R.id.btnContinue
                );

        // =============================================
        // SUCCESS
        // =============================================

        if (success) {

            txtTitle.setText(
                    "Chính xác 🎉"
            );

            btnContinue.setText(
                    "TIẾP TỤC"
            );
        }

        // =============================================
        // WRONG
        // =============================================

        else {

            txtTitle.setText(
                    "Sai rồi 😢"
            );

            btnContinue.setText(
                    "THỬ LẠI"
            );
        }

        // =============================================
        // MESSAGE
        // =============================================

        txtMessage.setText(
                message
        );

        // =============================================
        // BUTTON CLICK
        // =============================================

        btnContinue.setOnClickListener(v -> {

            dismiss();

            if (onAction != null) {

                onAction.run();
            }
        });
    }
}