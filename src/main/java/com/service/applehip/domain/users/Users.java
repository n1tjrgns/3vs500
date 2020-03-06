package com.service.applehip.domain.users;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Users {//extends BaseTimeEntity //회원정보 클래스

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    //회원 Pk

    @Column(length = 20)
    private String name;    //회원 이름

    @Column(length = 50)
    private String email;   //회원 이메일

    @Column(length = 128)
    private String password;    //회원 비밀번호

    @Builder
    public Users(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    //update 메소드
    public void update(String password){
        this.password = password;
    }
}
