package com.example.democode3.features.fake.repository;

import com.example.democode3.features.fake.model.FakeAchievement;
import com.example.democode3.features.fake.model.FakeActivity;
import com.example.democode3.features.fake.model.FakeComment;
import com.example.democode3.features.fake.model.FakeLeaderboardUser;
import com.example.democode3.features.fake.model.FakeNotification;
import com.example.democode3.features.fake.model.FakePost;
import com.example.democode3.features.fake.model.FakeSavedPost;
import com.example.democode3.features.fake.model.FakeUser;

import java.util.ArrayList;
import java.util.List;

public class FakeCommunityRepository {

    // =================================================
    // USERS
    // =================================================

    public List<FakeUser> getFakeUsers() {

        List<FakeUser> users =
                new ArrayList<>();

        users.add(

                new FakeUser(

                        "u1",

                        "KaiDev",

                        "Kai Developer",

                        "https://i.pravatar.cc/300?img=1",

                        "Backend engineer ☕",

                        12500,

                        28,

                        31,

                        1200,

                        180,

                        420,

                        true,

                        "Java",

                        "github.com/kaidev",

                        "Grandmaster",

                        "2024"
                )
        );

        users.add(

                new FakeUser(

                        "u2",

                        "NekoCode",

                        "Neko Chan",

                        "https://i.pravatar.cc/300?img=5",

                        "Python + AI 😭🔥",

                        9800,

                        22,

                        14,

                        840,

                        120,

                        260,

                        false,

                        "Python",

                        "github.com/nekocode",

                        "Diamond",

                        "2025"
                )
        );

        users.add(

                new FakeUser(

                        "u3",

                        "HuyBug",

                        "Huy Nguyen",

                        "https://i.pravatar.cc/300?img=12",

                        "Fix one bug create three bugs 😭",

                        7200,

                        18,

                        5,

                        320,

                        90,

                        180,

                        false,

                        "C++",

                        "github.com/huybug",

                        "Platinum",

                        "2025"
                )
        );

        users.add(

                new FakeUser(

                        "u4",

                        "LinhAI",

                        "Linh Tran",

                        "https://i.pravatar.cc/300?img=32",

                        "AI Engineer at OpenAI someday 😭",

                        18300,

                        36,

                        88,

                        3400,

                        220,

                        810,

                        true,

                        "Python",

                        "github.com/linhai",

                        "Legend",

                        "2023"
                )
        );

        users.add(

                new FakeUser(

                        "u5",

                        "ThaoUI",

                        "Thảo UI",

                        "https://i.pravatar.cc/300?img=44",

                        "UI/UX + Android",

                        6700,

                        17,

                        11,

                        530,

                        77,

                        150,

                        false,

                        "Kotlin",

                        "github.com/thaoui",

                        "Gold",

                        "2025"
                )
        );

        // =================================================
        // MORE USERS
        // =================================================

        for (int i = 6; i <= 13; i++) {

            users.add(

                    new FakeUser(

                            "u" + i,

                            "Coder" + i,

                            "User " + i,

                            "https://i.pravatar.cc/300?img=" + (i + 10),

                            "I love coding 😭🔥",

                            1000 * i,

                            i + 5,

                            i * 2,

                            i * 120,

                            i * 40,

                            i * 15,

                            false,

                            "Java",

                            "github.com/user" + i,

                            "Silver",

                            "2025"
                    )
            );
        }

        return users;
    }

    // =================================================
    // POSTS
    // =================================================

