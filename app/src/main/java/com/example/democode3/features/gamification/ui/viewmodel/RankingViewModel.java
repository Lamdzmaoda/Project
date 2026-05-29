package com.example.democode3.features.gamification.ui.viewmodel;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.democode3.features.fake.model.FakeUser;
import com.example.democode3.features.fake.session.FakeSessionManager;
import com.example.democode3.features.gamification.model.RankingUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RankingViewModel
        extends AndroidViewModel {

    // =====================================
    // DATA
    // =====================================

    private final MutableLiveData<List<RankingUser>>
            ranking =
            new MutableLiveData<>();

    // =====================================
    // LOADING
    // =====================================

    private final MutableLiveData<Boolean>
            loading =
            new MutableLiveData<>(false);

    // =====================================
    // SESSION
    // =====================================

    private final FakeSessionManager session =
            FakeSessionManager.getInstance();

    // =====================================
    // CONSTRUCTOR
    // =====================================

    public RankingViewModel(
            @NonNull Application application
    ) {

        super(application);
    }

    // =====================================
    // GET RANKING
    // =====================================

    public LiveData<List<RankingUser>>
    getRanking() {

        return ranking;
    }

    // =====================================
    // LOADING
    // =====================================

    public LiveData<Boolean>
    isLoading() {

        return loading;
    }

    // =====================================
    // WEEK
    // =====================================

    public void loadWeeklyRanking() {

        loadFakeRanking();
    }

    // =====================================
    // MONTH
    // =====================================

    public void loadMonthlyRanking() {

        loadFakeRanking();
    }

    // =====================================
    // LEGEND
    // =====================================

    public void loadLegendRanking() {

        loadFakeRanking();
    }

    // =====================================
    // LOAD
    // =====================================

    private void loadFakeRanking() {

        loading.setValue(true);

        new Handler(

                Looper.getMainLooper()

        ).postDelayed(() -> {

            List<FakeUser> users =
                    session.getAllUsers();

            List<RankingUser> result =
                    new ArrayList<>();

            // =============================
            // MAP
            // =============================

            for (FakeUser fake : users) {

                RankingUser user =
                        new RankingUser();

                user.id =
                        fake.id;

                user.username =
                        fake.username;

                user.avatarUrl =
                        fake.avatar;

                user.level =
                        fake.level;

                user.xp =
                        fake.xp;

                result.add(user);
            }

            // =============================
            // SORT
            // =============================

            Collections.sort(

                    result,

                    (a, b) -> Integer.compare(
                            b.xp,
                            a.xp
                    )
            );

            // =============================
            // RANK
            // =============================

            for (

                    int i = 0;

                    i < result.size();

                    i++
            ) {

                result.get(i).rank =
                        i + 1;
            }

            ranking.setValue(result);

            loading.setValue(false);

        }, 1200);
    }
}