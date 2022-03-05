package com.study.usermanagementapp.service;

import com.study.usermanagementapp.dao.UserDao;
import com.study.usermanagementapp.entity.Level;
import com.study.usermanagementapp.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserServiceTests {
    @Autowired
    UserService userService;
    @Autowired
    UserDao userDao;
    @Autowired
    PlatformTransactionManager transactionManager;
    @Autowired
    MailSender mailSender;

    List<User> users;

    @BeforeEach
    public void setUp(){
        users = Arrays.asList(
                new User("lun", "luncher", "luncher", "lun@abcde.com", 49, 0, Level.BASIC),
                new User("sword", "sword-master", "sword-master", "sword@abcde.com", 50, 0, Level.BASIC),
                new User("blade", "blade", "blade", "blade@abcde.com", 60, 29, Level.SILVER),
                new User("weapon", "weapon-master", "weapon-master", "weapon@abcde.com", 60, 30, Level.SILVER),
                new User("cur", "crusader", "crusader",  "cru@abcde.com",100, 100, Level.GOLD)
        );
    }

    @Test
    public void upgradeAllOrNothing() throws SQLException {
        // 지정된 id를 가진 사용자가 발견되면 예외를 발생시키는 대역 클래스
        UserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(userDao);
        testUserService.setTransactionManager(transactionManager);
        testUserService.setMailSender(mailSender);

        userDao.deleteAll();

        for (User user : users) userDao.add(user);

        try {
            testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch (TestUserServiceException e) {
        }

        checkLevelUpgraded(users.get(1), false);
    }

    @Test
    public void add() {
        userDao.deleteAll();

        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User getUserWithLevel = userDao.get(userWithLevel.getId());
        User getUserWithoutLevel = userDao.get(userWithoutLevel.getId());

        assertThat(getUserWithLevel.getLevel()).isEqualTo(userWithLevel.getLevel());
        assertThat(getUserWithoutLevel.getLevel()).isEqualTo(Level.BASIC);
    }

    @Test
    @DirtiesContext // 컨텍스트의 DI 설정을 변경하는 테스트라는 것을 알려줌
    public void upgradeLevels() throws SQLException {
        userDao.deleteAll();
        for(User user : users) userDao.add(user);

        MockMailSender mockMailSender = new MockMailSender();
        userService.setMailSender(mockMailSender);

        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);

        List<String> requests = mockMailSender.getRequests();
        assertThat(requests.size()).isEqualTo(2);
        assertThat(requests.get(0)).isEqualTo(users.get(1).getEmail());
        assertThat(requests.get(1)).isEqualTo(users.get(3).getEmail());
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User updateUser = userDao.get(user.getId());
        if (upgraded) {
            assertThat(updateUser.getLevel()).isEqualTo(user.getLevel().nextLevel());
        }
        else {
            assertThat(updateUser.getLevel()).isEqualTo(user.getLevel());
        }
    }

}
