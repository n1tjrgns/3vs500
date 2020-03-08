package com.service.applehip.util

import com.service.applehip.domain.seq.TableSeqRepository
import org.springframework.stereotype.Component

@Component
class SeqUtil(
        private var tableSeqRepository: TableSeqRepository
) {
    /**
     * 다음 SEQ를 가져오고, 테이블에 저장.
     */
    fun getNextSeq(tableName : TableName): Long {
        val tableSeq = this.tableSeqRepository.findById(tableName.tableName).get()
        val seq = tableSeq.getNextSeq()
        this.tableSeqRepository.save(tableSeq)
        return seq
    }

    /**
     * 현재 SEQ를 가져옴.
     */
    fun getCurrentSeq(tableName : TableName) =
        this.tableSeqRepository.findById(tableName.tableName).get().getCurrentSeq()
}

/**
 * SEQ 를 이용하는 테이블 목록
 */
enum class TableName(val tableName : String) {
    CHATROOM_INFO("CHATROOM_INFO")
}