package org.knou.keyproject.domain.board.repository;

import org.knou.keyproject.domain.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// 2023.8.6(일) 20h35
public interface BoardRepository extends JpaRepository<Board, Long>, BoardCustomRepository {
    @Query("SELECT b FROM Board b WHERE b.content like %:keyword% or b.title like %:keyword%")
    Page<Board> findByTitleOrContentKeyword(@Param("keyword") String keyword, Pageable pageable);

    Page<Board> findAllByMemberMemberIdOrderByBoardIdDesc(Long memberId, Pageable pageable);
}
