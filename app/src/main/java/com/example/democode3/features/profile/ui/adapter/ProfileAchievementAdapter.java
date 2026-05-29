package com.example.democode3.features.profile.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.democode3.R;
import com.example.democode3.features.fake.model.FakeAchievement;

import java.util.List;

public class ProfileAchievementAdapter
        extends RecyclerView.Adapter<
        ProfileAchievementAdapter.VH> {

    private final List<FakeAchievement> list;

    public ProfileAchievementAdapter(
            List<FakeAchievement> list
    ) {

        this.list = list;
    }

    static class VH
            extends RecyclerView.ViewHolder {

        TextView txtIcon;

        TextView txtTitle;

        TextView txtDescription;

        TextView txtProgress;

        TextView txtStatus;

        ProgressBar progressAchievement;

        VH(View v) {

            super(v);

            txtIcon =
                    v.findViewById(
                            R.id.txtIcon
                    );

            txtTitle =
                    v.findViewById(
                            R.id.txtTitle
                    );

            txtDescription =
                    v.findViewById(
                            R.id.txtDescription
                    );

            txtProgress =
                    v.findViewById(
                            R.id.txtProgress
                    );

            txtStatus =
                    v.findViewById(
                            R.id.txtStatus
                    );

            progressAchievement =
                    v.findViewById(
                            R.id.progressAchievement
                    );
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(

            @NonNull ViewGroup parent,

            int viewType
    ) {

        View view =
                LayoutInflater.from(
                        parent.getContext()
                ).inflate(

                        R.layout.item_profile_achievement,

                        parent,

                        false
                );

        return new VH(view);
    }

    @Override
    public void onBindViewHolder(

            @NonNull VH h,

            int position
    ) {

        FakeAchievement a =
                list.get(position);

        h.txtIcon.setText(
                a.icon
        );

        h.txtTitle.setText(
                a.title
        );

        h.txtDescription.setText(
                a.description
        );

        h.progressAchievement.setMax(
                a.maxProgress
        );

        h.progressAchievement.setProgress(
                a.progress
        );

        h.txtProgress.setText(

                a.progress
                        + " / "
                        + a.maxProgress
        );

        h.txtStatus.setText(

                a.unlocked

                        ?

                        "Đã mở"

                        :

                        "Đang khóa"
        );
    }

    @Override
    public int getItemCount() {

        return list.size();
    }
}