package com.service.applehip.domain.image

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "IMAGE")
class Image(
        @Id
        var imageNo : Long? = null,
        @Column
        private var awsKey : String = "",
        @Column
        var url : String = "",
        @Column
        private var regUserNo : Long? = 0,
        @Column
        @CreatedDate
        var regDate : LocalDateTime? = null,
        @Column
        @LastModifiedDate
        var uptDate : LocalDateTime? = null
) {

}