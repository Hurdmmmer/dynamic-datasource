package com.example.dynamicdatasource.service.impl;

import com.example.dynamicdatasource.config.aop.DataSource;
import com.example.dynamicdatasource.dao.UserMapper;
import com.example.dynamicdatasource.entity.User;
import com.example.dynamicdatasource.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IUserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private javax.sql.DataSource source;

    @Override
    @DataSource
    public int addUser(User user) {
        return userMapper.insert(user);
    }
//    @DataSource
    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }
}
