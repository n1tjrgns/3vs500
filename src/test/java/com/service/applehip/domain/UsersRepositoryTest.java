package com.service.applehip.domain;


import com.service.applehip.domain.users.Users;
import com.service.applehip.domain.users.UsersRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsersRepositoryTest {

    @Autowired
    UsersRepository usersRepository;

    @After //단위 테스트가 끝날 때 수행 될 작업을 지정하는 어노테이션
    public void cleanup(){  // 단위 테스트가 끝나면 repo를 delete All 하여 비움.
        usersRepository.deleteAll();
    }

    @Test
    public void 회원정보_받아오기(){
        //given
        String name ="test";
        String email = "test@test.com";
        String password = "123";

        usersRepository.save(Users.builder()
                .name("test")
                .email("test@test.com")
                .password("123")
                .build());

        //when
        List<Users> usersList = usersRepository.findAll();

        //then
        Users users = usersList.get(0);
        assertThat(users.getName()).isEqualTo(name);
        assertThat(users.getEmail()).isEqualTo(email);
        assertThat(users.getPassword()).isEqualTo(password);
    }
}