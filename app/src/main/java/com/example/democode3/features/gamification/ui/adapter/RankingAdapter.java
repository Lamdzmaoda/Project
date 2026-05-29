package com.example.democode3.features.gamification.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.democode3.R;
import com.example.democode3.features.fake.model.FakeUser;
import com.example.democode3.features.fake.session.FakeSessionManager;
import com.example.democode3.features.gamification.model.RankingUser;

import java.util.List;

public class RankingAdapter
        extends RecyclerView.Adapter<RankingAdapter.VH> {

    // =====================================
    // DATA
    // =====================================

    private final List<RankingUser> list;

    // =====================================
    // CLICK
    // =====================================

    private final OnUserClickListener listener;

    // =====================================
    // INTERFACE
    // =====================================

    public interface OnUserClickListener {

        void onUserClick(
                String userId
        );
    }

    // =====================================
    // CONSTRUCTOR
    // =====================================

    public RankingAdapter(

            List<RankingUser> list,

            OnUserClickListener listener
    ) {

        this.list = list;

        this.listener = listener;
    }

    // =====================================
    // HOLDER
    // =====================================

    static class VH
            extends RecyclerView.ViewHolder {

        LinearLayout rootLayout;

        TextView txtRank;

        TextView txtAvatar;

        TextView txtUsername;

        TextView txtLevel;

        TextView txtXp;

        LinearLayout layoutUser;

        public VH(View v) {

            super(v);

            rootLayout =
                    v.findViewById(
                            R.id.rootLayout
                    );

            txtRank =
                    v.findViewById(
                            R.id.txtRank
                    );

            txtAvatar =
                    v.findViewById(
                            R.id.txtAvatar
                    );

            txtUsername =
                    v.findViewById(
                            R.id.txtUsername
                    );

            txtLevel =
                    v.findViewById(
                            R.id.txtLevel
                    );

            txtXp =
                    v.findViewById(
                            R.id.txtXp
                    );

            layoutUser =
                    v.findViewById(
                            R.id.layoutUser
                    );
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
                LayoutInflater.from(
                        parent.getContext()
                ).inflate(

                        R.layout.item_ranking_user,

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

        RankingUser user =
                list.get(position);

        // =================================
        // RANK
        // =================================

        h.txtRank.setText(
                "#" + user.rank
        );

        // =================================
        // AVATAR
        // =================================

        if (

                user.username != null

                        &&

                        !user.username.isEmpty()
        ) {

            h.txtAvatar.setText(

                    user.username
                            .substring(0, 1)
                            .toUpperCase()
            );
        }

        // =================================
        // USERNAME
        // =================================

        h.txtUsername.setText(
                user.username
        );

        // =================================
        // LEVEL
        // =================================

        h.txtLevel.setText(
                "Lv." + user.level
        );

        // =================================
        // XP
        // =================================

        h.txtXp.setText(

                user.xp + " XP"
        );

        // =================================
        // CURRENT USER
        // =================================

        FakeUser currentUser =

                FakeSessionManager
                        .getInstance()
                        .getCurrentUser();

        boolean isCurrentUser =

                currentUser != null

                        &&

                        currentUser.id.equals(
                                user.id
                        );

        // =================================
        // BACKGROUND
        // =================================

        if (isCurrentUser) {

            h.itemView.setBackgroundResource(
                    R.drawable.bg_current_user_rank
            );

        } else {

            h.itemView.setBackgroundResource(
                    R.drawable.bg_card_secondary
            );
        }

        // =================================
        // TOP 3 COLOR
        // =================================

        if (user.rank == 1) {

            h.txtRank.setTextColor(
                    Color.parseColor("#FFD700")
            );

        }

        else if (user.rank == 2) {

            h.txtRank.setTextColor(
                    Color.parseColor("#C0C0C0")
            );
        }

        else if (user.rank == 3) {

            h.txtRank.setTextColor(
                    Color.parseColor("#CD7F32")
            );
        }

        else {

            h.txtRank.setTextColor(
                    Color.WHITE
            );
        }

        // =================================
        // CLICK
        // =================================

        View.OnClickListener click =
                v -> {

                    if (

                            listener != null

                                    &&

                                    user.id != null
                    ) {

                        listener.onUserClick(
                                user.id
                        );
                    }
                };

        h.itemView.setOnClickListener(
                click
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