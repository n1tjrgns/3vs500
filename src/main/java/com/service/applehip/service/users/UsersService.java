package com.service.applehip.service.users;

import com.service.applehip.domain.Users;
import com.service.applehip.domain.UsersRepository;
import com.service.applehip.web.dto.UsersSaveRequestDto;
import com.service.applehip.web.dto.UsersUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UsersService {

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
}
