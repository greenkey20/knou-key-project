package org.knou.keyproject.domain.bookchapter.repository;

// 2023.8.23(수) 21h50
public interface BookChapterCustomRepository {
    Long findCountByPlanId(Long planId);
}
