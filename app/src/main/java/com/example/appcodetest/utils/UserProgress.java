package com.example.appcodetest.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UserProgress {

    private static final String PREF = "USER_PROGRESS";

    public static void addXP(Context context, int xp) {

        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);

        int currentXP = sp.getInt("XP", 0);
        int level = sp.getInt("LEVEL", 1);

        currentXP += xp;

        // 🎯 LEVEL UP mỗi 100 XP
        while (currentXP >= 100) {
            currentXP -= 100;
            level++;
        }

        sp.edit()
                .putInt("XP", currentXP)
                .putInt("LEVEL", level)
                .apply();
    }

    public static int getXP(Context context) {
        return context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .getInt("XP", 0);
    }

    public static int getLevel(Context context) {
        return context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .getInt("LEVEL", 1);
    }
}