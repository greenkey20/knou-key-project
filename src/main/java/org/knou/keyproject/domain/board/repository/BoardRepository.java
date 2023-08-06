package org.knou.keyproject.domain.board.repository;

import org.knou.keyproject.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

// 2023.8.6(일) 20h35
public interface BoardRepository extends JpaRepository<Board, Long> {

}
