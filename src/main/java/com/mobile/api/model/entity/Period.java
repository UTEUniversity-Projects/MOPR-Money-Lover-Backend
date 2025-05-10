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

    @Column(name = "type")
    private Integer type;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "total_spent")
    private Double totalSpent;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;
}
