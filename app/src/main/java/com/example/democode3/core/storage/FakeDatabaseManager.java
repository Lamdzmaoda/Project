// core/preferences/FakeDatabaseManager.java

package com.example.democode3.core.storage;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FakeDatabaseManager {

    private static final String PREF_NAME =
            "fake_database";

    private static final String KEY_USERS =
            "users";

    // =====================================
    // USER ACCOUNT
    // =====================================

    public static class UserAccount {

        public long id;

        public String email;

        public String password;

        public String username;

        public String displayName;

        public String role;
    }

    // =====================================
    // PREFS
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
    // SAVE USERS
    // =====================================

    public static void saveUsers(
            Context context,
            List<UserAccount> users
    ) {

        JSONArray jsonArray =
                new JSONArray();

        try {

            for (UserAccount user : users) {

                JSONObject obj =
                        new JSONObject();

                obj.put("id", user.id);

                obj.put("email", user.email);

                obj.put("password", user.password);

                obj.put("username", user.username);

                obj.put(
                        "displayName",
                        user.displayName
                );

                obj.put("role", user.role);

                jsonArray.put(obj);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        getPrefs(context)
                .edit()
                .putString(
                        KEY_USERS,
                        jsonArray.toString()
                )
                .apply();
    }

    // =====================================
    // GET USERS
    // =====================================

    public static List<UserAccount> getUsers(
            Context context
    ) {

        List<UserAccount> users =
                new ArrayList<>();

        String json =
                getPrefs(context)
                        .getString(
                                KEY_USERS,
                                null
                        );

        // =================================
        // FIRST INSTALL
        // =================================

        if (json == null) {

            UserAccount admin =
                    new UserAccount();

            admin.id = 1;

            admin.email = "dante";

            admin.password = "dante";

            admin.username =
                    "dante_atreides";

            admin.displayName =
                    "Dante";

            admin.role = "ADMIN";

            users.add(admin);

            saveUsers(context, users);

            return users;
        }

        try {

            JSONArray jsonArray =
                    new JSONArray(json);

            for (
                    int i = 0;
                    i < jsonArray.length();
                    i++
            ) {

                JSONObject obj =
                        jsonArray.getJSONObject(i);

                UserAccount user =
                        new UserAccount();

                user.id =
                        obj.getLong("id");

                user.email =
                        obj.getString("email");

                user.password =
                        obj.getString("password");

                user.username =
                        obj.getString("username");

                user.displayName =
                        obj.getString(
                                "displayName"
                        );

                user.role =
                        obj.getString("role");

                users.add(user);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return users;
    }
}