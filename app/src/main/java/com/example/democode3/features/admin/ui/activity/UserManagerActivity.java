package com.example.democode3.features.admin.ui.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.democode3.R;

import com.example.democode3.features.admin.model.UserItem;
import com.example.democode3.features.admin.model.UserListResponse;

import com.example.democode3.features.admin.repository.AdminRepository;

import com.example.democode3.features.admin.ui.adapter.UserAdapter;

import com.example.democode3.features.profile.model.ProfileResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserManagerActivity
        extends AppCompatActivity {

    // =====================================
    // VIEW
    // =====================================

    private RecyclerView recyclerUsers;

    private SwipeRefreshLayout swipeRefresh;

    // =====================================
    // DATA
    // =====================================

    private final List<UserItem> users =
            new ArrayList<>();

    private UserAdapter adapter;

    // =====================================
    // REPOSITORY
    // =====================================

    private AdminRepository repository;

    // =====================================
    // CREATE
    // =====================================

    @Override
    protected void onCreate(
            @Nullable Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_admin_user_manager
        );

        initView();

        initRepository();

        initRecycler();

        setupRefresh();

        loadUsers();
    }

    // =====================================
    // INIT VIEW
    // =====================================

    private void initView() {

        recyclerUsers =
                findViewById(
                        R.id.recyclerUsers
                );

        swipeRefresh =
                findViewById(
                        R.id.swipeRefresh
                );
    }

    // =====================================
    // REPOSITORY
    // =====================================

    private void initRepository() {

        repository =
                new AdminRepository(this);
    }

    // =====================================
    // RECYCLER
    // =====================================

    private void initRecycler() {

        adapter =
                new UserAdapter(

                        this,

                        users,

                        new UserAdapter.Listener() {

                            @Override
                            public void onDelete(
                                    UserItem item,
                                    int position
                            ) {

                                showDeleteDialog(
                                        item,
                                        position
                                );
                            }

                            @Override
                            public void onEdit(
                                    UserItem item,
                                    int position
                            ) {

                                Toast.makeText(

                                        UserManagerActivity.this,

                                        "Sửa user sẽ làm tiếp 😭",

                                        Toast.LENGTH_SHORT

                                ).show();
                            }
                        }
                );

        recyclerUsers.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerUsers.setAdapter(adapter);
    }

    // =====================================
    // REFRESH
    // =====================================

    private void setupRefresh() {

        swipeRefresh.setOnRefreshListener(
                this::loadUsers
        );
    }

    // =====================================
    // LOAD USERS
    // =====================================

    private void loadUsers() {

        swipeRefresh.setRefreshing(true);

        repository.getUsers(

                new Callback<UserListResponse>() {

                    @Override
                    public void onResponse(

                            Call<UserListResponse> call,

                            Response<UserListResponse> response
                    ) {

                        swipeRefresh.setRefreshing(false);

                        if (
                                response.body() != null
                                        &&
                                        response.body().result != null
                        ) {

                            users.clear();

                            for (
                                    ProfileResult profile
                                    :
                                    response.body().result
                            ) {

                                UserItem item =
                                        new UserItem();

                                // =====================================
                                // BASIC
                                // =====================================

                                item.id =
                                        String.valueOf(
                                                profile.id
                                        );

                                item.username =
                                        profile.username;

                                item.displayName =
                                        profile.displayName;

                                item.email =
                                        profile.email;

                                // =====================================
                                // GAME
                                // =====================================

                                item.xp =
                                        profile.xp;

                                item.level =
                                        profile.level;

                                // =====================================
                                // VERIFIED
                                // =====================================

                                item.verified =
                                        profile.verified;

                                // =====================================
                                // ROLE
                                // =====================================

                                item.roles =
                                        profile.roles;

                                users.add(item);
                            }

                            adapter.notifyDataSetChanged();
                        }

                        else {

                            Toast.makeText(

                                    UserManagerActivity.this,

                                    "Không tải được user",

                                    Toast.LENGTH_SHORT

                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(

                            Call<UserListResponse> call,

                            Throwable t
                    ) {

                        swipeRefresh.setRefreshing(false);

                        Toast.makeText(

                                UserManagerActivity.this,

                                t.getMessage(),

                                Toast.LENGTH_LONG

                        ).show();
                    }
                }
        );
    }

    // =====================================
    // DELETE
    // =====================================

    private void showDeleteDialog(

            UserItem item,

            int position
    ) {

        new AlertDialog.Builder(this)

                .setTitle("Xóa người dùng")

                .setMessage(
                        "Bạn có chắc muốn xóa "
                                + item.username
                                + " ?"
                )

                .setPositiveButton(

                        "Xóa",

                        (dialog, which) -> {

                            deleteUser(
                                    item,
                                    position
                            );
                        }
                )

                .setNegativeButton(
                        "Hủy",
                        null
                )

                .show();
    }

    // =====================================
    // DELETE API
    // =====================================

    private void deleteUser(

            UserItem item,

            int position
    ) {

        repository.deleteUser(

                item.id,

                new Callback<String>() {

                    @Override
                    public void onResponse(

                            Call<String> call,

                            Response<String> response
                    ) {

                        if (response.isSuccessful()) {

                            users.remove(position);

                            adapter.notifyItemRemoved(
                                    position
                            );

                            Toast.makeText(

                                    UserManagerActivity.this,

                                    "Đã xóa user",

                                    Toast.LENGTH_SHORT

                            ).show();
                        }

                        else {

                            Toast.makeText(

                                    UserManagerActivity.this,

                                    "Xóa thất bại",

                                    Toast.LENGTH_SHORT

                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(

                            Call<String> call,

                            Throwable t
                    ) {

                        Toast.makeText(

                                UserManagerActivity.this,

                                t.getMessage(),

                                Toast.LENGTH_LONG

                        ).show();
                    }
                }
        );
    }
}