package com.service.applehip.web.dto.chat

import com.service.applehip.domain.chat.ChatRoomInfo

class ChatRoomInfoSaveRequest(
        var userList : String = "",
        var regUserNo : Long? = null
) {
    fun toEntity() =
            ChatRoomInfo(
                    userList = this.userList,
                    regUserNo = this.regUserNo
            )
}