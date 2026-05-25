package com.example.democode3.features.profile.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.democode3.R;

import com.example.democode3.features.profile.model.ProfileResult;

import com.example.democode3.features.profile.ui.fragment.ProfileFragment;

import java.util.List;

public class FollowUserAdapter
        extends RecyclerView.Adapter<
        FollowUserAdapter.VH> {

    private final List<ProfileResult>
            list;

    public FollowUserAdapter(
            List<ProfileResult> list
    ) {

        this.list = list;
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

                        R.layout.item_follow_user,

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

        ProfileResult user =
                list.get(position);

        // =========================
        // AVATAR
        // =========================

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

        // =========================
        // USERNAME
        // =========================

        h.txtUsername.setText(
                "@" + user.username
        );

        // =========================
        // NAME
        // =========================

        h.txtName.setText(
                user.displayName
        );

        // =========================
        // CLICK
        // =========================

        h.itemView.setOnClickListener(v -> {

            androidx.fragment.app.FragmentActivity activity =
                    (androidx.fragment.app.FragmentActivity)
                            v.getContext();

            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(

                            android.R.id.content,

                            ProfileFragment.newInstance(
                                    String.valueOf(user.id)
                            )
                    )
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    static class VH
            extends RecyclerView.ViewHolder {

        TextView txtAvatar;
        TextView txtUsername;
        TextView txtName;

        public VH(
                @NonNull View itemView
        ) {

            super(itemView);

            txtAvatar =
                    itemView.findViewById(
                            R.id.txtAvatar
                    );

            txtUsername =
                    itemView.findViewById(
                            R.id.txtUsername
                    );

            txtName =
                    itemView.findViewById(
                            R.id.txtName
                    );
        }
    }
}