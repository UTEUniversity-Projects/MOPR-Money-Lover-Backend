package com.mobile.api.model.entity;

import com.mobile.api.model.audit.Auditable;
import com.mobile.api.service.id.IdGenerator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "db_money_lover_wallet")
@Getter
@Setter
public class Wallet extends Auditable<String> {
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", type = IdGenerator.class)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    @Column(name = "is_primary")
    private Boolean isPrimary = false;

    @Column(name = "turn_on_notifications")
    private Boolean turnOnNotifications = true;

    @Column(name = "charge_to_total")
    private Boolean chargeToTotal = true;

    @ManyToOne
    @JoinColumn(name = "icon_id", nullable = false)
    private File icon;
}
