package com.example.democode3.core.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.democode3.core.enums.RoleType;
import com.example.democode3.core.enums.UserStatus;
import com.example.democode3.core.model.User;

public class SessionManager {

    private static final String PREF_NAME =
            "user_session";

    // =====================================
    // KEY
    // =====================================

    private static final String KEY_ID =
            "id";

    private static final String KEY_EMAIL =
            "email";

    private static final String KEY_USERNAME =
            "username";

    private static final String KEY_DISPLAY_NAME =
            "display_name";

    private static final String KEY_ROLE =
            "role";

    private static final String KEY_LEVEL =
            "level";

    private static final String KEY_XP =
            "xp";

    private static final String KEY_COIN =
            "coin";

    private static final String KEY_STREAK =
            "streak";

    private static final String KEY_VERIFIED =
            "verified";

    // =====================================
    // PREF
    // =====================================

    private static SharedPreferences getPrefs(
            Context context
    ) {

        return context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
        );
    }

    // =====================================
    // SAVE USER
    // =====================================

    public static void saveUser(
            Context context,
            User user
    ) {

        getPrefs(context)
                .edit()

                .putString(
                        KEY_ID,
                        String.valueOf(user.id)
                )

                .putString(
                        KEY_EMAIL,
                        user.email
                )

                .putString(
                        KEY_USERNAME,
                        user.username
                )

                .putString(
                        KEY_DISPLAY_NAME,
                        user.displayName
                )

                .putString(
                        KEY_ROLE,

                        user.role != null

                                ?

                                user.role.name()

                                :

                                "USER"
                )

                .putInt(
                        KEY_LEVEL,
                        user.level
                )

                .putInt(
                        KEY_XP,
                        user.xp
                )

                .putInt(
                        KEY_COIN,
                        user.coin
                )

                .putInt(
                        KEY_STREAK,
                        user.streak
                )

                .putBoolean(
                        KEY_VERIFIED,
                        user.verified
                )

                .apply();
    }

    // =====================================
    // GET USER
    // =====================================

    public static User getUser(
            Context context
    ) {

        SharedPreferences prefs =
                getPrefs(context);

        User user =
                new User();

        // =================================
        // SAFE ID
        // =================================

        Object rawId =
                prefs.getAll().get(KEY_ID);

        if (rawId == null) {

            user.id = "";
        }

        else {

            user.id =
                    String.valueOf(rawId);
        }

        // =================================
        // STRING
        // =================================

        user.email =
                prefs.getString(
                        KEY_EMAIL,
                        ""
                );

        user.username =
                prefs.getString(
                        KEY_USERNAME,
                        ""
                );

        user.displayName =
                prefs.getString(
                        KEY_DISPLAY_NAME,
                        ""
                );

        // =================================
        // ROLE
        // =================================

        String role =
                prefs.getString(
                        KEY_ROLE,
                        "USER"
                );

        try {

            user.role =
                    RoleType.valueOf(role);

        } catch (Exception e) {

            user.role =
                    RoleType.USER;
        }

        // =================================
        // LEVEL
        // =================================

        user.level =
                prefs.getInt(
                        KEY_LEVEL,
                        1
                );

        user.xp =
                prefs.getInt(
                        KEY_XP,
                        0
                );

        user.coin =
                prefs.getInt(
                        KEY_COIN,
                        0
                );

        user.streak =
                prefs.getInt(
                        KEY_STREAK,
                        0
                );

        user.verified =
                prefs.getBoolean(
                        KEY_VERIFIED,
                        false
                );

        user.status =
                UserStatus.ACTIVE;

        return user;
    }

    // =====================================
    // LOGIN
    // =====================================

    public static boolean isLoggedIn(
            Context context
    ) {

        return TokenManager.getToken(
                context
        ) != null;
    }

    // =====================================
    // CLEAR
    // =====================================

    public static void clear(
            Context context
    ) {

        getPrefs(context)
                .edit()
                .clear()
                .apply();

        TokenManager.clear(context);
    }
}