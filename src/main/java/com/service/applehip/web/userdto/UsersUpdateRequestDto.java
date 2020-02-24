package com.service.applehip.web.userdto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UsersUpdateRequestDto { //User의 update 로직을 담당 할 Dto.
    private String password;

    @Builder
    public UsersUpdateRequestDto(String password){
        this.password = password;
    }
}
