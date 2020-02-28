package com.service.applehip.web.dto.chat

class ChatSendRequestDto(
        var roomId : Int,
        var msg : String,
        var requestId: Int
)

class ChatSendResponseDto(
        var result : Boolean,
        var detailMsg : String
)