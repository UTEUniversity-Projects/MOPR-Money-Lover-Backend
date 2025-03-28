package com.mobile.api.model.entity;

import com.mobile.api.model.audit.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "db_money_lover_notification")
@Getter
@Setter
public class Notification extends Auditable<String> {
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", type = com.mobile.api.service.id.IdGenerator.class)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "budget_id", referencedColumnName = "id")
    private Budget budget;

    @Column(name = "content")
    private String content;

    @Column(name = "is_read")
    private Boolean isRead = false;

    @Column(name = "icon_path")
    private String iconPath;
}
