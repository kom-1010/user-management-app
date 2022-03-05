package com.study.usermanagementapp.service;

import com.study.usermanagementapp.entity.User;

public class TestUserService extends UserService{
    private String id;

    public TestUserService(String id) {
        this.id = id;
    }

    @Override
    protected void upgradeLevel(User user) {
        if (user.getId().equals(id)) throw new TestUserServiceException();
        super.upgradeLevel(user);
    }
}
