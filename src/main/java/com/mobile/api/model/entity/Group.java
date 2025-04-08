package com.mobile.api.model.entity;

import com.mobile.api.model.audit.Auditable;
import com.mobile.api.service.id.IdGenerator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "db_money_lover_group")
@Getter
@Setter
public class Group extends Auditable<String> {
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", type = IdGenerator.class)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "kind")
    private Integer kind;

    @Column(name = "is_system_group")
    private Boolean isSystemGroup = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "db_money_lover_permission_group",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private List<Permission> permissions = new ArrayList<>();
}
