package org.knou.keyproject.domain.plan.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.knou.keyproject.domain.plan.entity.QPlan;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

import static org.knou.keyproject.domain.plan.entity.QPlan.plan;

// 2023.8.4(금) 21h50
@RequiredArgsConstructor
@Repository
public class PlanCustomRepositoryImpl implements PlanCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
}
