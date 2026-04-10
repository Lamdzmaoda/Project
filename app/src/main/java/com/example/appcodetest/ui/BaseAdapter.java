package com.example.appcodetest.ui;

import android.view.*;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcodetest.R;

import java.util.List;

public class BaseAdapter<T> extends RecyclerView.Adapter<BaseAdapter.VH> {

    public interface Listener<T> {
        void onClick(T item);
        void onEdit(T item);
        void onDelete(T item);
        String getTitle(T item);
    }

    List<T> list;
    Listener<T> listener;

    public BaseAdapter(List<T> list, Listener<T> listener) {
        this.list = list;
        this.listener = listener;
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView txtTitle, btnEdit, btnDelete;

        public VH(View v) {
            super(v);
            txtTitle = v.findViewById(R.id.txtTitle);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {
        View view = LayoutInflater.from(p.getContext())
                .inflate(R.layout.item_card, p, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int i) {

        T item = list.get(i);

        h.txtTitle.setText(listener.getTitle(item));

        h.itemView.setOnClickListener(v -> listener.onClick(item));
        h.btnEdit.setOnClickListener(v -> listener.onEdit(item));
        h.btnDelete.setOnClickListener(v -> listener.onDelete(item));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}