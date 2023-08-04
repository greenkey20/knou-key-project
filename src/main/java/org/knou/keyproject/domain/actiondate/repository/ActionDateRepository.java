package org.knou.keyproject.domain.actiondate.repository;

import org.knou.keyproject.domain.actiondate.entity.ActionDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

// 2023.8.4(금) 23h Querydsl 추가
public interface ActionDateRepository extends JpaRepository<ActionDate, Long>, ActionDateCustomRepository {
    List<ActionDate> findByPlanPlanId(Long planId);

    void deleteAllByPlanPlanId(Long planId);
}
