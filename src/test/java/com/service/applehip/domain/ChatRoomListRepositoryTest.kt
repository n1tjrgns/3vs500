package com.service.applehip.domain

import com.service.applehip.domain.chat.*
import com.service.applehip.domain.chat.ChatRoomListRepository
import com.service.applehip.domain.users.Users
import com.service.applehip.domain.users.UsersRepository
import com.service.applehip.util.HamcrestTester
import org.junit.After
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDateTime
import java.time.ZoneOffset

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
        ,properties = ["spring.profiles.active=local"])
class ChatRoomListRepositoryTest : HamcrestTester(){
    @Autowired
    private lateinit var chatRoomListRepository: ChatRoomListRepository

    companion object {
        @Autowired
        private lateinit var chatRoomInfoRepository : ChatRoomInfoRepository
        @Autowired
        private lateinit var userRepository : UsersRepository
        @BeforeClass
        fun 테이블_시퀀스_생성() {
            val user  = userRepository.save(
                    Users.builder()
                            .password("123")
                            .name("Test")
                            .email("khmin25@humsuon.com")
                            .build())
            chatRoomInfoRepository.save(
                    ChatRoomInfo(
                            id = 1,
                            regUserNo = user.id,
                            userList = "${user.id}"))
        }
    }


    @After //단위 테스트가 끝날 때 수행 될 작업을 지정하는 어노테이션
    fun cleanup() {  // 단위 테스트가 끝나면 repo를 delete All 하여 비움.
        chatRoomListRepository.deleteAll()
    }

    @Test
    fun 채팅방리스트_생성하기() {    //getUserInfo
        //given
        val userNo = 1L
        val chatRoomId = 1L
        val requestTime = LocalDateTime.now()
        val seoul = ZoneOffset.of("+09:00")
        val entity = chatRoomListRepository.save(
                ChatRoomList(
                    id = ChatRoomListId(
                            userNo = userNo,
                            chatroomId = chatRoomId
                    )
                )
        )

        //when
        val chatRoomList = chatRoomListRepository!!.findAll()

        //then
        val chatRoom = chatRoomList[0]
        this.assertThatGreaterThanOrEqual(chatRoom.id.toString(), ChatRoomListId(userNo = userNo, chatroomId = chatRoomId).toString())
        this.assertThatGreaterThanOrEqual(chatRoom.joinDate.toEpochSecond(seoul), requestTime.toEpochSecond(seoul))
        this.assertThatEqual(chatRoom.chatroomName, "")
        this.assertThatEqual(chatRoom.lastChatSeq, 0L)
        this.assertThatEqual(chatRoom.startChatSeq, 0L)
        this.assertThatEqual(chatRoom.delYn, "N")
        this.assertThatEqual(chatRoom.delDate, null)
    }
}