package com.example.appcodetest.model;

import java.util.List;

public class Language {

    public String name;
    public String description;
    public String icon;

    public int level;
    public String durationDays;

    public String createAt;

    public List<Chapter> chapters;

    @Override
    public String toString() {
        return name;
    }
}