package com.mobile.api.repository.jpa;

import com.mobile.api.model.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long>, JpaSpecificationExecutor<Reminder> {
}
