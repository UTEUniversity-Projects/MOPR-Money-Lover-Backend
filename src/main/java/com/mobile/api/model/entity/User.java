package com.mobile.api.model.entity;

import com.mobile.api.model.audit.Auditable;
import com.mobile.api.service.id.IdGenerator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;

@Entity
@Table(name = "db_money_lover_user")
@Getter
@Setter
public class User extends Auditable<String> {
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", type = IdGenerator.class)
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id", nullable = false)
    private Account account;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "gender")
    private Integer gender;

    @Column(name = "birthday")
    private Instant birthday;
}
