package com.example.appcodetest.ui;

import android.content.Context;
import android.content.Intent;
import android.view.*;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcodetest.R;
import com.example.appcodetest.model.Lesson;

import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.VH> {

    // Danh sách lesson
    List<Lesson> list;

    // Context để mở Activity mới
    Context context;

    public LessonAdapter(Context context, List<Lesson> list) {
        this.context = context;
        this.list = list;
    }

    // ViewHolder giữ view của từng item lesson
    static class VH extends RecyclerView.ViewHolder {

        TextView txtTitle, txtXP, txtStatus;

        public VH(View v) {
            super(v);

            // Ánh xạ view từ item_lesson.xml
            txtTitle = v.findViewById(R.id.txtTitle);
            txtXP = v.findViewById(R.id.txtXP);
            txtStatus = v.findViewById(R.id.txtStatus);
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Inflate layout item lesson
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lesson, parent, false);

        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int i) {

        Lesson lesson = list.get(i);

        // Hiển thị số thứ tự + tên lesson
        h.txtTitle.setText((i + 1) + ". " + lesson.title);

        // Hiển thị XP nhận được
        h.txtXP.setText("XP +" + (int) lesson.xp);

        // 🔥 Hiển thị trạng thái lesson

        // Đã hoàn thành
        if ("TRUE".equals(lesson.completedStatus)) {
            h.txtStatus.setText("✔");
        }

        // Bị khóa
        else if ("TRUE_LOCKED".equals(lesson.lockedStatus)) {
            h.txtStatus.setText("🔒");
        }

        // Bình thường
        else {
            h.txtStatus.setText("");
        }

        // Click lesson → vào màn hình học
        h.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, LessonLearnActivity.class);

            // truyền lesson id sang màn hình học
            intent.putExtra("LESSON_ID", lesson.id);

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}