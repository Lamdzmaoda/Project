package com.example.democode3.core.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.democode3.R;

import java.util.List;

public class BaseAdapter<T>
        extends RecyclerView.Adapter<BaseAdapter.VH> {

    public interface Listener<T> {

        void onClick(T item);

        void onEdit(T item);

        void onDelete(T item);

        String getTitle(T item);

        default String getSubTitle(T item) {
            return null;
        }

        default boolean canEdit(T item) {
            return true;
        }

        default boolean canDelete(T item) {
            return true;
        }
    }

    List<T> list;

    Listener<T> listener;

    public BaseAdapter(
            List<T> list,
            Listener<T> listener
    ) {

        this.list = list;
        this.listener = listener;
    }

    static class VH
            extends RecyclerView.ViewHolder {

        TextView txtTitle;

        TextView txtSubtitle;

        TextView btnEdit;

        TextView btnDelete;

        public VH(View v) {

            super(v);

            txtTitle =
                    v.findViewById(R.id.txtTitle);

            txtSubtitle =
                    v.findViewById(R.id.txtSubtitle);

            btnEdit =
                    v.findViewById(R.id.btnEdit);

            btnDelete =
                    v.findViewById(R.id.btnDelete);
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(
            @NonNull ViewGroup p,
            int v
    ) {

        View view =
                LayoutInflater
                        .from(p.getContext())
                        .inflate(
                                R.layout.item_common_card,
                                p,
                                false
                        );

        return new VH(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull VH h,
            int i
    ) {

        T item = list.get(i);

        h.txtTitle.setText(
                listener.getTitle(item)
        );

        if (h.txtSubtitle != null) {

            String sub =
                    listener.getSubTitle(item);

            if (
                    sub != null
                            &&
                            !sub.isEmpty()
            ) {

                h.txtSubtitle.setText(sub);

                h.txtSubtitle.setVisibility(
                        View.VISIBLE
                );

            } else {

                h.txtSubtitle.setVisibility(
                        View.GONE
                );
            }
        }

        if (h.btnEdit != null) {

            h.btnEdit.setVisibility(

                    listener.canEdit(item)
                            ? View.VISIBLE
                            : View.GONE
            );
        }

        if (h.btnDelete != null) {

            h.btnDelete.setVisibility(

                    listener.canDelete(item)
                            ? View.VISIBLE
                            : View.GONE
            );
        }

        h.itemView.setOnClickListener(v ->

                listener.onClick(item)
        );

        if (h.btnEdit != null) {

            h.btnEdit.setOnClickListener(v ->

                    listener.onEdit(item)
            );
        }

        if (h.btnDelete != null) {

            h.btnDelete.setOnClickListener(v ->

                    listener.onDelete(item)
            );
        }
    }

    @Override
    public int getItemCount() {

        return list.size();
    }
}