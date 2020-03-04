package com.service.applehip.web.api

import com.service.applehip.util.GraphqlMethodParam
import com.service.applehip.util.GraphqlQueryBuilder
import com.service.applehip.util.GraphqlType
import com.service.applehip.web.dto.chat.ChatSendRequestDto
import org.assertj.core.api.Assertions
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.hamcrest.core.IsNull
import org.json.JSONObject
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.*
import org.springframework.jms.core.JmsTemplate
import org.springframework.test.context.junit4.SpringRunner
import javax.jms.MapMessage
import kotlin.random.Random

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
                                ,properties = ["classpath:application-local.yml"]) //랜덤 포트로 테스트 환경 설정
class ChatApiControllerTest {

    @LocalServerPort
    private var port = 0
    @Autowired
    private lateinit var restTemplate : TestRestTemplate
    @Autowired
    private lateinit var jmsTemplate : JmsTemplate
    // graphql 은 고정 url
    private fun getUrl() = "http://localhost:$port/graphql"

    @Value(value = "\${chat.queue.size}")
    private lateinit var queueSize : String
    @Value(value = "\${chat.queue.prefix}")
    private lateinit var queuePrefix : String

    @Test // 현재는 queue가 꺼져있으면 실패함
    fun 기본_URL_테스트() {
        //given
        val roomId = Random.nextInt(0,10000000)
        val msg = "This is a 테스트 메시지"
        val requestId = Random.nextInt(0,1000000)
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
                ) {
                result,
                detailMsg
                }
            }

         */


        val query = GraphqlQueryBuilder(GraphqlType.MUTATION)
                .method(queryName)
                .methodParam(name = "request", value =  GraphqlMethodParam().setParamObject(requestDto))
                .responseList("result", "detailMsg")
                .build()

        println("query:$query")
        // .also는 also안에서 it을 이용하여 해당 객체를 조작한뒤 다시 해당 객체를 반환함.
        // Kotlin에서는 new를 생략할수있음
        val queryJson = JSONObject().also { it.put("query", query) }
        val httpHeaders = HttpHeaders().also { it.contentType = MediaType.APPLICATION_JSON_UTF8 }  // json 형식으로 던져야함
        val httpEntity : HttpEntity<String> = HttpEntity(queryJson.toString(), httpHeaders) //header와 함께 보내기 위함.

        //when
        val responseEntity: ResponseEntity<String> = restTemplate.postForEntity(this.getUrl(), httpEntity, String::class.java)

        //then
        Assertions.assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.OK)   // http 통신 OK
        val body = responseEntity.body
        println(body)
        MatcherAssert.assertThat(body, IsNull.notNullValue()) // return value가 null이면 안됨

        val dataJson = JSONObject(body).get("data") as JSONObject // data라는 이름으로 고정됨
        MatcherAssert.assertThat(dataJson.get(queryName), IsNull.notNullValue()) //data안에 query이름과 같은 결과값이 존재
        val response = dataJson.get(queryName) as JSONObject
        MatcherAssert.assertThat(response["result"] as Boolean, Matchers.equalTo(true)) // 메시지 발송성공시 true, exception시 false
        MatcherAssert.assertThat(response["detailMsg"] as String, Matchers.equalTo("OK"))

        val queueNo = roomId % queueSize.toInt()
        val message = jmsTemplate.receive(queuePrefix + "_" + queueNo) // 메시지를 가져온다
        MatcherAssert.assertThat(message, Matchers.notNullValue())
        val chat = message as MapMessage
        println(chat)
        MatcherAssert.assertThat(chat.getInt("roomId"), Matchers.equalTo(roomId))
        MatcherAssert.assertThat(chat.getInt("requestId"), Matchers.equalTo(requestId))
        MatcherAssert.assertThat(chat.getString("msg"), Matchers.equalTo(msg))
    }

    @Test
    fun 큐사이즈_가져오기() {
        //given
        val queryName = "getQueueSize"

        /* query 설정

            query {
                getQueueSize
            }

         */
        val query = GraphqlQueryBuilder(GraphqlType.QUERY)
                .method(queryName)
                .build()
        println("query:$query")
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
        println(body)
        MatcherAssert.assertThat(body, IsNull.notNullValue()) // return value가 null이면 안됨

        val dataJson = JSONObject(body).get("data") as JSONObject
        MatcherAssert.assertThat(dataJson.get(queryName), IsNull.notNullValue())
        MatcherAssert.assertThat(dataJson.get(queryName) as Int, Matchers.equalTo(queueSize.toInt()))
    }
}