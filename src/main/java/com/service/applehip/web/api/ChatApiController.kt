package com.service.applehip.web.api

import com.service.applehip.web.dto.chat.ChatSendRequestDto
import com.service.applehip.web.dto.chat.ChatSendResponseDto
import io.leangen.graphql.annotations.GraphQLArgument
import io.leangen.graphql.annotations.GraphQLMutation
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi
import org.springframework.stereotype.Controller

@GraphQLApi
@Controller
class ChatApiController {

    @GraphQLMutation(name = "sendChat")
    fun sendMsg(@GraphQLArgument(name = "request") msg : ChatSendRequestDto ) : ChatSendResponseDto {
        return ChatSendResponseDto(false, "")
    }
}