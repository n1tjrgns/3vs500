package com.service.applehip.service.chat

import com.service.applehip.domain.chat.ChatRoomInfoRepository
import com.service.applehip.web.dto.chat.ChatRoomInfoSaveRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.lang.Exception

@Service
class ChatRoomService(private var chatRoomInfoRepository: ChatRoomInfoRepository) {

    companion object {
        val logger = LoggerFactory.getLogger(this::class.java)!!
    }

    fun saveChatRoomInfo(request : ChatRoomInfoSaveRequest) : String {
        try {
            chatRoomInfoRepository.save(request.toEntity())
            return "success"
        } catch (exception : Exception) {
            logger.error(exception.message)
            return "fail"
        }
    }
}