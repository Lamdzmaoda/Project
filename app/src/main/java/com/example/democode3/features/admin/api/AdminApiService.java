package com.example.democode3.features.admin.api;

import com.example.democode3.features.admin.model.UserItem;
import com.example.democode3.features.admin.model.UserListResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AdminApiService {

    // GET USERS

    @GET("users")
    Call<UserListResponse> getUsers();

    // DELETE USER

    @DELETE("users/{id}")
    Call<String> deleteUser(
            @Path("id") String id
    );

    // UPDATE USER

    @PUT("users/{id}")
    Call<UserItem> updateUser(

            @Path("id") String id,

            @Body Map<String, Object> body
    );
}