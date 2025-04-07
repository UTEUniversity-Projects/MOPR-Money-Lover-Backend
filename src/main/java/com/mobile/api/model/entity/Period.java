package com.mobile.api.model.entity;

import com.mobile.api.model.audit.Auditable;
import com.mobile.api.service.id.IdGenerator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;

@Entity
@Table(name = "db_money_lover_period")
@Getter
@Setter
public class Period extends Auditable<String> {
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", type = IdGenerator.class)
    private Long id;

    @OneToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;
}
