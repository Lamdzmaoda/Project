package com.example.democode3.features.home.ui.component;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.democode3.R;
import com.example.democode3.features.home.model.Chapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class ChapterPickerSheet
        extends BottomSheetDialogFragment {

    // =====================================================
    // DATA
    // =====================================================

    private final List<Chapter> chapters;

    private final OnChapterSelected listener;

    // =====================================================
    // CALLBACK
    // =====================================================

    public interface OnChapterSelected {

        void onSelected(Chapter chapter);
    }

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public ChapterPickerSheet(
            List<Chapter> chapters,
            OnChapterSelected listener
    ) {

        this.chapters = chapters;

        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable android.view.ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        View view =
                inflater.inflate(
                        R.layout.sheet_chapter_picker,
                        container,
                        false
                );

        LinearLayout layoutContainer =
                view.findViewById(
                        R.id.layoutChapterContainer
                );

        // =============================================
        // RENDER
        // =============================================

        for (Chapter chapter : chapters) {

            TextView txtChapter =
                    new TextView(requireContext());

            txtChapter.setText(
                    chapter.title
            );

            txtChapter.setTextSize(22);

            txtChapter.setTextColor(
                    0xFFFFFFFF
            );

            txtChapter.setPadding(
                    40,
                    40,
                    40,
                    40
            );

            txtChapter.setOnClickListener(v -> {

                listener.onSelected(chapter);

                dismiss();
            });

            layoutContainer.addView(
                    txtChapter
            );
        }

        return view;
    }
}