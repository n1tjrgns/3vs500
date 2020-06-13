package com.service.applehip.config.auth.dto;

import com.service.applehip.domain.users.GoogleUser;
import com.service.applehip.domain.users.Users;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {  //인증된 사용자의 정보를 담을 클래스
    private String name;
    private String email;
    private String picture;


    //TODO 구글 엔티티 -> Users 엔티티
    public SessionUser(GoogleUser googleUser){
        this.name = googleUser.getName();
        this.email = googleUser.getEmail();
        this.picture = googleUser.getPicture();
    }
    public SessionUser(Users user){
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
