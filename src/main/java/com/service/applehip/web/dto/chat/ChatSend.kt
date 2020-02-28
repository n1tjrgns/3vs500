package com.service.applehip.web.dto.chat

/**
 * 채팅발송 요청 DTO
 */
class ChatSendRequestDto(
        var roomId : Int,
        var msg : String,
        var requestId: Int
)

/**
 * 채팅발송 결과 DTO
 */
class ChatSendResponseDto(
        var result : Boolean,
        var detailMsg : String
)