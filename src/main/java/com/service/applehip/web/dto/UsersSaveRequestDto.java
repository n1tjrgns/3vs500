package com.service.applehip.web.dto;

import com.service.applehip.domain.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UsersSaveRequestDto {

    private String name;
    private String email;
    private String password;

    @Builder
    public UsersSaveRequestDto(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Users toEntity() {
        return Users.builder()
                .name(name)
                .email(email)
                .password(password)
                .build();
    }
}
