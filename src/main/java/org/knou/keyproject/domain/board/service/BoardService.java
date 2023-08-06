package org.knou.keyproject.domain.board.service;

import org.knou.keyproject.domain.board.dto.BoardPostRequestDto;
import org.knou.keyproject.domain.board.entity.Board;

public interface BoardService {
    Board saveNewBoard(BoardPostRequestDto requestDto);
}
