package com.service.applehip.web.api

import com.service.applehip.service.chat.ChatRoomService
import com.service.applehip.web.dto.chat.ChatRoomInfoSaveRequest
import io.leangen.graphql.annotations.GraphQLMutation
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi
import org.springframework.stereotype.Controller

@GraphQLApi
@Controller
class ChatRoomApiController(private var chatRoomService: ChatRoomService) {

    @GraphQLMutation(name = "createChatRoomInfo")
    fun createChatRoom(request : ChatRoomInfoSaveRequest) : String {
        return chatRoomService.saveChatRoomInfo(request)
    }
}