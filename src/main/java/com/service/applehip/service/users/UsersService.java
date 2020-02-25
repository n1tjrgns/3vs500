package com.service.applehip.service.users;

import com.service.applehip.domain.users.Users;
import com.service.applehip.domain.users.UsersRepository;
import com.service.applehip.web.dto.user.UsersResponseDto;
import com.service.applehip.web.dto.user.UsersSaveRequestDto;
import com.service.applehip.web.dto.user.UsersUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UsersService { //회원정보 서비스 로직

    private final UsersRepository usersRepository;

    @Transactional
    public Long save(UsersSaveRequestDto requestDto) {
        return usersRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, UsersUpdateRequestDto requestDto) {
        Users users = usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 id가 없습니다. id="+ id));

        users.update(requestDto.getPassword());

        return id;
    }

    public UsersResponseDto findById(Long id) {
        Users entity = usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 id가 없습니다. id="+ id));

        return new UsersResponseDto(entity);
    }
}
