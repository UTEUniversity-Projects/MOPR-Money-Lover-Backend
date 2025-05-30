package com.mobile.api.model;

import com.mobile.api.model.audit.Auditable;
import com.mobile.api.service.id.IdGenerator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;

@Entity
@Table(name = "system_token")
@Getter
@Setter
public class Token extends Auditable<String> {
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", type = IdGenerator.class)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "token", columnDefinition = "TEXT")
    private String token;

    @Column(name = "kind")
    private Integer kind;

    @Column(name = "expiry_time")
    private Instant expiryTime;
}
