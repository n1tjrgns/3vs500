package com.service.applehip.web.api

import com.service.applehip.util.GraphqlMethodParam
import com.service.applehip.util.GraphqlQueryBuilder
import com.service.applehip.util.GraphqlType
import com.service.applehip.util.HamcrestTester
import com.service.applehip.web.dto.chat.ChatRoomInfoSaveRequest
import org.json.JSONObject
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.*
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = ["spring.profiles.active=local"])
class ChatRoomApiControllerTest : HamcrestTester() {

    @LocalServerPort
    private var port = 0
    @Autowired
    private lateinit var restTemplate : TestRestTemplate

    private fun getUrl() = "http://localhost:$port/graphql"

    @Test
    fun 채팅방생성() {
        //given
        val regUserNo = 1L
        val targetUserNo = 2L
        val userList = "$regUserNo|$targetUserNo"
        val requestDto = ChatRoomInfoSaveRequest(
                regUserNo = regUserNo,
                userList = userList
        )
        //query
        val queryName = "createChatRoomInfo"
        val query = GraphqlQueryBuilder(GraphqlType.MUTATION)
                .method(queryName)
                .methodParam("request", GraphqlMethodParam().setParamObject(requestDto))
                .build()
        println("query : $query")

        //when
        val queryJson = JSONObject().also { it.put("query", query) }
        val httpHeaders = HttpHeaders().also { it.contentType = MediaType.APPLICATION_JSON_UTF8 }
        val httpEntity : HttpEntity<String> = HttpEntity(queryJson.toString(), httpHeaders)
        val responseEntity : ResponseEntity<String> = restTemplate.postForEntity(this.getUrl(), httpEntity, String::class.java)

        //then
        this.assertThatEqual(responseEntity.statusCode, HttpStatus.OK)
        val body = responseEntity.body.also { println("responseBody : $it") }
        this.assertThatIsNotNullValue(body)
        val dataJson = JSONObject(body).get("data") as JSONObject
        this.assertThatIsNotNullValue(dataJson)
        val response = dataJson.get(queryName) as String?
        this.assertThatIsNotNullValue(response)
        this.assertThatEqual(response, "success")
    }
}