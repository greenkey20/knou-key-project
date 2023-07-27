package org.knou.keyproject.domain.plan.repository;

import org.knou.keyproject.domain.plan.entity.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// 2023.7.22(토) 1h55
public interface PlanRepository extends JpaRepository<Plan, Long> {
        List<Plan> findByMemberMemberId(Long memberId, PageRequest of);

    // 2023.7.27(목) 21h10 reference(https://amongthestar.tistory.com/173) 보며 추가
    Page<Plan> findByMemberMemberId(Long memberId, Pageable pageable);

    // 2023.7.27(목) 22h
    Page<Plan> findByObjectContaining(String keyword, Pageable pageable);
}
