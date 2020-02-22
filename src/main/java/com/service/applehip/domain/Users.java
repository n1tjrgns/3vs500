package com.service.applehip.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String name;

    @Column(length = 50)
    private String email;

    @Column(length = 128)
    private String password;

    @Builder
    public Users(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void update(String password){
        this.password = password;
    }
}
