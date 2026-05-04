package com.example.appcodetest.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {

    // tên file SharedPreferences
    private static final String PREF_NAME = "APP_PREF";

    // =========================
    // TOKEN LOGIN
    // =========================

    // lưu token sau khi login thành công
    public static void saveToken(Context c, String token) {

        SharedPreferences.Editor e =
                c.getSharedPreferences(
                        PREF_NAME,
                        Context.MODE_PRIVATE
                ).edit();

        e.putString("TOKEN", token);
        e.apply();
    }

    // lấy token đã lưu
    public static String getToken(Context c) {

        return c.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
        ).getString("TOKEN", null);
    }

    // xóa token khi logout
    public static void clearToken(Context c) {

        SharedPreferences.Editor e =
                c.getSharedPreferences(
                        PREF_NAME,
                        Context.MODE_PRIVATE
                ).edit();

        e.remove("TOKEN");
        e.apply();
    }

    // =========================
    // GENERIC SAVE
    // dùng để lưu dữ liệu khác
    // ví dụ:
    // CURRENT_LANG
    // USER_NAME
    // AVATAR
    // =========================

    public static void save(
            Context c,
            String key,
            String value
    ) {

        SharedPreferences.Editor e =
                c.getSharedPreferences(
                        PREF_NAME,
                        Context.MODE_PRIVATE
                ).edit();

        e.putString(key, value);
        e.apply();
    }

    // lấy dữ liệu theo key
    public static String get(
            Context c,
            String key
    ) {

        return c.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
        ).getString(key, null);
    }

    // =========================
    // CLEAR ALL
    // xóa toàn bộ dữ liệu local
    // =========================

    public static void clear(Context c) {

        SharedPreferences.Editor e =
                c.getSharedPreferences(
                        PREF_NAME,
                        Context.MODE_PRIVATE
                ).edit();

        e.clear();
        e.apply();
    }
}