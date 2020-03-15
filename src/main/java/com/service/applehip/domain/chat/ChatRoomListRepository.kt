package com.service.applehip.domain.chat

import org.springframework.data.jpa.repository.JpaRepository

interface ChatRoomListRepository : JpaRepository<ChatRoomList, ChatRoomListId> {
    // userId로 모든 ChatRoomList를 출력함.
    fun findAllById_UserNo(userId : Long): MutableList<ChatRoomList>?
}