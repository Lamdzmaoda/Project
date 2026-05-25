package com.example.democode3.features.gamification.api;

import com.example.democode3.features.gamification.model.RankingUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RankingApiService {

    // =====================================
    // WEEK
    // =====================================

    @GET("api/gamification/ranking/week")
    Call<List<RankingUser>> getWeeklyRanking();

    // =====================================
    // MONTH
    // =====================================

    @GET("api/gamification/ranking/month")
    Call<List<RankingUser>> getMonthlyRanking();

    // =====================================
    // LEGEND
    // =====================================

    @GET("api/gamification/ranking/legend")
    Call<List<RankingUser>> getLegendRanking();
}