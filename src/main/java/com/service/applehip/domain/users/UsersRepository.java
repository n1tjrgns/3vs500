package com.service.applehip.domain.users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> { //회원정보 Repository

    Optional<Users> findByEmail(String email);
}
