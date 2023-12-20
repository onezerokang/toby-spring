package kang.onezero.tobyspring.user.service;

import kang.onezero.tobyspring.user.dao.UserDao;

public class UserService {
    UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
