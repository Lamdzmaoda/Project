package com.example.democode3.features.gamification.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.democode3.features.gamification.model.RankingUser;
import com.example.democode3.features.gamification.repository.RankingRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RankingViewModel
        extends AndroidViewModel {

    // =====================================
    // DATA
    // =====================================

    private final MutableLiveData<List<RankingUser>>
            ranking =
            new MutableLiveData<>();

    // =====================================
    // REPOSITORY
    // =====================================

    private final RankingRepository repository;

    // =====================================
    // CONSTRUCTOR
    // =====================================

    public RankingViewModel(
            @NonNull Application application
    ) {

        super(application);

        repository =
                new RankingRepository(
                        application
                );
    }

    // =====================================
    // GET
    // =====================================

    public LiveData<List<RankingUser>>
    getRanking() {

        return ranking;
    }

    // =====================================
    // WEEK
    // =====================================

    public void loadWeeklyRanking() {

        repository.getWeeklyRanking(

                new Callback<List<RankingUser>>() {

                    @Override
                    public void onResponse(

                            Call<List<RankingUser>> call,

                            Response<List<RankingUser>> response
                    ) {

                        ranking.setValue(
                                response.body()
                        );
                    }

                    @Override
                    public void onFailure(

                            Call<List<RankingUser>> call,

                            Throwable t
                    ) {

                    }
                }
        );
    }

    // =====================================
    // MONTH
    // =====================================

    public void loadMonthlyRanking() {

        repository.getMonthlyRanking(

                new Callback<List<RankingUser>>() {

                    @Override
                    public void onResponse(

                            Call<List<RankingUser>> call,

                            Response<List<RankingUser>> response
                    ) {

                        ranking.setValue(
                                response.body()
                        );
                    }

                    @Override
                    public void onFailure(

                            Call<List<RankingUser>> call,

                            Throwable t
                    ) {

                    }
                }
        );
    }

    // =====================================
    // LEGEND
    // =====================================

    public void loadLegendRanking() {

        repository.getLegendRanking(

                new Callback<List<RankingUser>>() {

                    @Override
                    public void onResponse(

                            Call<List<RankingUser>> call,

                            Response<List<RankingUser>> response
                    ) {

                        ranking.setValue(
                                response.body()
                        );
                    }

                    @Override
                    public void onFailure(

                            Call<List<RankingUser>> call,

                            Throwable t
                    ) {

                    }
                }
        );
    }
}