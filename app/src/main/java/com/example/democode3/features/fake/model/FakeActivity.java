package com.example.democode3.features.fake.model;

public class FakeActivity {

    public String id;

    public String type;

    public String title;

    public String description;

    public String createdAt;

    public FakeActivity(
            String id,
            String type,
            String title,
            String description,
            String createdAt
    ) {

        this.id = id;

        this.type = type;

        this.title = title;

        this.description = description;

        this.createdAt = createdAt;
    }
}