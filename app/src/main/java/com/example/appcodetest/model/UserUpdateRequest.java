package com.example.appcodetest.model;

public class UserUpdateRequest {

    public String firstName;
    public String lastName;
    public String email;

    public UserUpdateRequest(String firstName) {
        this.firstName = firstName;
    }
}