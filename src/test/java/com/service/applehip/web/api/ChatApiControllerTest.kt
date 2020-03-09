package com.service.applehip.web.api

import com.service.applehip.util.*
import com.service.applehip.web.dto.chat.ChatSendRequestDto
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
import org.springframework.jms.core.JmsTemplate
import org.springframework.test.context.junit4.SpringRunner
import javax.jms.MapMessage
import kotlin.random.Random

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
                                ,properties = ["spring.profiles.active=local"]) //랜덤 포트로 테스트 환경 설정
class ChatApiControllerTest : HamcrestTester() {

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

        val body = GraphqlTester.graphqlTest(query = query, restTemplate = restTemplate, url = this.getUrl())
        //then
        val dataJson = JSONObject(body).get("data") as JSONObject // data라는 이름으로 고정됨
        this.assertThatIsNotNullValue(dataJson.get(queryName))
        val response = dataJson.get(queryName) as JSONObject
        this.assertThatEqual(response["result"] as Boolean, true)
        this.assertThatEqual(response["detailMsg"] as String, "OK")

        val queueNo = roomId % queueSize.toInt()
        val message = jmsTemplate.receive(queuePrefix + "_" + queueNo) // 메시지를 가져온다
        this.assertThatIsNotNullValue(message)
        val chat = message as MapMessage
        println(chat)
        this.assertThatEqual(chat.getInt("roomId"), roomId)
        this.assertThatEqual(chat.getInt("requestId"), requestId)
        this.assertThatEqual(chat.getString("msg"), msg)
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
        val body = GraphqlTester.graphqlTest(query = query, restTemplate = restTemplate, url = this.getUrl())
        val dataJson = JSONObject(body).get("data") as JSONObject
        this.assertThatIsNotNullValue(dataJson.get(queryName))
        this.assertThatEqual(dataJson.get(queryName) as Int, queueSize.toInt())
    }
}