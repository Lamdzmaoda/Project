package com.example.democode3.core.network;

import android.content.Context;
import android.util.Log;

import com.example.democode3.core.session.TokenManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor
        implements Interceptor {

    // =====================================
    // CONTEXT
    // =====================================

    private final Context context;

    // =====================================
    // CONSTRUCTOR
    // =====================================

    public AuthInterceptor(
            Context context
    ) {

        this.context = context;
    }

    @Override
    public Response intercept(
            Chain chain
    ) throws IOException {

        Request request =
                chain.request();

        // =================================
        // PATH
        // =================================

        String path =
                request.url().encodedPath();

        Log.d(
                "API_PATH",
                path
        );

        // =================================
        // SKIP TOKEN
        // =================================

        boolean skipToken =

                path.contains("/auth/token")
                        ||
                        path.contains("/users/register")
                        ||
                        path.contains("/auth/refresh");

        // =================================
        // ADD TOKEN
        // =================================

        if (!skipToken) {

            String token =
                    TokenManager.getToken(
                            context
                    );

            Log.d(
                    "TOKEN",
                    String.valueOf(token)
            );

            if (token != null) {

                request =

                        request.newBuilder()

                                .addHeader(

                                        "Authorization",

                                        "Bearer " + token
                                )

                                .build();
            }
        }

        // =================================
        // RESPONSE
        // =================================

        Response response =
                chain.proceed(request);

        // =================================
        // LOG RESPONSE
        // =================================

        Log.d(
                "API_CODE",
                String.valueOf(response.code())
        );

        return response;
    }
}