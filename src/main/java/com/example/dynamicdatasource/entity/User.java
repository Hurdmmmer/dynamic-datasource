package com.example.dynamicdatasource.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private int id;
    private String username;
    private String password;
    private String mobile;
    private String email;
}
