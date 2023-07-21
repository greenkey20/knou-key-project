package org.knou.keyproject.domain.plan.repository;

import org.knou.keyproject.domain.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

// 2023.7.22(토) 1h55
public interface PlanRepository extends JpaRepository<Plan, Long> {
}
