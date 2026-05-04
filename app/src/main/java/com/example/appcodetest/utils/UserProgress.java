package com.example.appcodetest.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UserProgress {

    // tên file SharedPreferences lưu tiến trình học
    private static final String PREF = "USER_PROGRESS";

    // =========================
    // CỘNG XP + LEVEL UP
    // =========================

    public static void addXP(Context context, int xp) {

        SharedPreferences sp =
                context.getSharedPreferences(
                        PREF,
                        Context.MODE_PRIVATE
                );

        // XP hiện tại
        int currentXP =
                sp.getInt("XP", 0);

        // Level hiện tại
        int level =
                sp.getInt("LEVEL", 1);

        // cộng thêm XP mới
        currentXP += xp;

        // 🎯 mỗi 100 XP sẽ tăng 1 level
        while (currentXP >= 100) {

            currentXP -= 100;
            level++;
        }

        // lưu lại dữ liệu mới
        sp.edit()
                .putInt("XP", currentXP)
                .putInt("LEVEL", level)
                .apply();
    }

    // =========================
    // LẤY XP HIỆN TẠI
    // =========================

    public static int getXP(Context context) {

        return context.getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
        ).getInt("XP", 0);
    }

    // =========================
    // LẤY LEVEL HIỆN TẠI
    // =========================

    public static int getLevel(Context context) {

        return context.getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
        ).getInt("LEVEL", 1);
    }
}