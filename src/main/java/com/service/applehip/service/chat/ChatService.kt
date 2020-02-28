package com.service.applehip.service.chat

import com.service.applehip.web.dto.chat.ChatSendRequestDto
import com.service.applehip.web.dto.chat.ChatSendResponseDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class ChatService(private val chatSender: ChatSender) {

    @Value(value = "\${chat.queue.size}")
    private lateinit var queueSize : String
    @Value(value = "\${chat.queue.prefix}")
    private lateinit var queuePrefix : String

    fun sendMsg(request: ChatSendRequestDto) : ChatSendResponseDto {
        val queueNo = request.roomId % queueSize.toInt()
        return if(queueNo < queueSize.toInt()/2) {
            ChatSendResponseDto(true, queuePrefix+"_"+queueNo)
        } else {
            ChatSendResponseDto(false, queuePrefix+"_"+queueNo)
        }
    }

    fun getQueueSize() = queueSize.toInt()
}