package com.service.applehip.web;

import com.service.applehip.domain.Users;
import com.service.applehip.domain.UsersRepository;
import com.service.applehip.web.dto.UsersSaveRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //랜덤 포트로 테스트 환경 설정
public class UsersApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UsersRepository usersRepository;

    @After
    public void tearDown() throws  Exception{
        usersRepository.deleteAll();
    }

    @Test
    public void Users에_api_insert(){
        //given
        String name = "test";
        String email = "test@test.com";
        String password = "123";
        UsersSaveRequestDto requestDto = UsersSaveRequestDto.builder()
                .name(name)
                .email(email)
                .password(password)
                .build();

        String url = "http://localhost:" + port + "/3vs500/v1/users";

        //when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Users> all = usersRepository.findAll();
        assertThat(all.get(0).getName()).isEqualTo(name);
        assertThat(all.get(0).getEmail()).isEqualTo(email);
        assertThat(all.get(0).getPassword()).isEqualTo(password);
    }
}