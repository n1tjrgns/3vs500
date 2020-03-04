package com.service.applehip.web.api;

import com.service.applehip.service.users.UsersService;
import com.service.applehip.web.dto.user.UsersResponseDto;
import com.service.applehip.web.dto.user.UsersSaveRequestDto;
import com.service.applehip.web.dto.user.UsersUpdateRequestDto;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@GraphQLApi
@Controller
public class UsersGQApiController {

    private final UsersService usersService;

    /*@PostMapping("/3vs500/v1/users")
    public Long save(@RequestBody UsersSaveRequestDto requestDto){
        return usersService.save(requestDto);
    }
*/
    @GraphQLMutation(name = "saveUser")
    public Long save(@GraphQLArgument(name = "request") UsersSaveRequestDto requestDto){
        return usersService.save(requestDto);
    }


    /*@PutMapping("/3vs500/v1/users/{id}")
    public Long update(@PathVariable Long id, @RequestBody UsersUpdateRequestDto requestDto){
        return usersService.update(id, requestDto);
    }*/

    @GraphQLMutation(name = "updateUser")
    public Long update(@GraphQLArgument(name = "userId") Long id, @GraphQLArgument(name = "request") UsersUpdateRequestDto requestDto){
        return usersService.update(id, requestDto);
    }

    /*@GetMapping("/3vs500/v1/users/{id}")
    public UsersResponseDto findById(@PathVariable Long id){
        return usersService.findById(id);
    }*/
    @GraphQLQuery(name = "findUser") //get 방식은 GraphQLQuery 어노테이션 사용
    public UsersResponseDto findById(@GraphQLArgument(name = "userId") Long id){
        return usersService.findById(id);
    }
}