    public List<FakePost> getFakePosts() {

        List<FakePost> posts =
                new ArrayList<>();

        posts.add(

                new FakePost(

                        "p1",

                        "u1",

                        "KaiDev",

                        "https://i.pravatar.cc/300?img=1",

                        "Spring Boot API",

                        "Finally finished JWT authentication 😭🔥",

                        "",

                        "SecurityFilterChain chain = http.build();",

                        482,

                        92,

                        true,

                        false,

                        "2 phút trước"
                )
        );

        posts.add(

                new FakePost(

                        "p2",

                        "u2",

                        "NekoCode",

                        "https://i.pravatar.cc/300?img=5",

                        "Python AI",

                        "AI giải code đỉnh thật 😭",

                        "",

                        "print('Hello AI')",

                        300,

                        44,

                        false,

                        true,

                        "12 phút trước"
                )
        );

        posts.add(

                new FakePost(

                        "p3",

                        "u4",

                        "LinhAI",

                        "https://i.pravatar.cc/300?img=32",

                        "GPT API",

                        "Vừa tích hợp AI vào app Android 😭🔥",

                        "",

                        "Retrofit retrofit = new Retrofit.Builder()",

                        840,

                        140,

                        true,

                        true,

                        "1 giờ trước"
                )
        );

        // =================================================
        // RANDOM POSTS
        // =================================================

        for (int i = 4; i <= 80; i++) {

            posts.add(

                    new FakePost(

                            "p" + i,

                            "u" + ((i % 13) + 1),

                            "Coder" + ((i % 13) + 1),

                            "https://i.pravatar.cc/300?img=" + (i % 50),

                            "Coding Post #" + i,

                            "Today I learned something new 😭🔥",

                            "",

                            "System.out.println(\"Hello " + i + "\");",

                            i * 7,

                            i * 2,

                            i % 2 == 0,

                            i % 3 == 0,

                            i + " phút trước"
                    )
            );
        }

        return posts;
    }
    // =================================================
// COMMENTS
// =================================================

    public List<FakeComment> getFakeComments() {

        List<FakeComment> comments =
                new ArrayList<>();

        comments.add(

                new FakeComment(

                        "c1",

                        "p1",

                        "u2",

                        "NekoCode",

                        "https://i.pravatar.cc/300?img=5",

                        "JWT khó thật 😭🔥",

                        42,

                        true,

                        "2 phút trước"
                )
        );

        comments.add(

                new FakeComment(

                        "c2",

                        "p1",

                        "u4",

                        "LinhAI",

                        "https://i.pravatar.cc/300?img=32",

                        "Spring Security moment 😭",

                        20,

                        false,

                        "5 phút trước"
                )
        );

        for (int i = 3; i <= 200; i++) {

            comments.add(

                    new FakeComment(

                            "c" + i,

                            "p" + ((i % 80) + 1),

                            "u" + ((i % 13) + 1),

                            "Coder" + ((i % 13) + 1),

                            "https://i.pravatar.cc/300?img=" + (i % 50),

                            "Comment #" + i + " 😭🔥",

                            i % 40,

                            i % 2 == 0,

                            i + " phút trước"
                    )
            );
        }

        return comments;
    }
    // =================================================
// LEADERBOARD
// =================================================

    public List<FakeLeaderboardUser> getLeaderboard() {

        List<FakeLeaderboardUser> users =
                new ArrayList<>();

        users.add(

                new FakeLeaderboardUser(

                        "u4",

                        "LinhAI",

                        "https://i.pravatar.cc/300?img=32",

                        18300,

                        36,

                        88,

                        810,

                        1
                )
        );

        users.add(

                new FakeLeaderboardUser(

                        "u1",

                        "KaiDev",

                        "https://i.pravatar.cc/300?img=1",

                        12500,

                        28,

                        31,

                        420,

                        2
                )
        );

        users.add(

                new FakeLeaderboardUser(

                        "u2",

                        "NekoCode",

                        "https://i.pravatar.cc/300?img=5",

                        9800,

                        22,

                        14,

                        260,

                        3
                )
        );

        for (int i = 4; i <= 13; i++) {

            users.add(

                    new FakeLeaderboardUser(

                            "u" + i,

                            "Coder" + i,

                            "https://i.pravatar.cc/300?img=" + (i + 10),

                            i * 1000,

                            i + 3,

                            i * 2,

                            i * 20,

                            i
                    )
            );
        }

        return users;
    }
    // =================================================
// NOTIFICATIONS
// =================================================

