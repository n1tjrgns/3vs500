package com.service.applehip.web.api

import com.service.applehip.domain.seq.TableSeq
import com.service.applehip.domain.seq.TableSeqRepository
import com.service.applehip.util.*
import com.service.applehip.web.dto.chat.MakeChatRoomRequest
import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
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
    @Autowired
    private lateinit var tableSeqRepository : TableSeqRepository

    @Before
    fun 테이블_시퀀스_생성() {
        tableSeqRepository.save(TableSeq(tableName = "CHATROOM_INFO", currentSeq = 0))
    }

    @Test
    fun 채팅방생성() {
        //given
        val regUserNo = 1L
        val targetUserNo = 2L
        val requestDto = MakeChatRoomRequest(
                requestUserNo = regUserNo,
                targetUserNo = targetUserNo
        )
        //query
        val queryName = "createChatRoom"
        val query = GraphqlQueryBuilder(GraphqlType.MUTATION)
                .method(queryName)
                .methodParam("request", GraphqlMethodParam().setParamObject(requestDto))
                .build()
        println("query : $query")

        val body = GraphqlTester.graphqlTest(query = query, restTemplate = restTemplate, url = this.getUrl())
        val dataJson = JSONObject(body).get("data") as JSONObject
        this.assertThatIsNotNullValue(dataJson)
        val response = dataJson.get(queryName) as String?
        this.assertThatIsNotNullValue(response)
        this.assertThatEqual(response, "success")
    }
}