package com.service.applehip.domain.chat

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "CHATROOM_LIST")
class ChatRoomList(
        @EmbeddedId
        var id : ChatRoomListId? = null,
        @Column
        var startChatSeq : Long = 0, //채팅방에 참여한 Seq
        @Column
        var lastChatSeq : Long = 0,  //채팅방에서 마지막으로 읽은 Seq
        @Column
        var chatroomName : String = "",
        @Column
        var delYn : String = "N",
        @Column
        var delDate : LocalDateTime? = null,
        @Column
        @CreatedDate
        var joinDate : LocalDateTime = LocalDateTime.now()
)

@Embeddable
class ChatRoomListId( //PK가 여러개의 컬럼을 합쳐서 생성할때
        @Column
        var userNo: Long?,  // MEMBER_INFO TABLE 의 PK
        @Column
        var chatroomId: Long? // CHATROOM_INFO 의 PK
) : Serializable {
        constructor() : this(null, null)
}
