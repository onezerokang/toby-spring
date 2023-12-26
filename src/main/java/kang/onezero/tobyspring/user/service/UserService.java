package kang.onezero.tobyspring.user.service;

import kang.onezero.tobyspring.user.domain.User;

public interface UserService {
    void add(User user);
    void upgradeLevels();
}
