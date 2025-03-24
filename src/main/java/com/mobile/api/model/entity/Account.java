package com.mobile.api.model.entity;

import com.mobile.api.model.audit.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "db_money_lover_account")
@Getter
@Setter
public class Account extends Auditable<String> {
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", type = com.mobile.api.service.id.IdGenerator.class)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "avatar_path")
    private String avatarPath;

    @Column(name = "is_super_admin")
    private Boolean isSuperAdmin = false;

    @Column(name = "verified")
    private Boolean verified = false;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
}
