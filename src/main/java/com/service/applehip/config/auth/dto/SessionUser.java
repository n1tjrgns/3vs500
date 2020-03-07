package com.service.applehip.config.auth.dto;

import com.service.applehip.domain.users.GoogleUser;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {  //인증된 사용자의 정보를 담을 클래스
    private String name;
    private String email;
    private String picture;

    public SessionUser(GoogleUser googleUser){
        this.name = googleUser.getName();
        this.email = googleUser.getEmail();
        this.picture = googleUser.getPicture();
    }
}
