package com.icinfo.task.service;

import com.icinfo.task.entity.User;

import java.util.List;

public interface UserService {
    User login(String username, String password);
    void register(String username, String password);
    User getByUsername(String username);
    List<User> getUserList();
}
