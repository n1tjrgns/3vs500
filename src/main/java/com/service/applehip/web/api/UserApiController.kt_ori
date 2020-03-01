package com.service.applehip.web.api

import com.service.applehip.service.users.UsersService
import com.service.applehip.web.dto.user.UsersResponseDto
import com.service.applehip.web.dto.user.UsersSaveRequestDto
import com.service.applehip.web.dto.user.UsersUpdateRequestDto
import io.leangen.graphql.annotations.GraphQLArgument
import io.leangen.graphql.annotations.GraphQLMutation
import io.leangen.graphql.annotations.GraphQLQuery
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

    /**
     * Mutation 이름으로 updateUser를 생성함.
     */
    @GraphQLMutation(name = "updateUser")
    fun update(@GraphQLArgument(name = "userId") id: Long?,
               @GraphQLArgument(name = "request") requestDto: UsersUpdateRequestDto?): Long? = usersService.update(id, requestDto)
    /*
    코틀린은 중괄호 메소드가 한줄짜리인경우 return을 생략하고 = 을이용할수 있다.
    메소드 괄호 뒤에 Long? 이라고 되어있는것이 리턴타입이다. ? 의 의미는 null이 들어갈수 있는 타입이라는 의미
    즉, Long?은 널일수 있는 Long타입인것이다.
    반대로
    Long 은 null이 들어올경우 Exception이 발생한다.
     */

    /**
     * Query 이름으로 findUser를 생성함.
     */
    @GraphQLQuery(name = "findUser")
    fun findById(@GraphQLArgument(name = "userId") id: Long?): UsersResponseDto? = usersService.findById(id)
}