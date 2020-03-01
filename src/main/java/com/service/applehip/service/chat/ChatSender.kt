package com.service.applehip.service.chat

import com.service.applehip.web.dto.chat.ChatSendRequestDto
import com.service.applehip.web.dto.chat.ChatSendResponseDto

/**
 * 이후 큐가 변경될것을 감안하여 인터페이스로 구현
 */
interface ChatSender {
    /**
     * queue 발송
     */
    fun sendMsg(queueName: String, request: ChatSendRequestDto): ChatSendResponseDto
}