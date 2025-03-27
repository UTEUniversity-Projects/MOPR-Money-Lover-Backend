package com.mobile.api.model.entity;

import com.mobile.api.model.audit.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "db_money_lover_event")
@Getter
@Setter
public class Event extends Auditable<String> {
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", type = com.mobile.api.service.id.IdGenerator.class)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "icon_path")
    private String iconPath;

    @OneToMany(mappedBy = "event")
    private List<Bill> bills;
}
