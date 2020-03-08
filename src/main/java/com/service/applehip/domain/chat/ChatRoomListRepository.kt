package com.service.applehip.domain.chat

import org.springframework.data.jpa.repository.JpaRepository

interface ChatRoomListRepository : JpaRepository<ChatRoomList, Long> {
}