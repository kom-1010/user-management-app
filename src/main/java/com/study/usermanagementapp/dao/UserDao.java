package com.study.usermanagementapp.dao;

import com.study.usermanagementapp.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao {
    private JdbcTemplate jdbcTemplate;

    private RowMapper<User> userMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            return user;
        }
    };

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(User user) {
        jdbcTemplate.update(
                "insert into users(id, name, password) values(?, ?, ?)", user.getId(), user.getName(), user.getPassword());
    }

    public void deleteAll() throws SQLException {
        jdbcTemplate.update("delete from users");
    }

    public User get(String id) throws SQLException {
        return jdbcTemplate.queryForObject("select * from users where id = ?", new Object[]{id}, userMapper);
    }

    public int getCount() throws SQLException {
        return jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }

    public List<User> getAll() {
        return jdbcTemplate.query("select * from users order by id", userMapper);
    }
}
