package com.service.applehip.service.chat

import com.service.applehip.domain.chat.ChatRoomInfoRepository
import com.service.applehip.domain.chat.ChatRoomListRepository
import com.service.applehip.util.SeqUtil
import com.service.applehip.util.TableName
import com.service.applehip.web.dto.chat.MakeChatRoomRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.Exception

@Service
open class ChatRoomService(
        private var chatRoomInfoRepository: ChatRoomInfoRepository,
        private var chatRoomListRepository: ChatRoomListRepository,
        private var seqUtil: SeqUtil
) {

    companion object {
        val logger = LoggerFactory.getLogger(this::class.java)!!
    }

    /**
     * 채팅방 생성
     * 1. 채팅방 SEQ를 TABLE_SEQ에서 가져옴
     * 2. 채팅방 정보 테이블에 insert
     * 3. 내 채팅방 목록에 insert
     * 4. 상대방 채팅방 목록에 insert
     * 위 내용은 한 Transaction 으로 이루어져여 하며, exception 이 생길경우 rollback 되어야함
     */
    @Transactional(rollbackFor = [Exception::class])
    open fun makeChatRoom(request : MakeChatRoomRequest) : String {
        return try {
            // SEQ
            val chatRoomInfoSeq = this.seqUtil.getNextSeq(TableName.CHATROOM_INFO)
            this.chatRoomInfoRepository.save(request.chatRoomInfoEntity(chatRoomInfoSeq))
            this.chatRoomListRepository.save(request.addChatRoomListForTargetUserEntity(chatRoomInfoSeq))
            this.chatRoomListRepository.save(request.addChatRoomListForRequestUserEntity(chatRoomInfoSeq))
            "success"
        } catch (exception : Exception) {
            logger.error(exception.message)
            "fail"
        }
    }
}