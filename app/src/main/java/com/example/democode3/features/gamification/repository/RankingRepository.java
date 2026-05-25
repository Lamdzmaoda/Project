package com.example.democode3.features.gamification.repository;

import android.content.Context;

import com.example.democode3.core.network.RetrofitClient;

import com.example.democode3.features.gamification.api.RankingApiService;
import com.example.democode3.features.gamification.model.RankingUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class RankingRepository {

    // =====================================
    // API
    // =====================================

    private final RankingApiService api;

    // =====================================
    // CONSTRUCTOR
    // =====================================

    public RankingRepository(
            Context context
    ) {

        api =
                RetrofitClient

                        .getInstance(context)

                        .create(
                                RankingApiService.class
                        );
    }

    // =====================================
    // WEEK
    // =====================================

    public void getWeeklyRanking(

            Callback<List<RankingUser>> callback
    ) {

        api.getWeeklyRanking()
                .enqueue(callback);
    }

    // =====================================
    // MONTH
    // =====================================

    public void getMonthlyRanking(

            Callback<List<RankingUser>> callback
    ) {

        api.getMonthlyRanking()
                .enqueue(callback);
    }

    // =====================================
    // LEGEND
    // =====================================

    public void getLegendRanking(

            Callback<List<RankingUser>> callback
    ) {

        api.getLegendRanking()
                .enqueue(callback);
    }
}