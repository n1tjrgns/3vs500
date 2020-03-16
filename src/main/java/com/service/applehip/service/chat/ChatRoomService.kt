package com.service.applehip.service.chat

import com.service.applehip.domain.chat.ChatRoomInfoRepository
import com.service.applehip.domain.chat.ChatRoomList
import com.service.applehip.domain.chat.ChatRoomListRepository
import com.service.applehip.domain.users.UsersRepository
import com.service.applehip.util.SeqUtil
import com.service.applehip.util.TableName
import com.service.applehip.web.dto.chat.ChatRoomResponseDto
import com.service.applehip.web.dto.chat.MakeChatRoomRequest
import com.service.applehip.web.dto.user.UsersResponseDto
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.Exception

@Service
open class ChatRoomService(
        private var chatRoomInfoRepository: ChatRoomInfoRepository,
        private var chatRoomListRepository: ChatRoomListRepository,
        private var usersRepository: UsersRepository,
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

    /**
     * 채팅방 리스트 출력
     */
    open fun getChatRoomList(userId : Long): ArrayList<ChatRoomResponseDto> {
        // 채팅방 리스트를 위한 ArrayList 객체 생성
        val result = ArrayList<ChatRoomResponseDto>()

        // userNo 로 모든 채팅방목록을 출력
        val list = this.chatRoomListRepository.findAllById_UserNo(userId) as ArrayList<ChatRoomList>?

        list?.forEach {
            // list에 있는 객체를 chatRoom 이라고 할때
            chatRoom ->
            val chatRoomInfo = chatRoom.id?.chatroomId?.let { // let 의 경우 괄호 안에 행위를 한뒤 마지막 줄에 있는것을 반환함
                // chatRoom의 id가 null 이 아닐때, chatroomId가 null 이 아닐때
                chatRoomId -> //그값을 [chatRoomId] 라고 할때
                this.chatRoomInfoRepository.findById(chatRoomId) // 반환할껏은 이것
            }

            // chatRoomInfo가 있으면
            if(chatRoomInfo?.isPresent == true) {
                // 담을 객체를 생성하고
                val chatRoomResponseDto = ChatRoomResponseDto(chatRoom = chatRoom, chatRoomInfo= chatRoomInfo.get())
                // userList를 찾아서 담음
                val userIdList = chatRoomInfo.get().userList.split("|")
                userIdList.forEach { userId ->
                    val user = usersRepository.findById(userId.toLong())
                    if(user.isPresent) {
                        chatRoomResponseDto.addUser(UsersResponseDto(user.get()))
                    }
                }
                result.add(chatRoomResponseDto)
            }
        }
        return result
    }
}