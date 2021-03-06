package com.service.applehip.domain

import com.service.applehip.domain.chat.ChatRoomInfo
import com.service.applehip.domain.chat.ChatRoomInfoRepository
import com.service.applehip.domain.seq.TableSeq
import com.service.applehip.domain.seq.TableSeqRepository
import com.service.applehip.util.SeqUtil
import com.service.applehip.util.TableName
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDateTime
import java.time.ZoneOffset

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
        ,properties = ["spring.profiles.active=local"]) //랜덤 포트로 테스트 환경 설정
class ChatRoomInfoRepositoryTest {

    @Autowired
    private lateinit var chatRoomInfoRepository: ChatRoomInfoRepository
    @Autowired
    private lateinit var seqUtil : SeqUtil
    @Autowired
    private lateinit var tableSeqRepository : TableSeqRepository
    @Before
    fun 테이블_시퀀스_생성() {
        tableSeqRepository.save(TableSeq(tableName = "CHATROOM_INFO", currentSeq = 0))
    }

    @After //단위 테스트가 끝날 때 수행 될 작업을 지정하는 어노테이션
    fun cleanup() {  // 단위 테스트가 끝나면 repo를 delete All 하여 비움.
        chatRoomInfoRepository.deleteAll()
    }

    @Test
    fun 채팅방_생성하기() {    //getUserInfo
        //given
        val userNo = 1L
        val targetUserNo = 2L
        val userList = "$userNo|$targetUserNo"
        val seoul = ZoneOffset.of("+09:00")
        val requestTime = LocalDateTime.now()
        chatRoomInfoRepository.save(ChatRoomInfo(
                id = seqUtil.getNextSeq(TableName.CHATROOM_INFO),
                regUserNo = userNo,
                userList = userList
        ))

        //when
        val chatRoomInfoList = chatRoomInfoRepository.findAll()

        //then
        val chatRoomInfo = chatRoomInfoList[0]
        MatcherAssert.assertThat(chatRoomInfo.id!!, Matchers.greaterThanOrEqualTo(1L))
        MatcherAssert.assertThat(chatRoomInfo.regUserNo, Matchers.equalTo(userNo))
        MatcherAssert.assertThat(chatRoomInfo.maxSeq, Matchers.equalTo(0L))
        MatcherAssert.assertThat(chatRoomInfo.delYn, Matchers.equalTo("N"))
        MatcherAssert.assertThat(chatRoomInfo.delDate.toString(), Matchers.equalTo("null"))
        MatcherAssert.assertThat(chatRoomInfo.userList, Matchers.equalTo(userList))
        MatcherAssert.assertThat(chatRoomInfo.lastDate!!.toEpochSecond(seoul), Matchers.greaterThanOrEqualTo(requestTime.toEpochSecond(seoul)))
        MatcherAssert.assertThat(chatRoomInfo.regDate!!.toEpochSecond(seoul), Matchers.greaterThanOrEqualTo(requestTime.toEpochSecond(seoul)))
    }
}