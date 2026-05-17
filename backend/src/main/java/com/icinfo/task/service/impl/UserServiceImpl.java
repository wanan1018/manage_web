package com.icinfo.task.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.icinfo.task.entity.User;
import com.icinfo.task.mapper.UserMapper;
import com.icinfo.task.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User login(String username, String password) {
        String encrypted = DigestUtil.md5Hex(password);
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, username)
                        .eq(User::getPassword, encrypted)
        );
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        return user;
    }

    @Override
    public void register(String username, String password) {
        User exist = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, username)
        );
        if (exist != null) {
            throw new RuntimeException("用户名已存在");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(DigestUtil.md5Hex(password));
        userMapper.insert(user);
    }

    @Override
    public User getByUsername(String username) {
        return userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, username)
        );
    }

    @Override
    public List<User> getUserList() {
        return userMapper.selectList(
                new LambdaQueryWrapper<User>()
                        .orderByAsc(User::getRole)
                        .orderByAsc(User::getCreateTime)
        );
    }
}
