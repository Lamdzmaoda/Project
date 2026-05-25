package com.example.democode3.features.profile.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.democode3.R;

import com.example.democode3.core.model.ApiResponse;

import com.example.democode3.features.profile.model.ProfileResult;

import com.example.democode3.features.profile.repository.ProfileRepository;

import com.example.democode3.features.profile.ui.adapter.FollowUserAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchUserActivity
        extends AppCompatActivity {

    // =====================================
    // VIEW
    // =====================================

    private EditText edtSearch;

    private RecyclerView recyclerUser;

    // =====================================
    // ADAPTER
    // =====================================

    private FollowUserAdapter adapter;

    // =====================================
    // DATA
    // =====================================

    private final List<ProfileResult>
            users =
            new ArrayList<>();

    // =====================================
    // REPOSITORY
    // =====================================

    private ProfileRepository repository;

    // =====================================
    // CREATE
    // =====================================

    @Override
    protected void onCreate(
            @Nullable Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_search_user
        );

        initView();

        setupRecycler();

        setupSearch();
    }

    // =====================================
    // INIT VIEW
    // =====================================

    private void initView() {

        edtSearch =
                findViewById(
                        R.id.edtSearch
                );

        recyclerUser =
                findViewById(
                        R.id.recyclerUser
                );

        repository =
                new ProfileRepository(this);
    }

    // =====================================
    // RECYCLER
    // =====================================

    private void setupRecycler() {

        adapter =
                new FollowUserAdapter(users);

        recyclerUser.setLayoutManager(

                new LinearLayoutManager(this)
        );

        recyclerUser.setAdapter(adapter);
    }

    // =====================================
    // SEARCH
    // =====================================

    private void setupSearch() {

        edtSearch.addTextChangedListener(

                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(

                            CharSequence s,

                            int start,

                            int count,

                            int after
                    ) {

                    }

                    @Override
                    public void onTextChanged(

                            CharSequence s,

                            int start,

                            int before,

                            int count
                    ) {

                        search(
                                s.toString()
                        );
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s
                    ) {

                    }
                }
        );
    }

    // =====================================
    // SEARCH USER
    // =====================================

    private void search(
            String keyword
    ) {

        if (keyword.trim().isEmpty()) {

            users.clear();

            adapter.notifyDataSetChanged();

            return;
        }

        repository.searchUsers(

                keyword,

                new Callback<ApiResponse<ProfileResult>>() {

                    @Override
                    public void onResponse(

                            Call<ApiResponse<ProfileResult>> call,

                            Response<ApiResponse<ProfileResult>> response
                    ) {

                        users.clear();

                        if (

                                response.isSuccessful()

                                        &&

                                        response.body() != null

                                        &&

                                        response.body().result != null
                        ) {

                            users.add(
                                    response.body().result
                            );
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(

                            Call<ApiResponse<ProfileResult>> call,

                            Throwable t
                    ) {

                        t.printStackTrace();
                    }
                }
        );
    }
}