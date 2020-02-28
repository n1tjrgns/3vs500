package com.service.applehip.web.api

import com.service.applehip.web.dto.chat.ChatSendRequestDto
import org.assertj.core.api.Assertions
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.hamcrest.core.IsNull
import org.json.JSONObject
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.*
import org.springframework.test.context.junit4.SpringRunner
import kotlin.random.Random

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //랜덤 포트로 테스트 환경 설정
class ChatApiControllerTest {

    @LocalServerPort
    private var port = 0
    @Autowired
    private lateinit var restTemplate : TestRestTemplate
    // graphql 은 고정 url
    private fun getUrl() = "http://localhost:$port/graphql"

    @Test
    fun 기본_URL_테스트() {
        //given
        val roomId = Random.nextInt()
        val msg = "This is a 테스트 메시지"
        val requestId = Random.nextInt()
        val requestDto = ChatSendRequestDto(
                roomId = roomId,
                msg = msg,
                requestId = requestId
        )

        val queryName = "sendChat"

        /* query 설정

            mutation {
                saveUser (
                    request : {
                        roomId : ${requestDto.roomId},
                        msg: : "${requestDto.msg}",
                        requestId : ${requestDto.requestId}
                    }
                )
            }

         */
        val query = "mutation {" +          // 데이터를 변경하는 작업은 mutation 이 들어감
                           "    $queryName (" +                // query 명  @GraphQLMutation 참조
                           "        request : {" +               // 변수 명   @GraphQLArgument 참조
                           "            roomId:${requestDto.roomId}, " +      // 코틀린에서는 $변수명 ${변수명} 을 하면 값이 대입됨.
                           "            msg:\"${requestDto.msg}\", " +
                           "            requestId:${requestDto.requestId} " +
                           "        }" +
                           ") {" +
                           "    result,detailMsg}" +
                           "}"

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
        MatcherAssert.assertThat(body, IsNull.notNullValue()) // return value가 null이면 안됨

        val dataJson = JSONObject(body).get("data") as JSONObject
        MatcherAssert.assertThat(dataJson.get(queryName), IsNull.notNullValue())
        val response = dataJson.get(queryName) as JSONObject
        MatcherAssert.assertThat(response["result"] as Boolean, Matchers.equalTo(false))
        MatcherAssert.assertThat(response["detailMsg"] as String, Matchers.equalTo(""))
    }
}