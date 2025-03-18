package com.mobile.base.model.audit;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mobile.base.constant.BaseConstant;
import com.mobile.base.model.ReuseId;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable<T> extends ReuseId {
    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    private T createdBy;

    @CreatedDate
    @Column(name = "created_date" ,nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedBy
    @Column(name = "modified_by", nullable = false)
    private T modifiedBy;

    @LastModifiedDate
    @Column(name = "modified_date", nullable = false)
    private LocalDateTime modifiedDate;

    @Column(name = "status", nullable = false)
    private Integer status = BaseConstant.STATUS_ACTIVE;
}
