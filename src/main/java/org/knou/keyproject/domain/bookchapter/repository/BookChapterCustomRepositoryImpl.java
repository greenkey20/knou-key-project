package org.knou.keyproject.domain.bookchapter.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import static org.knou.keyproject.domain.bookchapter.entity.QBookChapter.bookChapter;

@Slf4j
@RequiredArgsConstructor
@Repository
public class BookChapterCustomRepositoryImpl implements BookChapterCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Long findCountByPlanId(Long planId) {
        return jpaQueryFactory
                .select(bookChapter.bookChapterId.count())
                .from(bookChapter)
                .where(bookChapter.plan.planId.eq(planId))
                .fetchOne();
    }
}
