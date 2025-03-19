package com.mobile.api.model.entity;

import com.mobile.api.model.audit.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "db_base_account")
@Getter
@Setter
public class Account extends Auditable<String> {
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", type = com.mobile.api.service.id.IdGenerator.class)
    private Long id;

    private String username;

    private String password;

    private String email;

    private String phone;

    private String avatarPath;

    private Boolean isSuperAdmin = false;

    private String resetPwdCode;

    private LocalDateTime resetPwdTime;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
}
