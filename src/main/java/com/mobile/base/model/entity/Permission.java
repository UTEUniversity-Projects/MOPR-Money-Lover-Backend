package com.mobile.base.model.entity;

import com.mobile.base.model.audit.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "db_base_permission")
@Getter
@Setter
public class Permission extends Auditable<String> {
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", type = com.mobile.base.service.id.IdGenerator.class)
    private Long id;

    private String name;

    private String action;

    private String description;

    private String nameGroup;

    private String code;

    private Boolean showMenu;

    @ManyToMany(mappedBy = "permissions")
    private List<Group> groups = new ArrayList<>();
}
