package com.service.applehip.web.api

import com.service.applehip.service.chat.ChatRoomService
import com.service.applehip.web.dto.chat.ChatRoomResponseDto
import com.service.applehip.web.dto.chat.MakeChatRoomRequest
import io.leangen.graphql.annotations.GraphQLMutation
import io.leangen.graphql.annotations.GraphQLQuery
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi
import org.springframework.stereotype.Controller

@GraphQLApi
@Controller
class ChatRoomApiController(private var chatRoomService: ChatRoomService) {

    /**
     * 채팅방 생성
     */
    @GraphQLMutation(name = "createChatRoom")
    fun createChatRoom(request : MakeChatRoomRequest) : String {
        return chatRoomService.makeChatRoom(request)
    }

    /**
     * 채팅방 리스트 출력
     */
    @GraphQLQuery(name = "getChatRoomList")
    fun getChatRoomList(userNo : Long) : List<ChatRoomResponseDto> {
        return chatRoomService.getChatRoomList(userNo)
    }
}