    public List<FakeNotification> getNotifications() {

        List<FakeNotification> notifications =
                new ArrayList<>();

        notifications.add(

                new FakeNotification(

                        "n1",

                        "LIKE",

                        "KaiDev",

                        "https://i.pravatar.cc/300?img=1",

                        "đã thích bài viết của bạn 😭🔥",

                        false,

                        "2 phút trước"
                )
        );

        notifications.add(

                new FakeNotification(

                        "n2",

                        "COMMENT",

                        "NekoCode",

                        "https://i.pravatar.cc/300?img=5",

                        "đã bình luận bài viết của bạn",

                        false,

                        "12 phút trước"
                )
        );

        notifications.add(

                new FakeNotification(

                        "n3",

                        "FOLLOW",

                        "LinhAI",

                        "https://i.pravatar.cc/300?img=32",

                        "đã theo dõi bạn 😭",

                        true,

                        "1 giờ trước"
                )
        );

        return notifications;
    }
    // =================================================
// ACHIEVEMENTS
// =================================================

    public List<FakeAchievement> getAchievements() {

        List<FakeAchievement> achievements =
                new ArrayList<>();

        achievements.add(

                new FakeAchievement(

                        "a1",

                        "First Blood",

                        "Hoàn thành bài học đầu tiên 😭🔥",

                        "🔥",

                        true,

                        1,

                        1,

                        "2 ngày trước"
                )
        );

        achievements.add(

                new FakeAchievement(

                        "a2",

                        "Bug Hunter",

                        "Fix 100 bugs",

                        "🐛",

                        true,

                        100,

                        100,

                        "1 tuần trước"
                )
        );

        achievements.add(

                new FakeAchievement(

                        "a3",

                        "AI Master",

                        "Sử dụng AI 500 lần",

                        "🤖",

                        false,

                        320,

                        500,

                        ""
                )
        );

        achievements.add(

                new FakeAchievement(

                        "a4",

                        "Code Warrior",

                        "Giải 1000 problems",

                        "⚔️",

                        false,

                        720,

                        1000,

                        ""
                )
        );

        return achievements;
    }
    // =================================================
// ACTIVITIES
// =================================================

    public List<FakeActivity> getActivities() {

        List<FakeActivity> activities =
                new ArrayList<>();

        activities.add(

                new FakeActivity(

                        "ac1",

                        "LESSON",

                        "Hoàn thành bài học",

                        "Python Variables 😭🔥",

                        "2 phút trước"
                )
        );

        activities.add(

                new FakeActivity(

                        "ac2",

                        "POST",

                        "Đăng bài viết mới",

                        "Spring Boot JWT",

                        "1 giờ trước"
                )
        );

        activities.add(

                new FakeActivity(

                        "ac3",

                        "PROBLEM",

                        "Giải thành công problem",

                        "Two Sum",

                        "Hôm qua"
                )
        );

        activities.add(

                new FakeActivity(

                        "ac4",

                        "AI",

                        "Đã hỏi AI",

                        "Giải thích thuật toán DFS",

                        "2 ngày trước"
                )
        );

        return activities;
    }
    // =================================================
// SAVED POSTS
// =================================================

    public List<FakeSavedPost> getSavedPosts() {

        List<FakeSavedPost> posts =
                new ArrayList<>();

        posts.add(

                new FakeSavedPost(

                        "p1",

                        "Spring Boot API",

                        "KaiDev",

                        "2 phút trước"
                )
        );

        posts.add(

                new FakeSavedPost(

                        "p3",

                        "GPT API",

                        "LinhAI",

                        "1 giờ trước"
                )
        );

        posts.add(

                new FakeSavedPost(

                        "p15",

                        "Clean Architecture",

                        "Coder9",

                        "Hôm qua"
                )
        );

        return posts;
    }

}