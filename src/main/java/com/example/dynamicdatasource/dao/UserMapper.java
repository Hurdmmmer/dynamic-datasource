package com.example.dynamicdatasource.dao;

import com.example.dynamicdatasource.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface UserMapper {
    int insert(User user);

    List<User> findAll();

    User findByUsername(@Param("username") String username);

    int updateByUsername(User user);
}
