package com.example.appcodetest.api;

import com.example.appcodetest.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // 🔥 Chỉ tạo Retrofit 1 lần duy nhất
    private static Retrofit retrofit;

    public static Retrofit getClient() {

        // Nếu chưa có Retrofit thì mới khởi tạo
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()

                    // BASE_URL lấy từ Constants.java
                    .baseUrl(Constants.BASE_URL)

                    // Dùng Gson để tự động convert JSON ↔ Object
                    .addConverterFactory(GsonConverterFactory.create())

                    .build();
        }

        // Trả về Retrofit đã tạo
        return retrofit;
    }
}