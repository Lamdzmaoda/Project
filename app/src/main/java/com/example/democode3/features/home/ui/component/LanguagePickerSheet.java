package com.example.democode3.features.home.ui.component;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.democode3.R;
import com.example.democode3.features.home.model.Language;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class LanguagePickerSheet
        extends BottomSheetDialogFragment {

    // =========================================
    // DATA
    // =========================================

    private final List<Language> languages;

    private final OnLanguageSelected listener;

    // =========================================
    // CALLBACK
    // =========================================

    public interface OnLanguageSelected {

        void onSelected(Language language);
    }

    // =========================================
    // CONSTRUCTOR
    // =========================================

    public LanguagePickerSheet(
            List<Language> languages,
            OnLanguageSelected listener
    ) {

        this.languages = languages;

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
                        R.layout.sheet_language_picker,
                        container,
                        false
                );

        LinearLayout layoutContainer =
                view.findViewById(
                        R.id.layoutLanguageContainer
                );

        // =====================================
        // RENDER LANGUAGE
        // =====================================

        for (Language language : languages) {

            TextView txtLanguage =
                    new TextView(requireContext());

            txtLanguage.setText(
                    language.name
            );

            txtLanguage.setTextSize(22);

            txtLanguage.setTextColor(
                    0xFFFFFFFF
            );

            txtLanguage.setPadding(
                    40,
                    40,
                    40,
                    40
            );

            txtLanguage.setOnClickListener(v -> {

                listener.onSelected(language);

                dismiss();
            });

            layoutContainer.addView(
                    txtLanguage
            );
        }

        return view;
    }
}