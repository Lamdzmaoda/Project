package com.example.democode3.features.admin.fake;

import com.example.democode3.core.enums.RoleType;
import com.example.democode3.features.admin.model.AdminUser;

import java.util.ArrayList;
import java.util.List;

public class FakeAdminApi {

    public List<AdminUser> getUsers() {

        List<AdminUser> list =
                new ArrayList<>();

        list.add(
                new AdminUser(
                        1,
                        "dante_atreides",
                        RoleType.ADMIN,
                        1200
                )
        );

        list.add(
                new AdminUser(
                        2,
                        "alice",
                        RoleType.USER,
                        420
                )
        );

        return list;
    }
}