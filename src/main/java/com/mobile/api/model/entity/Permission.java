package com.mobile.api.model.entity;

import com.mobile.api.model.audit.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "db_money_lover_permission")
@Getter
@Setter
public class Permission extends Auditable<String> {
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", type = com.mobile.api.service.id.IdGenerator.class)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "action")
    private String action;

    @Column(name = "description")
    private String description;

    @Column(name = "name_group")
    private String nameGroup;

    @Column(name = "code")
    private String code;

    @Column(name = "show_menu")
    private Boolean showMenu;

    @Column(name = "groups")
    @ManyToMany(mappedBy = "permissions")
    private List<Group> groups = new ArrayList<>();
}
