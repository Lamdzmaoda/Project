package com.example.appcodetest.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcodetest.R;
import com.example.appcodetest.api.ApiService;
import com.example.appcodetest.api.RetrofitClient;
import com.example.appcodetest.model.ApiResponse;
import com.example.appcodetest.model.UserResponse;
import com.example.appcodetest.utils.Prefs;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserManagerActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    UserAdapter adapter;
    List<UserResponse> userList = new ArrayList<>();

    ApiService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manager);

        recyclerView = findViewById(R.id.recyclerUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new UserAdapter(this, userList);
        recyclerView.setAdapter(adapter);

        api = RetrofitClient.getClient().create(ApiService.class);

        loadUsers();
    }

    private void loadUsers() {

        String token = "Bearer " + Prefs.getToken(this);

        Log.d("TOKEN", token); // 🔥 debug token

        api.getUsers(token).enqueue(new Callback<ApiResponse<List<UserResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<UserResponse>>> call,
                                   Response<ApiResponse<List<UserResponse>>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().result != null) {

                        userList.clear();
                        userList.addAll(response.body().result);
                        adapter.notifyDataSetChanged();

                        Log.d("USER", "Loaded: " + userList.size());
                    }

                } else {
                    Toast.makeText(UserManagerActivity.this,
                            "Load user thất bại", Toast.LENGTH_SHORT).show();

                    Log.e("USER", "Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<UserResponse>>> call, Throwable t) {

                Toast.makeText(UserManagerActivity.this,
                        "Lỗi kết nối", Toast.LENGTH_SHORT).show();

                Log.e("USER", t.toString());
            }
        });
    }
}