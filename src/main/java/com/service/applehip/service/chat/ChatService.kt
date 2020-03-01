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

    /**
     * 함수가 길지않은경우에는 아래와같이 할수있음
     * fun 지금함수명() = 다른함수()
     * 위의 경우 다른함수의 return 타입이 지금함수의 return 타입으로 결정됨
     * fun 함수명() = 상수
     */
    fun sendMsg(request: ChatSendRequestDto)
            = chatSender.sendMsg(queueName = this.getQueName(request.roomId), request = request)

    /**
     * 큐사이즈를 int 형으로 반환
     */
    fun getQueueSize() = queueSize.toInt()

    /**
     * queue 이름 생성규칙 prefix_[0 부터 queueSize-1]
     * 같은 채팅방번호일경우 같은 큐에 쌓임, 하지만 단일큐는 많이 처리할수 없음.
     * 큐사이즈를 변경하려면 properties 에서 수정후 재부팅
     */
    private fun getQueName(id : Int) = this.queuePrefix + "_" + (id % this.getQueueSize())
}