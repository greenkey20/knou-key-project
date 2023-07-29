package org.knou.keyproject.domain.actiondate.repository;

import org.knou.keyproject.domain.actiondate.entity.ActionDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActionDateRepository extends JpaRepository<ActionDate, Long> {
    List<ActionDate> findByPlanPlanId(Long planId);

    void deleteAllByPlanPlanId(Long planId);
}
