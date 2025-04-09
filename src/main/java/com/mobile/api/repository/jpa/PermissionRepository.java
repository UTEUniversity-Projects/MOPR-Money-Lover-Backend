package com.mobile.api.repository.jpa;

import com.mobile.api.model.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {
    boolean existsByAction(String action);

    boolean existsByCode(String code);

    Optional<Permission> findByActionOrCode(String action, String code);
}
