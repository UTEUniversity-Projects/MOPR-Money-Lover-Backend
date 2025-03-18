package com.mobile.base.model.entity;

import com.mobile.base.model.audit.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "db_base_group")
@Getter
@Setter
public class Group extends Auditable<String> {
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", type = com.mobile.base.service.id.IdGenerator.class)
    private Long id;

    private String name;

    private String description;

    private Integer kind;

    private Boolean isSystemGroup = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "db_base_permission_group",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private List<Permission> permissions = new ArrayList<>();
}
