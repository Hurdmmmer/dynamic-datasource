package com.example.dynamicdatasource.service;

import com.example.dynamicdatasource.entity.User;

public interface IUserService {
    int addUser(User user);

    User findByUsername(String username);
}
