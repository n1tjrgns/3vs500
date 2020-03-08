package com.service.applehip.domain.seq

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "TABLE_SEQ")
class TableSeq( //Auto_increment를 생성하지 않기 위한 class
        @Id
        private var tableName : String? = null,
        @Column
        private var currentSeq : Long = 0
) {
    fun getNextSeq() = ++currentSeq
    fun getCurrentSeq() = currentSeq
}