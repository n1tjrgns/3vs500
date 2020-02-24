package com.service.applehip.web.api

import com.service.applehip.domain.users.Users
import com.service.applehip.domain.users.UsersRepository
import com.service.applehip.web.dto.user.UsersSaveRequestDto
import com.service.applehip.web.dto.user.UsersUpdateRequestDto
import org.assertj.core.api.Assertions
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.core.IsNull
import org.json.JSONObject
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.*
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //랜덤 포트로 테스트 환경 설정
class UserApiControllerTest {

    @LocalServerPort
    private var port = 0

    @Autowired
    private lateinit var restTemplate : TestRestTemplate //테스트를 위한 restTemplate

    @Autowired
    private lateinit var usersRepository: UsersRepository

    // graphql 은 고정 url
    private fun getUrl() = "http://localhost:$port/graphql"

    @After
    @Throws(Exception::class)
    fun tearDown()= usersRepository.deleteAll()

    @Test
    fun 회원정보_저장하기() {
        //given
        val name = "test권"
        val email = "test@test.com"
        val password = "123"
        val requestDto = UsersSaveRequestDto.builder()
                .name(name)
                .email(email)
                .password(password)
                .build()

        val queryName = "saveUser"

        // query 설정
        val query = "mutation {" +          // 데이터를 변경하는 작업은 mutation 이 들어감
                    " $queryName (" +                // query 명  @GraphQLMutation 참조
                    " request : {" +               // 변수 명   @GraphQLArgument 참조
                    " name:\"${requestDto.name}\", " +      // 코틀린에서는 $변수명 ${변수명} 을 하면 값이 대입됨.
                    " email:\"${requestDto.email}\", " +
                    " password:\"${requestDto.password}\" })}"

        // .also는 also안에서 it을 이용하여 해당 객체를 조작한뒤 다시 해당 객체를 반환함.
        // Kotlin에서는 new를 생략할수있음
        val queryJson = JSONObject().also { it.put("query", query) }
        val httpHeaders = HttpHeaders().also { it.contentType = MediaType.APPLICATION_JSON_UTF8 }  // json 형식으로 던져야함
        val httpEntity : HttpEntity<String> = HttpEntity(queryJson.toString(), httpHeaders) //header와 함께 보내기 위함.

        //when
        val responseEntity: ResponseEntity<String> = restTemplate.postForEntity(this.getUrl(), httpEntity, String::class.java)

        //then
        Assertions.assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.OK)
        val body = responseEntity.body
        assertThat(body, IsNull.notNullValue()) // return value가 null이면 안됨

        val dataJson = JSONObject(body).get("data") as JSONObject
        assertThat(dataJson.get(queryName), IsNull.notNullValue())
        assertThat(dataJson.get(queryName) as Int, Matchers.greaterThanOrEqualTo(1))

        val all: List<Users> = usersRepository.findAll()
        Assertions.assertThat(all[0].name).isEqualTo(name)
        Assertions.assertThat(all[0].email).isEqualTo(email)
        Assertions.assertThat(all[0].password).isEqualTo(password)
    }

    @Test
    fun 회원정보_변경하기() {
        //given
        val savedUsers = usersRepository.save(Users.builder()
                .name("test권")
                .email("test@test.com")
                .password("123")
                .build())


        val modifyPassword = "456789"

        val requestDto = UsersUpdateRequestDto.builder()
                .password(modifyPassword)
                .build()

        val updateId = savedUsers.id    // getMethod는 생략가능 get 생략가능
        val queryName = "updateUser"
        // query 설정
        val query = "mutation {" +          // 데이터를 변경하는 작업은 mutation 이 들어감
                " $queryName (" +                // query 명  @GraphQLMutation 참조
                " userId : $updateId ," +
                " request : {" +               // 변수 명   @GraphQLArgument 참조
                " password:\"${requestDto.password}\" })}"

        // .also는 also안에서 it을 이용하여 해당 객체를 조작한뒤 다시 해당 객체를 반환함.
        // Kotlin에서는 new를 생략할수있음
        val queryJson = JSONObject().also { it.put("query", query) }
        val httpHeaders = HttpHeaders().also { it.contentType = MediaType.APPLICATION_JSON_UTF8 }  // json 형식으로 던져야함
        val httpEntity : HttpEntity<String> = HttpEntity(queryJson.toString(), httpHeaders) //header와 함께 보내기 위함.

        //when
        val responseEntity: ResponseEntity<String> = restTemplate.postForEntity(this.getUrl(), httpEntity, String::class.java)

        //then
        Assertions.assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.OK)
        val body = responseEntity.body
        assertThat(body, IsNull.notNullValue()) // return value가 null이면 안됨

        val dataJson = JSONObject(body).get("data") as JSONObject
        assertThat(dataJson.get(queryName), IsNull.notNullValue())
        assertThat((dataJson.get(queryName) as Int).toLong(), Matchers.equalTo(updateId))

        val all: List<Users> = usersRepository.findAll()
        Assertions.assertThat(all[0].password).isEqualTo(modifyPassword)
    }
}