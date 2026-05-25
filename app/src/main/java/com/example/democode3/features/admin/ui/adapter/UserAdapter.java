package com.example.democode3.features.admin.ui.adapter;

import android.content.Context;
import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.democode3.R;

import com.example.democode3.features.admin.model.UserItem;

import com.example.democode3.features.profile.model.UserProfileResponse;

import java.util.List;
import java.util.Random;

public class UserAdapter
        extends RecyclerView.Adapter<UserAdapter.VH> {

    // =====================================
    // LISTENER
    // =====================================

    public interface Listener {

        void onDelete(
                UserItem item,
                int position
        );

        void onEdit(
                UserItem item,
                int position
        );
    }

    // =====================================
    // DATA
    // =====================================

    private final Context context;

    private final List<UserItem> list;

    private final Listener listener;

    // =====================================
    // CONSTRUCTOR
    // =====================================

    public UserAdapter(

            Context context,

            List<UserItem> list,

            Listener listener
    ) {

        this.context = context;

        this.list = list;

        this.listener = listener;
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

                LayoutInflater.from(context)

                        .inflate(
                                R.layout.item_user,
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

        UserItem item =
                list.get(position);

        // =====================================
        // NAME
        // =====================================

        String displayName =
                item.displayName;

        if (
                displayName == null
                        ||
                        displayName.isEmpty()
        ) {

            displayName =
                    item.username;
        }

        h.txtName.setText(
                displayName
        );

        // =====================================
        // EMAIL
        // =====================================

        h.txtEmail.setText(
                item.email
        );

        // =====================================
        // DATE
        // =====================================

        h.txtDate.setText(
                "Ngày tạo: 24/05/2026"
        );

        // =====================================
        // AVATAR
        // =====================================

        String first = "U";

        if (
                displayName != null
                        &&
                        !displayName.isEmpty()
        ) {

            first =

                    displayName

                            .substring(0, 1)

                            .toUpperCase();
        }

        h.txtAvatar.setText(first);

        // =====================================
        // RANDOM COLOR
        // =====================================

        String[] colors = {

                "#3B82F6",
                "#22C55E",
                "#A855F7",
                "#F59E0B",
                "#06B6D4",
                "#EC4899"
        };

        Random random =
                new Random();

        String color =
                colors[
                        random.nextInt(
                                colors.length
                        )
                        ];

        h.txtAvatar.setBackgroundColor(
                Color.parseColor(color)
        );

        // =====================================
        // ROLE
        // =====================================

        boolean isAdmin = false;

        if (item.roles != null) {

            for (Object obj : item.roles) {

                try {

                    UserProfileResponse.RoleResponse role =
                            (UserProfileResponse.RoleResponse) obj;

                    if (
                            role != null
                                    &&
                                    role.name != null
                                    &&
                                    role.name.equalsIgnoreCase("ADMIN")
                    ) {

                        isAdmin = true;

                        break;
                    }
                }

                catch (Exception ignored) {
                }
            }
        }

        if (isAdmin) {

            h.txtRole.setText("ADMIN");

            h.txtRole.setTextColor(
                    Color.parseColor("#3B82F6")
            );
        }

        else {

            h.txtRole.setText("USER");

            h.txtRole.setTextColor(
                    Color.parseColor("#22C55E")
            );
        }

        // =====================================
        // CLICK EDIT
        // =====================================

        h.btnEdit.setOnClickListener(v -> {

            if (listener != null) {

                listener.onEdit(
                        item,
                        position
                );
            }
        });

        // =====================================
        // CLICK DELETE
        // =====================================

        h.btnDelete.setOnClickListener(v -> {

            if (listener != null) {

                listener.onDelete(
                        item,
                        position
                );
            }
        });
    }

    // =====================================
    // COUNT
    // =====================================

    @Override
    public int getItemCount() {

        return list.size();
    }

    // =====================================
    // VIEW HOLDER
    // =====================================

    static class VH
            extends RecyclerView.ViewHolder {

        TextView txtAvatar;

        TextView txtName;

        TextView txtEmail;

        TextView txtDate;

        TextView txtRole;

        TextView btnEdit;

        TextView btnDelete;

        public VH(
                @NonNull View itemView
        ) {

            super(itemView);

            txtAvatar =
                    itemView.findViewById(
                            R.id.txtAvatar
                    );

            txtName =
                    itemView.findViewById(
                            R.id.txtName
                    );

            txtEmail =
                    itemView.findViewById(
                            R.id.txtEmail
                    );

            txtDate =
                    itemView.findViewById(
                            R.id.txtDate
                    );

            txtRole =
                    itemView.findViewById(
                            R.id.txtRole
                    );

            btnEdit =
                    itemView.findViewById(
                            R.id.btnEdit
                    );

            btnDelete =
                    itemView.findViewById(
                            R.id.btnDelete
                    );
        }
    }
}