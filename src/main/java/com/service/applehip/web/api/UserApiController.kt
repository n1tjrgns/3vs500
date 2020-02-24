package com.service.applehip.web.api

import com.service.applehip.service.users.UsersService
import com.service.applehip.web.dto.user.UsersSaveRequestDto
import io.leangen.graphql.annotations.GraphQLArgument
import io.leangen.graphql.annotations.GraphQLMutation
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi
import org.springframework.stereotype.Controller

/**
 * Api for User
 * @GraphQLApi 는 @RestController 와 비슷
 * val은 final이랑 같음. 기본생성자는 클래스 옆에 쓸수있음
 */
@GraphQLApi
@Controller
class UserApiController(private val usersService: UsersService) { // private final UsersService usersService

    /**
     * Mutation 이름으로 saveUser를 생성함.
     */
    @GraphQLMutation(name = "saveUser") // query 이름
    fun save(@GraphQLArgument(name = "request") requestDto: UsersSaveRequestDto?): Long? = usersService.save(requestDto)
    /*
    위에꺼 java로 하면
    @GraphQLMutation(name = "saveUser")
    final public Long save(@GraphQLArgument(name = "request")UsersSaveRequestDto requestDto) {
        return usersService.save(requestDto);
    }
     */

}