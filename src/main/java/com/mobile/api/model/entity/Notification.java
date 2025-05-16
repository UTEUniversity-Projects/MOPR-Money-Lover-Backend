package com.mobile.api.model.entity;

import com.mobile.api.model.audit.Auditable;
import com.mobile.api.service.id.IdGenerator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "db_money_lover_notification")
@Getter
@Setter
@NoArgsConstructor
public class Notification extends Auditable<String> {
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", type = IdGenerator.class)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Column(name = "content")
    private String content;

    @Column(name = "is_read")
    private Boolean isRead = false;

    @Column(name = "type")
    private Integer type;

    @Column(name = "scope")
    private Integer scope;

    public Notification(User user, Wallet wallet, String content, Integer type, Integer scope) {
        this.user = user;
        this.wallet = wallet;
        this.content = content;
        this.type = type;
        this.scope = scope;
    }
}
