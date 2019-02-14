package com.example.dynamicdatasource;

import com.example.dynamicdatasource.config.aop.DataSource;
import com.example.dynamicdatasource.entity.User;
import com.example.dynamicdatasource.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DynamicDatasourceApplicationTests {

    @Autowired
    private IUserService userService;


    @Test
    public void contextLoads() {

        User user = User.builder().username("durhummer")
                .password("123")
                .mobile("13868845898")
                .email("123@qq.com")
                .build();

        userService.addUser(user);
    }

    @Test
    public void testFindByUsername() {
        User user = userService.findByUsername("durhummer");
        System.out.println("user = " + user);
    }
}

