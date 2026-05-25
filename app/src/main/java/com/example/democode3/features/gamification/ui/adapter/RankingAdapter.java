package com.example.democode3.features.gamification.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.democode3.R;
import com.example.democode3.core.session.SessionManager;
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

        TextView txtRank;

        TextView txtAvatar;

        TextView txtUsername;

        TextView txtXp;

        LinearLayout layoutUser;

        public VH(View v) {

            super(v);

            txtRank =
                    v.findViewById(R.id.txtRank);

            txtAvatar =
                    v.findViewById(R.id.txtAvatar);

            txtUsername =
                    v.findViewById(R.id.txtUsername);

            txtXp =
                    v.findViewById(R.id.txtXp);

            layoutUser =
                    v.findViewById(R.id.layoutUser);
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
        // NAME
        // =================================

        h.txtUsername.setText(
                user.username
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

        String currentUserId =
                String.valueOf(

                        SessionManager
                                .getUser(
                                        h.itemView.getContext()
                                )
                                .id
                );

        boolean isCurrentUser =
                user.id != null
                        &&
                        user.id.equals(currentUserId);

        if (isCurrentUser) {

            h.itemView.setBackgroundColor(
                    0xFF1E3A8A
            );

        } else {

            h.itemView.setBackgroundColor(
                    0xFF111827
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

        h.txtAvatar.setOnClickListener(
                click
        );

        h.layoutUser.setOnClickListener(
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