package com.example.democode3.core.network;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // =====================================
    // BASE URL
    // =====================================

    public static final String BASE_URL =
            "https://myproject-ekgm.onrender.com/identity/";

    // =====================================
    // RETROFIT
    // =====================================

    private static Retrofit retrofit;

    // =====================================
    // GET INSTANCE
    // =====================================

    public static Retrofit getInstance(
            Context context
    ) {

        if (retrofit == null) {

            // =================================
            // LOGGING
            // =================================

            HttpLoggingInterceptor logging =
                    new HttpLoggingInterceptor();

            logging.setLevel(
                    HttpLoggingInterceptor.Level.BODY
            );

            // =================================
            // OKHTTP
            // =================================

            OkHttpClient client =
                    new OkHttpClient.Builder()

                            .addInterceptor(
                                    new AuthInterceptor(
                                            context
                                    )
                            )

                            .addInterceptor(logging)

                            .connectTimeout(
                                    60,
                                    TimeUnit.SECONDS
                            )

                            .readTimeout(
                                    60,
                                    TimeUnit.SECONDS
                            )

                            .writeTimeout(
                                    60,
                                    TimeUnit.SECONDS
                            )

                            .build();

            // =================================
            // RETROFIT
            // =================================

            retrofit =
                    new Retrofit.Builder()

                            .baseUrl(BASE_URL)

                            .client(client)

                            .addConverterFactory(
                                    GsonConverterFactory.create()
                            )

                            .build();
        }

        return retrofit;
    }
}