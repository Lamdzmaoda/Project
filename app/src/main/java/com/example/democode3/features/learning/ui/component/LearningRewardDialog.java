package com.example.democode3.features.learning.ui.component;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.democode3.R;

public class LearningRewardDialog
        extends DialogFragment {

    private final int xp;

    private final int coin;

    public LearningRewardDialog(
            int xp,
            int coin
    ) {

        this.xp = xp;

        this.coin = coin;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(
            @Nullable Bundle savedInstanceState
    ) {

        Dialog dialog =
                super.onCreateDialog(savedInstanceState);

        dialog.requestWindowFeature(
                Window.FEATURE_NO_TITLE
        );

        return dialog;
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
                        R.layout.dialog_learning_reward,
                        container,
                        false
                );

        TextView txtXp =
                view.findViewById(
                        R.id.txtRewardXp
                );

        TextView txtCoin =
                view.findViewById(
                        R.id.txtRewardCoin
                );

        Button btnContinue =
                view.findViewById(
                        R.id.btnContinueLearning
                );

        txtXp.setText(
                "+" + xp + " XP"
        );

        txtCoin.setText(
                "+" + coin + " Coin"
        );

        btnContinue.setOnClickListener(v -> {

            dismiss();

            requireActivity().finish();
        });

        return view;
    }

    @Override
    public void onStart() {

        super.onStart();

        Dialog dialog =
                getDialog();

        if (dialog != null
                && dialog.getWindow() != null) {

            dialog.getWindow().setLayout(

                    WindowManager.LayoutParams.MATCH_PARENT,

                    WindowManager.LayoutParams.WRAP_CONTENT
            );

            dialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent
            );
        }
    }
}