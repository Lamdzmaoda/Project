package com.example.appcodetest.ui;

import android.view.*;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcodetest.R;

import java.util.List;

public class BaseAdapter<T> extends RecyclerView.Adapter<BaseAdapter.VH> {

    // 🔥 Interface dùng chung cho click item
    public interface Listener<T> {

        // Click vào toàn bộ item
        void onClick(T item);

        // Click nút sửa
        void onEdit(T item);

        // Click nút xóa
        void onDelete(T item);

        // Lấy title để hiển thị ra RecyclerView
        String getTitle(T item);
    }

    // Danh sách dữ liệu
    List<T> list;

    // Listener xử lý sự kiện
    Listener<T> listener;


    public BaseAdapter(List<T> list, Listener<T> listener) {
        this.list = list;
        this.listener = listener;
    }


    // ViewHolder giữ các view của item
    static class VH extends RecyclerView.ViewHolder {

        TextView txtTitle, btnEdit, btnDelete;

        public VH(View v) {
            super(v);

            // Ánh xạ view từ item_card.xml
            txtTitle = v.findViewById(R.id.txtTitle);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {

        // Inflate layout item
        View view = LayoutInflater.from(p.getContext())
                .inflate(R.layout.item_card, p, false);

        return new VH(view);
    }


    @Override
    public void onBindViewHolder(@NonNull VH h, int i) {

        // Lấy item tại vị trí hiện tại
        T item = list.get(i);

        // Hiển thị title
        h.txtTitle.setText(listener.getTitle(item));

        // Click toàn item
        h.itemView.setOnClickListener(v ->
                listener.onClick(item)
        );

        // Click sửa
        h.btnEdit.setOnClickListener(v ->
                listener.onEdit(item)
        );

        // Click xóa
        h.btnDelete.setOnClickListener(v ->
                listener.onDelete(item)
        );
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}