package com.service.applehip.web;

import com.service.applehip.service.users.UsersService;
import com.service.applehip.web.dto.UsersSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UsersApiController {

    //생성자를 통한 의존성 주입
    private final UsersService usersService;

    @PostMapping("/3vs500/v1/users")
    public Long save(@RequestBody UsersSaveRequestDto requestDto){
        return usersService.save(requestDto);
    }
}
