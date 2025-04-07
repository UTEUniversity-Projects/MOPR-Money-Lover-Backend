package com.mobile.api.model.entity;

import com.mobile.api.model.audit.Auditable;
import com.mobile.api.service.id.IdGenerator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "db_money_lover_account")
@Getter
@Setter
public class Account extends Auditable<String> {
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", type = IdGenerator.class)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @OneToOne
    @JoinColumn(name = "avatar_id", referencedColumnName = "id")
    private File avatar;

    @Column(name = "is_super_admin")
    private Boolean isSuperAdmin = false;

    @Column(name = "verified")
    private Boolean verified = false;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
}
