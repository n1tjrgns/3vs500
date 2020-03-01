package com.service.applehip.service.chat

import com.service.applehip.web.dto.chat.ChatSendRequestDto
import com.service.applehip.web.dto.chat.ChatSendResponseDto
import org.slf4j.LoggerFactory
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Component
import java.lang.Exception

/**
 * ActiveMQ용 ChatSender
 */
@Component
class ActiveMqChatSender(private val jmsTemplate: JmsTemplate) : ChatSender {
     // java message service

    companion object {
        val logger = LoggerFactory.getLogger(this::class.java)!!
    }

    /**
     * 실제 ActiveMQ로 발송
     */
    override fun sendMsg(queueName: String, request: ChatSendRequestDto): ChatSendResponseDto {
        try {
            jmsTemplate.send(queueName) { // 람다 방식, send(destinationName, MessageCreator) 인데, MessageCreator가 하나의 메소드를 가지고 있음(람다가능)
                session ->
                session.createMapMessage().also {
                    it.setInt("roomId", request.roomId)
                    it.setString("msg", request.msg)
                    it.setInt("requestId",request.requestId)
                }
            }
        } catch (error : Exception) {
            logger.error(error.message)
            return ChatSendResponseDto(result = false, detailMsg =  error.message?:"ETC") //에러가 발생할시 false
        }
        /*
        파라미터명을 쓰고 대입할수 있어서 빌더타입이 필요없음
        빌더타입은 체이닝을 통하여, 어떤 변수에 어떤값을 넣을지 눈에 보기 편하려고 함. (책의 저자도 그런 이유라고 설명)
        하지만 코틀린은 파라미터명을 소스코드상에 박을수 있어서, 구분하기 쉬움
        함수(파라미터명 = 변수)
         */
        return ChatSendResponseDto(result = true, detailMsg = "OK") //이상없으면 true
    }
}