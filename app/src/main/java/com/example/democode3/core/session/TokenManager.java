package com.example.democode3.core.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import org.json.JSONObject;

public class TokenManager {

    // =====================================
    // PREF
    // =====================================

    private static final String PREF_NAME =
            "auth_pref";

    private static final String KEY_TOKEN =
            "jwt_token";

    // =====================================
    // SAVE TOKEN
    // =====================================

    public static void saveToken(

            Context context,

            String token
    ) {

        SharedPreferences pref =

                context.getSharedPreferences(

                        PREF_NAME,

                        Context.MODE_PRIVATE
                );

        pref.edit()

                .putString(
                        KEY_TOKEN,
                        token
                )

                .apply();
    }

    // =====================================
    // GET TOKEN
    // =====================================

    public static String getToken(
            Context context
    ) {

        SharedPreferences pref =

                context.getSharedPreferences(

                        PREF_NAME,

                        Context.MODE_PRIVATE
                );

        return pref.getString(
                KEY_TOKEN,
                null
        );
    }

    // =====================================
    // IS ADMIN
    // =====================================

    public static boolean isAdmin(
            Context context
    ) {

        try {

            String token =
                    getToken(context);

            if (token == null)
                return false;

            String[] split =
                    token.split("\\.");

            String payload =
                    split[1];

            byte[] decodedBytes =

                    Base64.decode(

                            payload,

                            Base64.URL_SAFE
                    );

            String decodedPayload =
                    new String(decodedBytes);

            JSONObject json =
                    new JSONObject(
                            decodedPayload
                    );

            String scope =
                    json.getString("scope");

            return scope.contains(
                    "ROLE_ADMIN"
            );
        }

        catch (Exception e) {

            return false;
        }
    }

    // =====================================
    // CLEAR TOKEN
    // =====================================

    public static void clear(
            Context context
    ) {

        SharedPreferences pref =

                context.getSharedPreferences(

                        PREF_NAME,

                        Context.MODE_PRIVATE
                );

        pref.edit()
                .clear()
                .apply();
    }
}