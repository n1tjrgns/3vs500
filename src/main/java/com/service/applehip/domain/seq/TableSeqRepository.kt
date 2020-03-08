package com.service.applehip.domain.seq

import org.springframework.data.jpa.repository.JpaRepository

interface TableSeqRepository : JpaRepository<TableSeq, String> {
}