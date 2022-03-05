package com.study.usermanagementapp.entity;

public class User {
    String id;
    String name;
    String password;
    String email;
    int login;
    int recommend;
    Level level;

    public User() {}

    public User(String id, String name, String password, String email, int login, int recommend, Level level) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.login = login;
        this.recommend = recommend;
        this.level = level;
    }

    public void upgradeLevel() {
        Level nextLevel = level.nextLevel();
        if (nextLevel == null) {
            throw new IllegalStateException(level + "은 업그레이드가 불가능합니다.");
        }
        else {
            level = nextLevel;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public int getLogin() {
        return login;
    }

    public void setLogin(int login) {
        this.login = login;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
