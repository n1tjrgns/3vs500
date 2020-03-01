package com.service.applehip.web.api

import com.service.applehip.service.chat.ChatService
import com.service.applehip.web.dto.chat.ChatSendRequestDto
import com.service.applehip.web.dto.chat.ChatSendResponseDto
import io.leangen.graphql.annotations.GraphQLArgument
import io.leangen.graphql.annotations.GraphQLMutation
import io.leangen.graphql.annotations.GraphQLQuery
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi
import org.springframework.stereotype.Controller

@GraphQLApi
@Controller
class ChatApiController(private val chatService: ChatService) {

    /**
     * 채팅발송 Api
     */
    @GraphQLMutation(name = "sendChat")
    fun sendMsg(@GraphQLArgument(name = "request") msg : ChatSendRequestDto ) : ChatSendResponseDto {
        return chatService.sendMsg(request = msg)
    }

    /**
     * Queue Size를 api서버에서 설정 추후 채팅 서버에서 이 파라미터를 참조
     */
    @GraphQLQuery(name = "getQueueSize")
    fun getParam() = chatService.getQueueSize()
}