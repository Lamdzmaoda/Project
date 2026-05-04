package com.example.appcodetest.utils;

public class Constants {

    // 🔥 Địa chỉ API backend chính của project
    // Android sẽ gọi toàn bộ API thông qua BASE_URL này
    // Ví dụ:
    // login  -> auth/token
    // users  -> users/myInfo
    // course -> course/languages
    public static final String BASE_URL =
            "https://myproject-ekgm.onrender.com/identity/";


    // 🔥 KEY dùng để lưu TOKEN trong SharedPreferences
    // Sau khi login thành công:
    // token sẽ được lưu với key = "TOKEN"
    //
    // Ví dụ:
    // Prefs.saveToken(context, token);
    //
    // Sau đó có thể lấy lại bằng:
    // Prefs.getToken(context);
    public static final String KEY_TOKEN =
            "TOKEN";
}