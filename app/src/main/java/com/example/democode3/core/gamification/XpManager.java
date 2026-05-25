package com.example.democode3.core.gamification;

import android.content.Context;
import android.content.SharedPreferences;

public class XpManager {

    // =====================================
    // PREF
    // =====================================

    private static final String PREF =
            "xp_pref";

    private static final String KEY_XP =
            "user_xp";

    private static final String KEY_LEVEL =
            "user_level";

    private static final String KEY_STREAK =
            "user_streak";

    // =====================================
    // XP ACTION
    // =====================================

    public static final int XP_LESSON = 25;

    public static final int XP_POST = 10;

    public static final int XP_COMMENT = 5;

    public static final int XP_DAILY = 50;

    // =====================================
    // GET PREF
    // =====================================

    private static SharedPreferences getPref(
            Context context
    ) {

        return context.getSharedPreferences(

                PREF,

                Context.MODE_PRIVATE
        );
    }

    // =====================================
    // ADD XP
    // =====================================

    public static void addXp(

            Context context,

            int xp
    ) {

        int currentXp =
                getXp(context);

        currentXp += xp;

        int level =
                calculateLevel(currentXp);

        getPref(context)

                .edit()

                .putInt(
                        KEY_XP,
                        currentXp
                )

                .putInt(
                        KEY_LEVEL,
                        level
                )

                .apply();
    }

    // =====================================
    // GET XP
    // =====================================

    public static int getXp(
            Context context
    ) {

        return getPref(context)

                .getInt(
                        KEY_XP,
                        0
                );
    }

    // =====================================
    // GET LEVEL
    // =====================================

    public static int getLevel(
            Context context
    ) {

        return getPref(context)

                .getInt(
                        KEY_LEVEL,
                        1
                );
    }

    // =====================================
    // CALCULATE LEVEL
    // =====================================

    public static int calculateLevel(
            int xp
    ) {

        return (xp / 100) + 1;
    }

    // =====================================
    // XP CURRENT LEVEL
    // =====================================

    public static int getCurrentLevelXp(
            Context context
    ) {

        int xp =
                getXp(context);

        return xp % 100;
    }

    // =====================================
    // NEXT LEVEL XP
    // =====================================

    public static int getNextLevelXp() {

        return 100;
    }

    // =====================================
    // STREAK
    // =====================================

    public static int getStreak(
            Context context
    ) {

        return getPref(context)

                .getInt(
                        KEY_STREAK,
                        0
                );
    }

    // =====================================
    // ADD STREAK
    // =====================================

    public static void addStreak(
            Context context
    ) {

        int streak =
                getStreak(context);

        streak++;

        getPref(context)

                .edit()

                .putInt(
                        KEY_STREAK,
                        streak
                )

                .apply();
    }

    // =====================================
    // RESET STREAK
    // =====================================

    public static void resetStreak(
            Context context
    ) {

        getPref(context)

                .edit()

                .putInt(
                        KEY_STREAK,
                        0
                )

                .apply();
    }

    // =====================================
    // CLEAR
    // =====================================

    public static void clear(
            Context context
    ) {

        getPref(context)

                .edit()

                .clear()

                .apply();
    }
}