package com.example.appcodetest.model;

import java.util.List;

public class Chapter {

    public String id;
    public String languageName;

    public String title;
    public int orderIndex;

    public String lockedStatus;
    public String createAt;

    public List<Lesson> lessons;

    @Override
    public String toString() {
        return title;
    }
}