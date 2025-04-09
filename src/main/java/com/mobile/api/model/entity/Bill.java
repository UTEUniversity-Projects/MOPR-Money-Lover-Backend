package com.mobile.api.model.entity;

import com.mobile.api.model.audit.Auditable;
import com.mobile.api.service.id.IdGenerator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Table(name = "db_money_lover_bill")
@Getter
@Setter
public class Bill extends Auditable<String> {
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", type = IdGenerator.class)
    private Long id;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "note")
    private String note;

    @Column(name = "is_included_report")
    private Boolean isIncludedReport = true;

    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToMany
    @JoinTable(name = "db_money_lover_bill_tag",
            joinColumns = @JoinColumn(name = "bill_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @OneToOne
    @JoinColumn(name = "reminder_id")
    private Reminder reminder;

    @OneToOne
    @JoinColumn(name = "picture_id")
    private File picture;
}
