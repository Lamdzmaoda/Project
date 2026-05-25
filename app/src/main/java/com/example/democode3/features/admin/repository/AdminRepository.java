package com.example.democode3.features.admin.repository;

import android.content.Context;

import com.example.democode3.core.network.RetrofitClient;
import com.example.democode3.features.admin.api.AdminApiService;
import com.example.democode3.features.admin.model.UserItem;
import com.example.democode3.features.admin.model.UserListResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class AdminRepository {

    private final AdminApiService api;

    public AdminRepository(Context context) {

        api = RetrofitClient

                .getInstance(context)

                .create(AdminApiService.class);
    }

    // GET USERS

    public void getUsers(
            Callback<UserListResponse> callback
    ) {

        api.getUsers()
                .enqueue(callback);
    }

    // DELETE USER

    public void deleteUser(

            String id,

            Callback<String> callback
    ) {

        api.deleteUser(id)
                .enqueue(callback);
    }

    // UPDATE USER

    public void updateUser(

            String id,

            Map<String, Object> body,

            Callback<UserItem> callback
    ) {

        api.updateUser(id, body)
                .enqueue(callback);
    }
}