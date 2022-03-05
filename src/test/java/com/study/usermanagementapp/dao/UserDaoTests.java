package com.study.usermanagementapp.dao;

import com.study.usermanagementapp.entity.User;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class UserDaoTests {

    @Test
    public void addAndGet() throws SQLException, ClassNotFoundException {
        UserDao dao = new UserDao();

        User user = new User();
        user.setId("abc");
        user.setName("jinsu");
        user.setPassword("1234");

        dao.add(user);

        System.out.println(user.getId() + "등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());
        System.out.println(user2.getId() + "조회 성공");
    }
}
