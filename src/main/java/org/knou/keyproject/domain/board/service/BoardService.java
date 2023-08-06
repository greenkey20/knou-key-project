package org.knou.keyproject.domain.board.service;

import org.knou.keyproject.domain.board.dto.BoardPostRequestDto;
import org.knou.keyproject.domain.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardService {
    Board saveNewBoard(BoardPostRequestDto requestDto);

    Page<Board> findAllBoards(Pageable pageable);

    Page<Board> findByTitleContainingOrContentContaining(String keyword, Pageable pageable);

    Board findBoardById(Long boardId);

    Board findVerifiedBoard(Long boardId);

    void increaseReadCount(Long boardId);

    Board updateBoard(Long boardId, BoardPostRequestDto requestDto);
}
