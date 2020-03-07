package com.service.applehip.domain.users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthUser extends JpaRepository<GoogleUser, Long> {

    //이메일로 가입 여부 판단
    Optional<GoogleUser> findByEmail(String email);
}
