package com.mobile.api.model.entity;

import com.mobile.api.model.audit.Auditable;
import com.mobile.api.service.id.IdGenerator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "db_money_lover_category")
@Getter
@Setter
@NoArgsConstructor
public class Category extends Auditable<String> {
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", type = IdGenerator.class)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_expense")
    private Boolean isExpense;

    @Column(name = "ordering")
    private Integer ordering;

    @ManyToOne
    @JoinColumn(name = "icon_id", nullable = false)
    private File icon;

    public Category(String name, User user, Boolean isExpense, Integer ordering, File icon) {
        this.name = name;
        this.user = user;
        this.isExpense = isExpense;
        this.ordering = ordering;
        this.icon = icon;
    }
}
