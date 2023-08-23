package org.knou.keyproject.domain.bookchapter.repository;

import org.knou.keyproject.domain.bookchapter.entity.BookChapter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookChapterRepository extends JpaRepository<BookChapter, Long>, BookChapterCustomRepository {
    List<BookChapter> findAllByPlanPlanIdOrderByBookChapterIdAsc(Long planId);

//    List<BookChapter> findAllByPlanPlanId(Long planId);
}
