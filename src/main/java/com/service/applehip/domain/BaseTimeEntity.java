package com.service.applehip.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass //JPA Entity 클래스들이 이 클래스를 상속할 경우 필드들도 칼럼으로 인식하도록 한다.
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {  //날짜를 자동으로 관리해주기 위한 클래스, Jpa 에서 Auditing라는걸 지원해줌.

    @CreatedDate
    private LocalDateTime createdDate; //엔티티 생성시 생성시각 자동 저장

    @LastModifiedDate
    private LocalDateTime modifiedDate; //조회한 엔티티 변경시 변경 시간 자동 저장
}
