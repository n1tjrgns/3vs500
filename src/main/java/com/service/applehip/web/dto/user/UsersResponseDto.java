package com.service.applehip.web.dto.user;

import com.service.applehip.domain.users.Users;
import lombok.Getter;

@Getter
public class UsersResponseDto { //User의 select 로직을 담당 할 Dto.

    //여기서 Builder 선언을 하지 않은 이유는 조회의 경우 일부만 가져올 것이기 때문에 Entitiy 클래스의 데이터를 사용한다.
    private Long id;
    private String name;
    private String email;
    private String password;

    public UsersResponseDto(Users entity){
        this.id = entity.getId();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.password = entity.getPassword();
    }

}
